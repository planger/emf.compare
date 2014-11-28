/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.annotation.MergeItemAnnotation;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeContentMergeViewer extends EMFCompareContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = TreeContentMergeViewer.class.getName();

	/**
	 * The {@link org.eclipse.emf.common.notify.AdapterFactory} used to create
	 * {@link AdapterFactoryContentProvider} and {@link AdapterFactoryLabelProvider} for ancestor, left and
	 * right {@link org.eclipse.jface.viewers.TreeViewer}.
	 */
	private final ComposedAdapterFactory fAdapterFactory;

	private AtomicBoolean fSyncExpandedState;

	private double[] fBasicCenterCurve;

	private ModelCommentPopupDialog popup;

	private MouseTrackListener mouseTrackListener;

	/**
	 * Creates a new {@link TreeContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public TreeContentMergeViewer(Composite parent, EMFCompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);

		fAdapterFactory = new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault()
				.createFilteredAdapterFactoryRegistry());
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		fSyncExpandedState = new AtomicBoolean();

		buildControl(parent);
		setContentProvider(new TreeContentMergeViewerContentProvider(config));
		getCenterControl().addMouseTrackListener(getMouseTrackListener());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fAdapterFactory.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getAncestorMergeViewer() {
		return (TreeMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getLeftMergeViewer() {
		return (TreeMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getRightMergeViewer() {
		return (TreeMergeViewer)super.getRightMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractMergeViewer createMergeViewer(final Composite parent, final MergeViewerSide side) {
		final TreeMergeViewer mergeTreeViewer = new TreeMergeViewer(parent, side, this,
				getCompareConfiguration());
		IContentProvider contentProvider = new AdapterFactoryContentProvider(fAdapterFactory) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
			 */
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof ICompareAccessor) {
					return ((ICompareAccessor)object).getItems().toArray();
				}
				return super.getElements(object);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
			 */
			@Override
			public Object[] getChildren(Object object) {
				if (object instanceof IMergeViewerItem.Container) {
					IMergeViewerItem[] children = ((IMergeViewerItem.Container)object).getChildren(
							getDifferenceGroupProvider(), getDifferenceFilterPredicate());

					return children;
				}
				return super.getChildren(object);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(java.lang.Object)
			 */
			@Override
			public boolean hasChildren(Object object) {
				if (object instanceof IMergeViewerItem.Container) {
					return ((IMergeViewerItem.Container)object).hasChildren(getDifferenceGroupProvider(),
							getDifferenceFilterPredicate());
				}
				return super.hasChildren(object);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getParent(java.lang.Object)
			 */
			@Override
			public Object getParent(Object object) {
				if (object instanceof IMergeViewerItem.Container) {
					return ((IMergeViewerItem.Container)object).getParent();
				}
				return super.getParent(object);
			}
		};
		mergeTreeViewer.setContentProvider(contentProvider);
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(fAdapterFactory) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object object) {
				if (object instanceof IMergeViewerItem) {
					final String text;
					IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
					final Object value = mergeViewerItem.getSideValue(side);
					if (value instanceof EObject && ((EObject)value).eIsProxy()) {
						text = "proxy : " + ((InternalEObject)value).eProxyURI().toString(); //$NON-NLS-1$
					} else if (mergeViewerItem.isInsertionPoint()) {
						// workaround for 406513: Windows specific issue. Only labels of (Tree/Table)Item are
						// selectable on Windows platform. The labels of placeholders in (Tree/Table)Viewer
						// are one whitespace. Placeholder are then selectable at the very left of itself.
						// Add a 42 whitespaces label to workaround.
						text = "                                          "; //$NON-NLS-1$
					} else if (value == null
							&& mergeViewerItem.getSideValue(side.opposite()) instanceof Resource) {
						text = getResourceBundle().getString("UnkownResource"); //$NON-NLS-1$
					} else if (value == null && mergeViewerItem.getLeft() == null
							&& mergeViewerItem.getRight() == null
							&& mergeViewerItem.getAncestor() instanceof Resource) {
						text = getResourceBundle().getString("UnkownResource"); //$NON-NLS-1$
					} else {
						text = super.getText(value);
					}
					return text;
				}
				return super.getText(object);
			}

			@Override
			public Image getImage(Object object) {
				if (object instanceof IMergeViewerItem) {
					IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
					if (mergeViewerItem.isInsertionPoint()) {
						return null;
					} else if (mergeViewerItem.getSideValue(side) == null
							&& mergeViewerItem.getSideValue(side.opposite()) instanceof Resource) {
						return super.getImage(mergeViewerItem.getSideValue(side.opposite()));
					} else if (mergeViewerItem.getLeft() == null && mergeViewerItem.getRight() == null
							&& mergeViewerItem.getAncestor() instanceof Resource) {
						return super.getImage(mergeViewerItem.getAncestor());
					} else {
						return super.getImage(mergeViewerItem.getSideValue(side));
					}
				}
				return super.getImage(object);
			}
		};
		mergeTreeViewer.setLabelProvider(labelProvider);

		mergeTreeViewer.getStructuredViewer().getTree().addListener(SWT.Collapse,
				new ExpandCollapseListener(mergeTreeViewer, false));
		mergeTreeViewer.getStructuredViewer().getTree().addListener(SWT.Expand,
				new ExpandCollapseListener(mergeTreeViewer, true));

		mergeTreeViewer.getStructuredViewer().getTree().getVerticalBar().addListener(SWT.Selection,
				new Listener() {
					public void handleEvent(Event event) {
						redrawCenterControl();
					}
				});

		mergeTreeViewer.getStructuredViewer().getTree().addMouseWheelListener(new MouseWheelListener() {
			public void mouseScrolled(MouseEvent e) {
				redrawCenterControl();
			}
		});
		mergeTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				redrawCenterControl();
			}
		});

		return mergeTreeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(GC g) {
		TreeMergeViewer leftMergeViewer = getLeftMergeViewer();
		TreeMergeViewer rightMergeViewer = getRightMergeViewer();

		Tree leftTree = leftMergeViewer.getStructuredViewer().getTree();
		Tree rightTree = rightMergeViewer.getStructuredViewer().getTree();

		Rectangle leftClientArea = leftTree.getClientArea();
		Rectangle rightClientArea = rightTree.getClientArea();

		final List<TreeItem> leftItems = getExpandedTreeItems(leftTree);
		final List<TreeItem> rightItems = getExpandedTreeItems(rightTree);

		final ImmutableSet<TreeItem> selection = ImmutableSet.copyOf(leftTree.getSelection());

		for (TreeItem leftItem : leftItems) {
			final boolean selected = Iterables.any(selection, equalTo(leftItem));
			IMergeViewerItem leftData = (IMergeViewerItem)leftItem.getData();
			final Diff leftDiff = leftData.getDiff();
			if (leftDiff != null) {
				if (MergeViewerUtil.isVisibleInMergeViewer(leftDiff, getDifferenceGroupProvider(),
						getDifferenceFilterPredicate())
						&& !MergeViewerUtil.isMarkAsMerged(leftDiff, leftData, getCompareConfiguration())) {
					TreeItem rightItem = findRightTreeItemFromLeftDiff(rightItems, leftDiff, leftData);

					if (rightItem != null) {
						final Color strokeColor = getCompareColor().getStrokeColor(leftDiff, isThreeWay(),
								false, selected);
						g.setForeground(strokeColor);
						drawCenterLine(g, leftClientArea, rightClientArea, leftItem, rightItem);
					}
				}
			}
		}
		for (TreeItem rightItem : rightItems) {
			IMergeViewerItem rightData = (IMergeViewerItem)rightItem.getData();
			if (rightData.getRightAnnotation() != null) {
				drawAnnotation(g, rightItem);
			}
		}
	}

	private MouseTrackListener getMouseTrackListener() {
		if (mouseTrackListener == null) {
			final Tree tree = getRightMergeViewer().getStructuredViewer().getTree();
			mouseTrackListener = new MouseTrackListener() {

				public void mouseHover(MouseEvent e) {
					// FIXME
					TreeItem item = tree.getItem(new Point(e.x + 40, e.y));
					if (item == null) {
						return;
					}
					IMergeViewerItem itemData = (IMergeViewerItem)item.getData();
					if (itemData != null && itemData.getRightAnnotation() != null) {
						showMessage(tree.getShell(), tree.toDisplay(item.getBounds().x, item.getBounds().y),
								itemData.getRightAnnotation().getHeader(), itemData.getRightAnnotation()
										.getDescription());
					}
				}

				public void mouseExit(MouseEvent e) {
				}

				public void mouseEnter(MouseEvent e) {
				}
			};
		}
		return mouseTrackListener;
	}

	protected void showMessage(Shell parent, Point point, String header, String description) {
		if (popup == null) {
			popup = new ModelCommentPopupDialog(parent, SWT.NO_FOCUS | SWT.ON_TOP, header, description);
			popup.create();
		}
		popup.open();
		popup.setLocation(point);
	}

	private void drawAnnotation(GC g, TreeItem rightItem) {
		IMergeViewerItem rightData = (IMergeViewerItem)rightItem.getData();
		MergeItemAnnotation rightAnnotation = rightData.getRightAnnotation();
		Control control = getCenterControl();
		Label imageLabel = new Label(control.getParent(), SWT.IMAGE_GIF);
		imageLabel.setImage(rightAnnotation.getImage());

		int x = control.getBounds().width / 2;
		Rectangle rightBounds = rightItem.getBounds();
		if (rightAnnotation.getImage() != null) {
			g.drawImage(rightAnnotation.getImage(), x, rightBounds.y);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		super.createToolItems(toolBarManager);
		IContributionItem[] items = toolBarManager.getItems();
		for (IContributionItem iContributionItem : items) {
			if (iContributionItem instanceof ActionContributionItem) {
				IAction action = ((ActionContributionItem)iContributionItem).getAction();
				String id = action.getActionDefinitionId();
				if ("org.eclipse.compare.copyAllLeftToRight".equals(id)) { //$NON-NLS-1$
					toolBarManager.remove(iContributionItem);
				} else if ("org.eclipse.compare.copyAllRightToLeft".equals(id)) { //$NON-NLS-1$
					toolBarManager.remove(iContributionItem);
				}
			}
		}
	}

	private List<TreeItem> getExpandedTreeItems(Tree tree) {
		return getExpandedTreeItems(tree.getItems());
	}

	/**
	 * @param items
	 * @return
	 */
	private List<TreeItem> getExpandedTreeItems(TreeItem[] items) {
		List<TreeItem> ret = newArrayList();
		for (TreeItem item : items) {
			ret.add(item);
			if (!item.getExpanded()) {
				continue;
			}
			ret.addAll(getExpandedTreeItems(item.getItems()));
		}
		return ret;
	}

	private void drawCenterLine(GC g, Rectangle leftClientArea, Rectangle rightClientArea, TreeItem leftItem,
			TreeItem rightItem) {
		Control control = getCenterControl();
		Point from = new Point(0, 0);
		Point to = new Point(0, 0);

		Rectangle leftBounds = leftItem.getBounds();
		Rectangle rightBounds = rightItem.getBounds();

		from.y = leftBounds.y + (leftBounds.height / 2) - leftClientArea.y;
		if ("gtk".equals(SWT.getPlatform())) { //$NON-NLS-1$
			from.y -= 1;
		} else if ("win32".equals(SWT.getPlatform())) { //$NON-NLS-1$
			from.y += 1;
		}

		to.x = control.getBounds().width;
		to.y = rightBounds.y + (rightBounds.height / 2) - rightClientArea.y;
		if ("gtk".equals(SWT.getPlatform())) { //$NON-NLS-1$
			to.y -= 1;
		} else if ("win32".equals(SWT.getPlatform())) { //$NON-NLS-1$
			to.y += 1;
		}

		int[] points = getCenterCurvePoints(from, to);
		for (int i = 1; i < points.length; i++) {
			g.drawLine(from.x + i - 1, points[i - 1], i, points[i]);
		}
	}

	private TreeItem findRightTreeItemFromLeftDiff(List<TreeItem> rightItems, Diff leftDiff,
			IMergeViewerItem leftData) {
		TreeItem ret = null;
		for (TreeItem rightItem : rightItems) {
			IMergeViewerItem rightData = (IMergeViewerItem)rightItem.getData();
			final Diff rightDiff = rightData.getDiff();
			if (leftDiff == rightDiff) {
				return rightItem;
			} else if (rightData.getAncestor() == leftData.getAncestor()
					&& rightData.getRight() == leftData.getRight()
					&& rightData.getLeft() == leftData.getLeft()) {
				ret = rightItem;
			}
		}
		return ret;
	}

	private int[] getCenterCurvePoints(Point from, Point to) {
		int startx = from.x;
		int starty = from.y;
		int endx = to.x;
		int endy = to.y;
		if (fBasicCenterCurve == null) {
			buildBaseCenterCurve(endx - startx);
		}
		double height = endy - starty;
		height = height / 2;
		int width = endx - startx;
		int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int)(-height * fBasicCenterCurve[i] + height + starty);
		}
		return points;
	}

	private void buildBaseCenterCurve(int w) {
		double width = w;
		fBasicCenterCurve = new double[getCenterWidth()];
		for (int i = 0; i < getCenterWidth(); i++) {
			double r = i / width;
			fBasicCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class ExpandCollapseListener implements Listener {
		/**
		 * 
		 */
		private final TreeMergeViewer mergeTreeViewer;

		private boolean expanded;

		/**
		 * @param mergeTreeViewer
		 */
		private ExpandCollapseListener(TreeMergeViewer mergeTreeViewer, boolean expanded) {
			this.mergeTreeViewer = mergeTreeViewer;
			this.expanded = expanded;
		}

		public void handleEvent(Event e) {
			Object data = e.item.getData();

			final List<Object> toBeExpanded = newArrayList();
			toBeExpanded.add(data);

			final Object parent;
			if (getLeftMergeViewer() == mergeTreeViewer) {
				parent = ((IMergeViewerItem)data).getLeft();
			} else if (getRightMergeViewer() == mergeTreeViewer) {
				parent = ((IMergeViewerItem)data).getRight();
			} else {
				parent = ((IMergeViewerItem)data).getAncestor();
			}

			if (parent instanceof EObject) {
				Comparison comparison = getCompareConfiguration().getComparison();
				Match match = comparison.getMatch((EObject)parent);
				if (match != null) {
					for (Diff referenceChange : filter(match.getDifferences(), and(
							instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE)))) {
						if (getLeftMergeViewer() == mergeTreeViewer) {
							Match matchOfValue = comparison.getMatch(((ReferenceChange)referenceChange)
									.getValue());
							if (matchOfValue != null) {
								EObject right = matchOfValue.getRight();
								EObject eContainer; // XXX: use itemProvider.getParent().
								if (right != null) {
									eContainer = right.eContainer();
								} else {
									EObject ancestor = matchOfValue.getOrigin();
									eContainer = ancestor.eContainer();
								}
								Match match2 = comparison.getMatch(eContainer);
								if (match2.getLeft() != parent) {
									IMergeViewerItem.Container container = new MergeViewerItem.Container(
											getCompareConfiguration().getComparison(), null, match2,
											MergeViewerSide.RIGHT, fAdapterFactory);
									toBeExpanded.add(container);
								}
							}
						} else if (getRightMergeViewer() == mergeTreeViewer) {
							Match matchOfValue = comparison.getMatch(((ReferenceChange)referenceChange)
									.getValue());
							if (matchOfValue != null) {
								EObject left = matchOfValue.getLeft();
								EObject eContainer; // XXX: use itemProvider.getParent().
								if (left != null) {
									eContainer = left.eContainer();
								} else {
									EObject ancestor = matchOfValue.getOrigin();
									eContainer = ancestor.eContainer();
								}
								Match match2 = comparison.getMatch(eContainer);
								if (match2.getRight() != parent) {
									IMergeViewerItem.Container container = new MergeViewerItem.Container(
											getCompareConfiguration().getComparison(), null, match2,
											MergeViewerSide.LEFT, fAdapterFactory);
									toBeExpanded.add(container);
								}
							}
						}
					}
				}
			}

			try {
				if (fSyncExpandedState.compareAndSet(false, true)) {
					for (Object object : toBeExpanded) {
						getLeftMergeViewer().setExpandedState(object, expanded);
						getRightMergeViewer().setExpandedState(object, expanded);
						getAncestorMergeViewer().setExpandedState(object, expanded);
					}
				}
			} finally {
				getCenterControl().redraw();
				fSyncExpandedState.set(false);
			}
		}
	}

	private class ModelCommentPopupDialog extends PopupDialog {

		private FormToolkit toolkit;

		private Composite composite;

		private ScrolledComposite scrolledComposite;

		private String description;

		private String header;

		public ModelCommentPopupDialog(Shell parent, int shellStyle, String header, String description) {
			super(parent, shellStyle, false, false, false, false, false, null, null);
			this.header = header;
			this.description = description;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			toolkit = new FormToolkit(getShell().getDisplay());

			scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			toolkit.adapt(scrolledComposite);

			composite = toolkit.createComposite(scrolledComposite, SWT.NONE);
			composite.setLayout(new GridLayout());
			scrolledComposite.setContent(composite);

			Label headerLabel = toolkit.createLabel(composite, header);
			headerLabel.setFont(JFaceResources.getFontRegistry().getBold("")); //$NON-NLS-1$
			toolkit.createLabel(composite, description);
			return scrolledComposite;
		}

		public void setLocation(Point location) {
			Rectangle bounds = getShell().getBounds();
			Rectangle monitorBounds = getShell().getMonitor().getClientArea();
			// ensure the popup fits on the shell's monitor
			bounds.x = contrain(location.x, monitorBounds.x, monitorBounds.x + monitorBounds.width
					- bounds.width);
			bounds.y = contrain(location.y, monitorBounds.y, monitorBounds.y + monitorBounds.height
					- bounds.height);

			getShell().setLocation(new Point(bounds.x, bounds.y));
		}

		private int contrain(int value, int min, int max) {
			return Math.max(min, Math.min(max, value));
		}

	}

}
