/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_ASYNC;
import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_SYNC;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;

import java.util.Collection;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.compare.INavigatable;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.internal.utils.DisposableResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoDifferencesCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label.NoVisibleItemCompareInput;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.EMFCompareColor;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeInput;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.EmptyComparisonScope;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressInfoComposite;
import org.eclipse.emf.compare.ide.ui.internal.progress.JobProgressMonitorWrapper;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.FetchListener;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.util.CompareHandlerService;
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IMergePreviewModeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.SideLabelProvider;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IColorChangeEvent;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.PseudoConflictsFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProviderChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.IDiagnosable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.progress.PendingUpdateAdapter;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * Implementation of {@link AbstractViewerWrapper}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EMFCompareStructureMergeViewer extends AbstractStructuredViewerWrapper<CTabFolder, WrappableTreeViewer> implements CommandStackListener {

	private final class CompareInputChangedJob extends Job {
		private CompareInputChangedJob(String name) {
			super(name);
		}

		@Override
		public IStatus run(IProgressMonitor monitor) {
			IProgressMonitor wrapper = new JobProgressMonitorWrapper(monitor, progressInfoItem);
			SubMonitor subMonitor = SubMonitor.convert(wrapper, EMFCompareIDEUIMessages
					.getString("EMFCompareStructureMergeViewer.computingModelDifferences"), 100); //$NON-NLS-1$
			try {
				compareInputChanged((ICompareInput)getInput(), subMonitor.newChild(100));
			} catch (final OperationCanceledException e) {
				return Status.CANCEL_STATUS;
			} catch (final Exception e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			} finally {
				subMonitor.setWorkRemaining(0);
			}
			return Status.OK_STATUS;
		}
	}

	private static final Predicate<Diff> UNRESOLVED_AND_WITHOUT_PSEUDO_CONFLICT = and(
			hasState(DifferenceState.UNRESOLVED), not(hasConflict(ConflictKind.PSEUDO)));

	/** The width of the tree ruler. */
	private static final int TREE_RULER_WIDTH = 17;

	private static final Predicate<? super Object> IS_DIFF = new Predicate<Object>() {
		public boolean apply(Object object) {
			return getDataOfTreeNodeOfAdapter(object) instanceof Diff;
		}
	};

	/** The adapter factory. */
	private ComposedAdapterFactory fAdapterFactory;

	/** The tree ruler associated with this viewer. */
	private EMFCompareDiffTreeRuler treeRuler;

	private final ICompareInputChangeListener fCompareInputChangeListener;

	/** The expand/collapse item listener. */
	private ITreeViewerListener fWrappedTreeListener;

	/** The tree viewer. */

	/** The undo action. */
	private UndoAction undoAction;

	/** The redo action. */
	private RedoAction redoAction;

	/** The compare handler service. */
	private CompareHandlerService fHandlerService;

	/**
	 * When comparing EObjects from a resource, the resource involved doesn't need to be unload by EMF
	 * Compare.
	 */
	private boolean resourceSetShouldBeDisposed;

	private DependencyData dependencyData;

	private ISelectionChangedListener selectionChangeListener;

	private final Listener fEraseItemListener;

	private JobProgressInfoComposite progressInfoItem;

	private Job inputChangedTask;

	private CompareToolBar toolBar;

	private boolean pseudoConflictsFilterEnabled;

	private Navigatable navigatable;

	private EMFCompareColor fColors;

	private boolean editingDomainNeedsToBeDisposed;

	private FetchListener toolbarUpdaterContentProviderListener;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the SWT parent control under which to create the viewer's SWT control.
	 * @param config
	 *            a compare configuration the newly created viewer might want to use.
	 */
	public EMFCompareStructureMergeViewer(Composite parent, EMFCompareConfiguration config) {
		super(parent, config);

		updateLayout(true, false);

		StructureMergeViewerFilter structureMergeViewerFilter = getCompareConfiguration()
				.getStructureMergeViewerFilter();
		getViewer().addFilter(structureMergeViewerFilter);

		StructureMergeViewerGrouper structureMergeViewerGrouper = getCompareConfiguration()
				.getStructureMergeViewerGrouper();
		structureMergeViewerGrouper.install(getViewer());

		fCompareInputChangeListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput input) {
				EMFCompareStructureMergeViewer.this.compareInputChanged(input);
			}
		};

		setContentProvider(new EMFCompareStructureMergeViewerContentProvider(getCompareConfiguration()
				.getAdapterFactory(), getViewer()));

		navigatable = new Navigatable(getViewer(), getContentProvider());

		toolBar = new CompareToolBar(CompareViewerPane.getToolBarManager(parent),
				structureMergeViewerGrouper, structureMergeViewerFilter, getCompareConfiguration());
		getViewer().addSelectionChangedListener(toolBar);

		createContextMenu();

		selectionChangeListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChangedEvent(event);
			}
		};
		addSelectionChangedListener(selectionChangeListener);

		fWrappedTreeListener = new ITreeViewerListener() {
			public void treeExpanded(TreeExpansionEvent event) {
				treeRuler.redraw();
			}

			public void treeCollapsed(TreeExpansionEvent event) {
				treeRuler.redraw();
			}
		};
		getViewer().addTreeListener(fWrappedTreeListener);

		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				handleEraseItemEvent(event);
			}
		};
		getViewer().getControl().addListener(SWT.EraseItem, fEraseItemListener);

		fHandlerService = CompareHandlerService.createFor(getCompareConfiguration().getContainer(),
				getControl().getShell());

		toolbarUpdaterContentProviderListener = new FetchListener() {

			@Override
			public void startFetching() {
				toolBar.setEnabled(false);
			}

			@Override
			public void doneFetching() {
				toolBar.setEnabled(true);
			}
		};

		getContentProvider().addFetchingListener(toolbarUpdaterContentProviderListener);

		setLabelProvider(new DelegatingStyledCellLabelProvider(
				new EMFCompareStructureMergeViewerLabelProvider(
						getCompareConfiguration().getAdapterFactory(), this)));

		undoAction = new UndoAction(getCompareConfiguration().getEditingDomain());
		redoAction = new RedoAction(getCompareConfiguration().getEditingDomain());

		editingDomainChange(null, getCompareConfiguration().getEditingDomain());

		inputChangedTask.setPriority(Job.LONG);

		config.getEventBus().register(this);
	}

	/**
	 * The tool bar must be init after we know the editable state of left and right input.
	 * 
	 * @see #compareInputChanged(ICompareInput, IProgressMonitor)
	 */
	private void initToolbar() {
		SWTUtil.safeAsyncExec(new Runnable() {

			public void run() {
				toolBar.initToolbar(getViewer(), navigatable);
			}
		});
	}

	/**
	 * Allow users to merge diffs through context menu.
	 */
	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EMFCompareStructureMergeViewer.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(getViewer().getControl());
		getViewer().getControl().setMenu(menu);
	}

	/**
	 * Fill the context menu with the appropriate actions (ACCEPT/REJECT or LEFT TO RIGHT/RIGHT TO LEFT
	 * depending on the {@link org.eclipse.emf.compare.internal.merge.MergeMode}, and the write access of
	 * models in input).
	 * 
	 * @param manager
	 *            the context menu to fill.
	 */
	private void fillContextMenu(IMenuManager manager) {
		if (!isDiffSelected()) {
			return;
		}
		boolean leftEditable = getCompareConfiguration().isLeftEditable();
		boolean rightEditable = getCompareConfiguration().isRightEditable();
		final EnumSet<MergeMode> modes;
		if (rightEditable && leftEditable) {
			modes = EnumSet.of(MergeMode.RIGHT_TO_LEFT, MergeMode.LEFT_TO_RIGHT);
		} else {
			modes = EnumSet.of(MergeMode.ACCEPT, MergeMode.REJECT);
		}
		if (rightEditable || leftEditable) {
			for (MergeMode mode : modes) {
				IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
				MergeAction mergeAction = new MergeAction(getCompareConfiguration().getEditingDomain(),
						mergerRegistry, mode, leftEditable, rightEditable, navigatable,
						(IStructuredSelection)getSelection());
				manager.add(mergeAction);
			}
		}
	}

	/**
	 * Check if the item selected in this viewer is a single Diff.
	 * 
	 * @return true if the item selected is a single Diff, false otherwise.
	 */
	private boolean isDiffSelected() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			if (getDataOfTreeNodeOfAdapter(element) instanceof Diff) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractViewerWrapper#preHookCreateControlAndViewer()
	 */
	@Override
	protected void preHookCreateControlAndViewer() {
		fAdapterFactory = new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault()
				.createFilteredAdapterFactoryRegistry());
		fAdapterFactory.addAdapterFactory(new TreeItemProviderAdapterFactorySpec());
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());

		getCompareConfiguration().setAdapterFactory(fAdapterFactory);

		inputChangedTask = new CompareInputChangedJob(EMFCompareIDEUIMessages
				.getString("EMFCompareStructureMergeViewer.computingModelDifferences")); //$NON-NLS-1$
	}

	@Subscribe
	public void colorChanged(
			@SuppressWarnings("unused")/* necessary for @Subscribe */IColorChangeEvent changeColorEvent) {
		internalRedraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see 
	 *      org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.ViewerWrapper.createControl(Composite,
	 *      CompareConfiguration)
	 */
	@Override
	protected ControlAndViewer<CTabFolder, WrappableTreeViewer> createControlAndViewer(Composite parent) {
		parent.setLayout(new FillLayout());
		CTabFolder tabFolder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
		tabFolder.setLayout(new FillLayout());

		// Ensures that this viewer will only display the page's tab
		// area if there are more than one page
		//
		tabFolder.addControlListener(new ControlAdapter() {
			boolean guard = false;

			@Override
			public void controlResized(ControlEvent event) {
				if (!guard) {
					guard = true;
					hideTabs();
					guard = false;
				}
			}
		});

		updateProblemIndication(Diagnostic.OK_INSTANCE);

		Composite control = new Composite(tabFolder, SWT.NONE);
		createItem(0, control);
		tabFolder.setSelection(0);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(data);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);

		progressInfoItem = new JobProgressInfoComposite(inputChangedTask, control, SWT.SMOOTH
				| SWT.HORIZONTAL, SWT.NONE);
		progressInfoItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		progressInfoItem.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		final WrappableTreeViewer treeViewer = new WrappableTreeViewer(control, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.viewers.TreeViewer#isExpandable(java.lang.Object)
			 */
			@Override
			public boolean isExpandable(Object element) {
				if (element instanceof PendingUpdateAdapter) {
					// Prevents requesting the content provider if the object is a PendingUpdateAdapter
					return false;
				}
				if (hasFilters()) {
					// workaround for 65762
					return getFilteredChildren(element).length > 0;
				}
				return super.isExpandable(element);
			}
		};
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setUseHashlookup(true);

		dependencyData = new DependencyData(getCompareConfiguration(), treeViewer);

		tabFolder.setData(CompareUI.COMPARE_VIEWER_TITLE, EMFCompareIDEUIMessages
				.getString("EMFCompareStructureMergeViewer.title")); //$NON-NLS-1$

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		final ITheme currentTheme;
		if (themeManager != null) {
			currentTheme = themeManager.getCurrentTheme();
		} else {
			currentTheme = null;
		}
		boolean leftIsLocal = getCompareConfiguration().getBooleanProperty("LEFT_IS_LOCAL", false); //$NON-NLS-1$
		fColors = new EMFCompareColor(control.getDisplay(), leftIsLocal, currentTheme,
				getCompareConfiguration().getEventBus());

		treeRuler = new EMFCompareDiffTreeRuler(control, SWT.NONE, treeViewer, dependencyData, fColors);
		GridData rulerLayoutData = new GridData(SWT.FILL, SWT.FILL, false, true);
		rulerLayoutData.exclude = true;
		rulerLayoutData.widthHint = TREE_RULER_WIDTH;
		rulerLayoutData.minimumWidth = TREE_RULER_WIDTH;
		treeRuler.setLayoutData(rulerLayoutData);

		return ControlAndViewer.create(tabFolder, treeViewer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getContentProvider()
	 */
	@Override
	public EMFCompareStructureMergeViewerContentProvider getContentProvider() {
		return (EMFCompareStructureMergeViewerContentProvider)super.getContentProvider();
	}

	private CTabItem createItem(int index, Control control) {
		CTabItem item = new CTabItem((CTabFolder)control.getParent(), SWT.NONE, index);
		item.setControl(control);
		return item;
	}

	@Subscribe
	public void handleEditingDomainChange(ICompareEditingDomainChange event) {
		editingDomainChange(event.getOldValue(), event.getNewValue());
	}

	private void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		if (newValue != oldValue) {
			if (oldValue != null) {
				oldValue.getCommandStack().removeCommandStackListener(this);
			}

			if (newValue != null) {
				newValue.getCommandStack().addCommandStackListener(this);
			}

			undoAction.setEditingDomain(newValue);
			redoAction.setEditingDomain(newValue);
		}
	}

	private void refreshTitle() {
		Composite parent = getControl().getParent();
		if (parent instanceof CompareViewerSwitchingPane) {
			int displayedDiff = JFaceUtil.filterVisibleElement(getViewer(), IS_DIFF).size();
			Comparison comparison = getCompareConfiguration().getComparison();
			if (comparison != null) {
				List<Diff> differences = comparison.getDifferences();
				final int computedDiff;
				if (pseudoConflictsFilterEnabled) {
					computedDiff = size(Iterables.filter(differences, not(hasConflict(ConflictKind.PSEUDO))));
				} else {
					computedDiff = differences.size();
				}
				int filteredDiff = differences.size() - displayedDiff;
				if (filteredDiff < 0) {
					// some differences (conflicts in default view) are displayed twice,
					// use this workaround to avoid displayed negative numbers, but we have
					// to know that we display wrong number.
					filteredDiff = 0;
				}
				final int differencesToMerge;
				if (pseudoConflictsFilterEnabled) {
					differencesToMerge = size(Iterables.filter(differences,
							UNRESOLVED_AND_WITHOUT_PSEUDO_CONFLICT));
				} else {
					differencesToMerge = size(Iterables.filter(differences,
							hasState(DifferenceState.UNRESOLVED)));
				}
				((CompareViewerSwitchingPane)parent).setTitleArgument(EMFCompareIDEUIMessages.getString(
						"EMFCompareStructureMergeViewer.titleDesc", differencesToMerge, computedDiff, //$NON-NLS-1$
						filteredDiff));
			}
		}
	}

	static EObject getDataOfTreeNodeOfAdapter(Object object) {
		EObject data = null;
		if (object instanceof Adapter) {
			Notifier target = ((Adapter)object).getTarget();
			if (target instanceof TreeNode) {
				data = ((TreeNode)target).getData();
			}
		}
		return data;
	}

	@Subscribe
	public void mergePreviewModeChange(@SuppressWarnings("unused") IMergePreviewModeChange event) {
		final IMerger.Registry registry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				dependencyData.updateDependencies(getSelection(), registry);
				internalRedraw();
			}
		});
	}

	@Subscribe
	public void handleDifferenceFilterChange(IDifferenceFilterChange event) {
		pseudoConflictsFilterEnabled = any(event.getSelectedDifferenceFilters(),
				instanceOf(PseudoConflictsFilter.class));
		SWTUtil.safeRefresh(this, false, true);
		getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
			public void run() {
				if (navigatable != null
						&& (navigatable.getViewer().getSelection() == null || navigatable.getViewer()
								.getSelection().isEmpty())) {
					selectFirstDiffOrDisplayLabelViewer(getCompareConfiguration().getComparison());
				}
			}
		});
	}

	@Subscribe
	public void handleDifferenceGroupProviderChange(
			@SuppressWarnings("unused") IDifferenceGroupProviderChange event) {
		SWTUtil.safeRefresh(this, false, true);
		getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
			public void run() {
				selectFirstDiffOrDisplayLabelViewer(getCompareConfiguration().getComparison());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(Object, Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		if (oldInput instanceof ICompareInput) {
			ICompareInput old = (ICompareInput)oldInput;
			old.removeCompareInputChangeListener(fCompareInputChangeListener);
			toolBar.dispose();
		}
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.addCompareInputChangeListener(fCompareInputChangeListener);
			compareInputChanged(ci);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractViewerWrapper#handleDispose(DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		if (fHandlerService != null) {
			fHandlerService.dispose();
		}
		getCompareConfiguration().getEventBus().unregister(this);
		getViewer().removeTreeListener(fWrappedTreeListener);
		Object input = getInput();
		if (input instanceof ICompareInput) {
			ICompareInput ci = (ICompareInput)input;
			ci.removeCompareInputChangeListener(fCompareInputChangeListener);
		}
		removeSelectionChangedListener(selectionChangeListener);
		getViewer().removeSelectionChangedListener(toolBar);
		getViewer().getTree().removeListener(SWT.EraseItem, fEraseItemListener);
		if (editingDomainNeedsToBeDisposed) {
			((IDisposable)getCompareConfiguration().getEditingDomain()).dispose();
		}
		compareInputChanged((ICompareInput)null);
		treeRuler.handleDispose();
		fAdapterFactory.dispose();
		toolBar.dispose();
		fColors.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		undoAction.update();
		redoAction.update();

		Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
		if (mostRecentCommand instanceof ICompareCopyCommand) {
			Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();

			if (!affectedObjects.isEmpty()) {
				// MUST NOT call a setSelection with a list, o.e.compare does not handle it (cf
				// org.eclipse.compare.CompareEditorInput#getElement(ISelection))
				final Iterator<EObject> affectedIterator = Iterables.filter(affectedObjects, EObject.class)
						.iterator();
				IDifferenceGroupProvider groupProvider = getCompareConfiguration()
						.getStructureMergeViewerGrouper().getProvider();
				TreeNode unfilteredNode = null;
				while (affectedIterator.hasNext() && unfilteredNode == null) {
					EObject affected = affectedIterator.next();
					Iterable<TreeNode> treeNodes = groupProvider.getTreeNodes(affected);
					for (TreeNode node : treeNodes) {
						if (!JFaceUtil.isFiltered(getViewer(), node, node.getParent())) {
							unfilteredNode = node;
						}
					}
				}
				if (unfilteredNode != null) {
					final Object adaptedAffectedObject = fAdapterFactory.adapt(unfilteredNode,
							ICompareInput.class);
					// execute synchronously the set selection to be sure the MergeAction#run() will
					// select next diff after.
					SWTUtil.safeSyncExec(new Runnable() {
						public void run() {
							refresh();
							StructuredSelection selection = new StructuredSelection(adaptedAffectedObject);
							// allows to call CompareToolBar#selectionChanged(SelectionChangedEvent)
							getViewer().setSelection(selection);
						}
					});
					// update content viewers with the new selection
					SWTUtil.safeAsyncExec(new Runnable() {
						public void run() {
							navigatable.openSelectedChange();
						}
					});
				}
			}
		} else {
			// FIXME, should recompute the difference, something happened outside of this compare editor
		}

	}

	/**
	 * Triggered by fCompareInputChangeListener and {@link #inputChanged(Object, Object)}.
	 */
	void compareInputChanged(ICompareInput input) {
		if (input == null) {
			// When closing, we don't need a progress monitor to handle the input change
			compareInputChanged((ICompareInput)null, new NullProgressMonitor());
			return;
		}
		// The compare configuration is nulled when the viewer is disposed
		if (getCompareConfiguration() != null) {
			updateLayout(true, true);
			inputChangedTask.schedule();
		}
	}

	void compareInputChanged(CompareInputAdapter input, IProgressMonitor monitor) {
		compareInputChanged(null, (Comparison)input.getComparisonObject());
	}

	void compareInputChanged(ComparisonScopeInput input, IProgressMonitor monitor) {
		EMFCompare comparator = getCompareConfiguration().getEMFComparator();

		IComparisonScope comparisonScope = input.getComparisonScope();
		final Comparison comparison = comparator.compare(comparisonScope, BasicMonitor.toMonitor(monitor));

		compareInputChanged(input.getComparisonScope(), comparison);
	}

	void compareInputChanged(final IComparisonScope scope, final Comparison comparison) {
		if (!getControl().isDisposed()) { // guard against disposal
			final TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
			treeNode.setData(comparison);
			final Object input = fAdapterFactory.adapt(treeNode, ICompareInput.class);

			// this will set to the EMPTY difference group provider, but necessary to avoid NPE while setting
			// input.
			IDifferenceGroupProvider groupProvider = getCompareConfiguration()
					.getStructureMergeViewerGrouper().getProvider();
			treeNode.eAdapters().add(groupProvider);

			// display problem tabs if any
			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					updateProblemIndication(comparison.getDiagnostic());
				}
			});

			// must set the input now in a synchronous mean. It will be used in the #setComparisonAndScope
			// afterwards during the initialization of StructureMergeViewerFilter and
			// StructureMergeViewerGrouper.
			SWTUtil.safeSyncExec(new Runnable() {
				public void run() {
					getViewer().setInput(input);
				}
			});

			getCompareConfiguration().setComparisonAndScope(comparison, scope);

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					if (!getControl().isDisposed()) {
						updateLayout(false, true);
					}
				}
			});

			getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {
				public void run() {
					if (!getControl().isDisposed()) {
						// Needs all to build all treeItem to compute the label
						dependencyData.updateTreeItemMappings();

						// title is not initialized as the comparison was set in the configuration after the
						// refresh caused by the initialization of the viewer filters and the group providers.
						refreshTitle();

						// Selects the first difference once the tree has been filled.
						selectFirstDiffOrDisplayLabelViewer(comparison);
					}
				}
			});

			SWTUtil.safeAsyncExec(new Runnable() {
				public void run() {
					fHandlerService.updatePaneActionHandlers(new Runnable() {
						public void run() {
							fHandlerService.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
							fHandlerService.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

						}
					});
				}
			});
		}
	}

	void compareInputChanged(final ICompareInput input, IProgressMonitor monitor) {
		if (input != null) {
			if (input instanceof CompareInputAdapter) {
				resourceSetShouldBeDisposed = false;
				compareInputChanged((CompareInputAdapter)input, monitor);
			} else if (input instanceof ComparisonScopeInput) {
				resourceSetShouldBeDisposed = false;
				compareInputChanged((ComparisonScopeInput)input, monitor);
			} else {
				resourceSetShouldBeDisposed = true;
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

				final ITypedElement left = input.getLeft();
				final ITypedElement right = input.getRight();
				final ITypedElement origin = input.getAncestor();

				final boolean leftEditable;
				final boolean rightEditable;

				EMFCompareConfiguration compareConfiguration = getCompareConfiguration();
				/**
				 * A resource node means that the left ITypedElement is in the workspace, a DiffNode input
				 * means the comparison has been launched from a Replace With action.
				 */
				if (left instanceof ResourceNode && !(input instanceof DiffNode)) {
					IResource resource = ((ResourceNode)left).getResource();
					leftEditable = !resource.getResourceAttributes().isReadOnly();
				} else {
					leftEditable = compareConfiguration.isLeftEditable();
				}

				if (right instanceof ResourceNode) {
					IResource resource = ((ResourceNode)right).getResource();
					rightEditable = !resource.getResourceAttributes().isReadOnly();
				} else {
					rightEditable = compareConfiguration.isRightEditable();
				}

				compareConfiguration.setLeftEditable(leftEditable);
				compareConfiguration.setRightEditable(rightEditable);

				if (leftEditable && rightEditable) {
					compareConfiguration.setMergePreviewMode(MergeMode.RIGHT_TO_LEFT);
				} else {
					compareConfiguration.setMergePreviewMode(MergeMode.ACCEPT);
				}

				final BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK,
						EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[0]);
				IComparisonScope scope = null;

				try {
					scope = ComparisonScopeBuilder.create(compareConfiguration.getContainer(), left, right,
							origin, subMonitor.newChild(85));
				} catch (OperationCanceledException e) {
					scope = new EmptyComparisonScope();
					((BasicDiagnostic)((EmptyComparisonScope)scope).getDiagnostic())
							.merge(new BasicDiagnostic(Diagnostic.CANCEL, EMFCompareIDEUIPlugin.PLUGIN_ID, 0,
									EMFCompareIDEUIMessages
											.getString("EMFCompareStructureMergeViewer.operationCanceled"), //$NON-NLS-1$
									new Object[] {e, }));
				} catch (Exception e) {
					scope = new EmptyComparisonScope();
					((BasicDiagnostic)((EmptyComparisonScope)scope).getDiagnostic()).merge(BasicDiagnostic
							.toDiagnostic(e));
					EMFCompareIDEUIPlugin.getDefault().log(e);
				}

				if (scope instanceof IDiagnosable && ((IDiagnosable)scope).getDiagnostic() != null) {
					diagnostic.merge(((IDiagnosable)scope).getDiagnostic());
				}

				final Builder comparisonBuilder = EMFCompare.builder().setPostProcessorRegistry(
						EMFCompareRCPPlugin.getDefault().getPostProcessorRegistry());

				EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);

				final Comparison compareResult = comparisonBuilder.build().compare(scope,
						BasicMonitor.toMonitor(subMonitor.newChild(15)));

				compareResult.eAdapters().add(new ForwardingCompareInputAdapter(input));
				ICompareInputLabelProvider labelProvider = getCompareConfiguration().getLabelProvider();
				SideLabelProvider sideLabelProvider = new SideLabelProvider(labelProvider
						.getAncestorLabel(input), labelProvider.getLeftLabel(input), labelProvider
						.getRightLabel(input), labelProvider.getAncestorImage(input), labelProvider
						.getLeftImage(input), labelProvider.getRightImage(input));
				compareResult.eAdapters().add(sideLabelProvider);

				if (compareResult.getDiagnostic() != null) {
					diagnostic.merge(compareResult.getDiagnostic());
				}
				// update diagnostic of the comparison with the global one.
				compareResult.setDiagnostic(diagnostic);

				final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
				final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
				final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

				ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(leftResourceSet,
						rightResourceSet, originResourceSet);
				editingDomainNeedsToBeDisposed = true;
				compareConfiguration.setEditingDomain(editingDomain);

				compareInputChanged(scope, compareResult);
			}
			initToolbar();
		} else {
			compareInputChangedToNull();
		}
	}

	/**
	 * Select the first difference...if there are differences, otherwise, display appropriate content viewer
	 * (no differences or no visible differences)
	 * 
	 * @param comparison
	 *            the comparison used to know if there are differences.
	 */
	private void selectFirstDiffOrDisplayLabelViewer(final Comparison comparison) {
		if (comparison != null) {
			ICompareInput compareInput = (ICompareInput)EcoreUtil.getAdapter(comparison.eAdapters(),
					ICompareInput.class);
			List<Diff> differences = comparison.getDifferences();
			if (differences.isEmpty()) {
				navigatable.fireOpen(new NoDifferencesCompareInput(compareInput));
			} else if (JFaceUtil.filterVisibleElement(getViewer(), IS_DIFF).isEmpty()) {
				navigatable.fireOpen(new NoVisibleItemCompareInput(compareInput));
			} else {
				navigatable.selectChange(INavigatable.FIRST_CHANGE);
			}
		}
	}

	private void updateLayout(boolean displayProgress, boolean doLayout) {
		((GridData)progressInfoItem.getLayoutData()).exclude = !displayProgress;
		progressInfoItem.setVisible(displayProgress);

		((GridData)getViewer().getControl().getLayoutData()).exclude = displayProgress;
		getViewer().getControl().setVisible(!displayProgress);

		((GridData)treeRuler.getLayoutData()).exclude = displayProgress;
		treeRuler.setVisible(!displayProgress);

		if (doLayout) {
			getControl().layout(true, true);
		}
	}

	private void compareInputChangedToNull() {
		if (!inputChangedTask.cancel()) {
			try {
				inputChangedTask.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Throwables.propagate(e);
			}
		}

		ResourceSet leftResourceSet = null;
		ResourceSet rightResourceSet = null;
		ResourceSet originResourceSet = null;

		if (getCompareConfiguration().getComparison() != null) {
			Comparison comparison = getCompareConfiguration().getComparison();
			Iterator<Match> matchIt = comparison.getMatches().iterator();
			if (comparison.isThreeWay()) {
				while (matchIt.hasNext()
						&& (leftResourceSet == null || rightResourceSet == null || originResourceSet == null)) {
					Match match = matchIt.next();
					if (leftResourceSet == null) {
						leftResourceSet = getResourceSet(match.getLeft());
					}
					if (rightResourceSet == null) {
						rightResourceSet = getResourceSet(match.getRight());
					}
					if (originResourceSet == null) {
						originResourceSet = getResourceSet(match.getOrigin());
					}
				}
			} else {
				while (matchIt.hasNext() && (leftResourceSet == null || rightResourceSet == null)) {
					Match match = matchIt.next();
					if (leftResourceSet == null) {
						leftResourceSet = getResourceSet(match.getLeft());
					}
					if (rightResourceSet == null) {
						rightResourceSet = getResourceSet(match.getRight());
					}
				}
			}
		}

		editingDomainChange(getCompareConfiguration().getEditingDomain(), null);

		if (resourceSetShouldBeDisposed) {
			disposeResourceSet(leftResourceSet);
			disposeResourceSet(rightResourceSet);
			disposeResourceSet(originResourceSet);
		}

		if (getCompareConfiguration() != null) {
			getCompareConfiguration().dispose();
		}
		getViewer().setInput(null);
	}

	/**
	 * Disposes the {@link ResourceSet}.
	 * 
	 * @param resourceSet
	 *            that need to be disposed.
	 */
	protected void disposeResourceSet(ResourceSet resourceSet) {
		if (resourceSet instanceof DisposableResourceSet) {
			((DisposableResourceSet)resourceSet).dispose();
		} else {
			unload(resourceSet);
		}
	}

	/**
	 * Handle the erase item event. When select a difference in the structure merge viewer, highlight required
	 * differences with a specific color, and highlight unmergeable differences with another color.
	 * 
	 * @param event
	 *            the erase item event.
	 */
	private void handleEraseItemEvent(Event event) {
		TreeItem item = (TreeItem)event.item;
		EObject dataItem = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(item.getData());
		if (dataItem != null) {
			final Set<Diff> requires = dependencyData.getRequires();
			final Set<Diff> rejectedDiffs = dependencyData.getRejections();
			final GC g = event.gc;
			if (requires.contains(dataItem)) {
				paintItemBackground(g, item, fColors.getRequiredFillColor());
			} else if (rejectedDiffs.contains(dataItem)) {
				paintItemBackground(g, item, fColors.getUnmergeableFillColor());
			}
		}
	}

	/**
	 * Paint the background of the given item with the given color.
	 * 
	 * @param g
	 *            the GC associated to the item.
	 * @param item
	 *            the given item.
	 * @param color
	 *            the given color.
	 */
	private void paintItemBackground(GC g, TreeItem item, Color color) {
		Rectangle itemBounds = item.getBounds();
		Tree tree = item.getParent();
		Rectangle areaBounds = tree.getClientArea();
		g.setClipping(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
		g.setBackground(color);
		g.fillRectangle(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
	}

	private void updateProblemIndication(Diagnostic diagnostic) {
		int lastEditorPage = getPageCount() - 1;
		if (lastEditorPage >= 0 && getItemControl(lastEditorPage) instanceof ProblemIndicationComposite) {
			((ProblemIndicationComposite)getItemControl(lastEditorPage)).setDiagnostic(diagnostic);
			if (diagnostic.getSeverity() != Diagnostic.OK) {
				setActivePage(lastEditorPage);
				updateLayout(false, true);
			}
		} else if (diagnostic.getSeverity() != Diagnostic.OK) {
			ProblemIndicationComposite problemIndicationComposite = new ProblemIndicationComposite(
					getControl(), SWT.NONE);
			problemIndicationComposite.setDiagnostic(diagnostic);
			createItem(++lastEditorPage, problemIndicationComposite);
			getControl().getItem(lastEditorPage).setText(
					CommonUIPlugin.getPlugin().getString("_UI_Problems_label")); //$NON-NLS-1$
			setActivePage(lastEditorPage);
			updateLayout(false, true);
			showTabs();
		}
	}

	private void showTabs() {
		if (getPageCount() > 1) {
			getControl().getItem(0).setText(
					EMFCompareIDEUIMessages.getString("EMFCompareStructureMergeViewer.tabItem.0.title")); //$NON-NLS-1$
			getControl().setTabHeight(SWT.DEFAULT);
			Point point = getControl().getSize();
			getControl().setSize(point.x, point.y - 6);
		}
	}

	private void hideTabs() {
		if (getPageCount() <= 1) {
			getControl().getItem(0).setText(""); //$NON-NLS-1$
			getControl().setTabHeight(1);
			Point point = getControl().getSize();
			getControl().setSize(point.x, point.y + 6);
		}
	}

	private void setActivePage(int pageIndex) {
		Assert.isTrue(pageIndex >= 0 && pageIndex < getPageCount());
		getControl().setSelection(pageIndex);
	}

	private int getPageCount() {
		// May not have been created yet, or may have been disposed.
		if (getControl() != null && !getControl().isDisposed()) {
			return getControl().getItemCount();
		}
		return 0;
	}

	private Control getItemControl(int itemIndex) {
		CTabItem item = getControl().getItem(itemIndex);
		if (item != null) {
			return item.getControl();
		}
		return null;
	}

	private static void unload(ResourceSet resourceSet) {
		if (resourceSet != null) {
			for (Resource resource : resourceSet.getResources()) {
				resource.unload();
			}
			resourceSet.getResources().clear();
		}
	}

	private static ResourceSet getResourceSet(EObject eObject) {
		if (eObject != null) {
			Resource eResource = eObject.eResource();
			if (eResource != null) {
				return eResource.getResourceSet();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	@Override
	protected void internalRefresh(Object element) {
		// Postpones the refresh if the content provider is in pending mode
		getContentProvider().runWhenReady(IN_UI_SYNC, new Runnable() {

			public void run() {
				getViewer().refresh();
			}
		});
		// Updates dependency data when the viewer has been refreshed and the content provider is ready.
		getContentProvider().runWhenReady(IN_UI_SYNC, new Runnable() {
			public void run() {
				dependencyData.updateTreeItemMappings();
				dependencyData.updateDependencies(getSelection(), EMFCompareRCPPlugin.getDefault()
						.getMergerRegistry());

				internalRedraw();

			}
		});
		// Needs dependency data however do not need to be run in UI thread
		getContentProvider().runWhenReady(IN_UI_ASYNC, new Runnable() {

			public void run() {
				refreshTitle();
			}
		});

	}

	private void handleSelectionChangedEvent(SelectionChangedEvent event) {
		dependencyData.updateDependencies(event.getSelection(), EMFCompareRCPPlugin.getDefault()
				.getMergerRegistry());
		internalRedraw();
	}

	/**
	 * We need to call redraw() on the tree and the tree ruler because getControl().redraw() doesn't propagate
	 * the redraw on its sub components under windows platform.
	 */
	private void internalRedraw() {
		getViewer().getTree().redraw();
		treeRuler.redraw();
	}
}
