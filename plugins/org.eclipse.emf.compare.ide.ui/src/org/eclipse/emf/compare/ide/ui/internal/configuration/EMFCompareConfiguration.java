/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.configuration;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.ReadOnlyGraph;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.impl.CompareEditingDomainChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.impl.ComparisonAndScopeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.impl.EMFComparatorChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.impl.MergePreviewModeChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareConfiguration extends ForwardingCompareConfiguration implements IEMFCompareConfiguration {

	private static final String COMPARE_RESULT = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARE_RESULT"; //$NON-NLS-1$

	private static final String COMPARATOR = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARATOR"; //$NON-NLS-1$

	private static final String EDITING_DOMAIN = EMFCompareIDEUIPlugin.PLUGIN_ID + ".EDITING_DOMAIN"; //$NON-NLS-1$

	private static final String ADAPTER_FACTORY = EMFCompareIDEUIPlugin.PLUGIN_ID + ".ADAPTER_FACTORY"; //$NON-NLS-1$

	private static final String PREVIEW_MERGE_MODE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".PREVIEW_MERGE_MODE"; //$NON-NLS-1$

	private static final String COMPARISON_SCOPE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARISON_SCOPE"; //$NON-NLS-1$;

	private static final String SMV_FILTERS = EMFCompareIDEUIPlugin.PLUGIN_ID + ".SMV_FILTERS"; //$NON-NLS-1$;

	private static final String EVENT_BUS = EMFCompareIDEUIPlugin.PLUGIN_ID + ".EVENT_BUS"; //$NON-NLS-1$;

	private static final String SMV_GROUP_PROVIDERS = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".SMV_GROUP_PROVIDERS"; //$NON-NLS-1$;

	private static final String RESOURCES_GRAPH = EMFCompareRCPUIPlugin.PLUGIN_ID + ".RESOURCES_GRAPH"; //$NON-NLS-1$;

	private final PropertyChangeListener propertyChangeListener;

	private final CompareConfiguration compareConfiguration;

	public EMFCompareConfiguration(CompareConfiguration compareConfiguration) {
		this.compareConfiguration = compareConfiguration;
		setDefaultValues();
		propertyChangeListener = new PropertyChangeListener();
		compareConfiguration.addPropertyChangeListener(propertyChangeListener);
	}

	private void setDefaultValues() {
		if (getProperty(PREVIEW_MERGE_MODE) == null) {
			if (isLeftEditable() && isRightEditable()) {
				setProperty(PREVIEW_MERGE_MODE, MergeMode.RIGHT_TO_LEFT);
			} else {
				setProperty(PREVIEW_MERGE_MODE, MergeMode.ACCEPT);
			}
		}

		EventBus eventBus = new EventBus();
		if (getProperty(SMV_FILTERS) == null) {
			setProperty(SMV_FILTERS, new StructureMergeViewerFilter(eventBus));
		}

		if (getProperty(SMV_GROUP_PROVIDERS) == null) {
			setProperty(SMV_GROUP_PROVIDERS, new StructureMergeViewerGrouper(eventBus));
		}

		if (getProperty(EVENT_BUS) == null) {
			setProperty(EVENT_BUS, eventBus);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getEventBus()
	 */
	public EventBus getEventBus() {
		return (EventBus)getProperty(EVENT_BUS);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.configuration.ForwardingCompareConfiguration#delegate()
	 */
	@Override
	protected CompareConfiguration delegate() {
		return compareConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		compareConfiguration.removePropertyChangeListener(propertyChangeListener);
		// CompareConfiguration does not clear its properties list...
		// Lets clean our own mess ourselves
		// EVENT_BUS must not be set to null
		compareConfiguration.setProperty(COMPARISON_SCOPE, null);
		compareConfiguration.setProperty(COMPARE_RESULT, null);
		compareConfiguration.setProperty(SMV_FILTERS, null);
		compareConfiguration.setProperty(EDITING_DOMAIN, null);
		compareConfiguration.setProperty(ADAPTER_FACTORY, null);
		compareConfiguration.setProperty(SMV_GROUP_PROVIDERS, null);
		compareConfiguration.setProperty(PREVIEW_MERGE_MODE, null);
		compareConfiguration.setProperty(RESOURCES_GRAPH, null);
	}

	public boolean getBooleanProperty(String key, boolean dflt) {
		final boolean ret;
		Object value = getProperty(key);
		if (value instanceof Boolean) {
			ret = ((Boolean)value).booleanValue();
		} else {
			ret = dflt;
		}
		return ret;
	}

	public Comparison getComparison() {
		return (Comparison)getProperty(COMPARE_RESULT);
	}

	public EMFCompare getEMFComparator() {
		return (EMFCompare)getProperty(COMPARATOR);
	}

	public ICompareEditingDomain getEditingDomain() {
		return (ICompareEditingDomain)getProperty(EDITING_DOMAIN);
	}

	public MergeMode getMergePreviewMode() {
		return (MergeMode)getProperty(PREVIEW_MERGE_MODE);
	}

	public AdapterFactory getAdapterFactory() {
		return (AdapterFactory)getProperty(ADAPTER_FACTORY);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getComparisonScope()
	 */
	public IComparisonScope getComparisonScope() {
		return (IComparisonScope)getProperty(COMPARISON_SCOPE);
	}

	public void setEMFComparator(EMFCompare newComparator) {
		EMFCompare oldComparator = getEMFComparator();
		setProperty(COMPARATOR, newComparator);
		getEventBus().post(new EMFComparatorChange(oldComparator, newComparator));
	}

	public void setEditingDomain(ICompareEditingDomain newValue) {
		ICompareEditingDomain oldValue = getEditingDomain();
		setProperty(EDITING_DOMAIN, newValue);
		getEventBus().post(new CompareEditingDomainChange(oldValue, newValue));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#setComparisonAndScope(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public void setComparisonAndScope(Comparison newComparison, IComparisonScope newComparisonScope) {
		Comparison oldComparison = getComparison();
		IComparisonScope oldComparisonScope = getComparisonScope();
		setProperty(COMPARE_RESULT, newComparison);
		setProperty(COMPARISON_SCOPE, newComparisonScope);

		if (oldComparison == null && oldComparisonScope == null) {
			initStructureMergeViewerGroupProvider(newComparison, newComparisonScope);
			initStructureMergeViewerFilter(newComparison, newComparisonScope);
		}

		getEventBus().post(
				new ComparisonAndScopeChange(oldComparison, newComparison, oldComparisonScope,
						newComparisonScope));
	}

	protected void initStructureMergeViewerGroupProvider(Comparison comparison,
			IComparisonScope comparisonScope) {
		EMFCompareRCPUIPlugin plugin = EMFCompareRCPUIPlugin.getDefault();
		IDifferenceGroupProvider.Descriptor.Registry groupProviderRegistry = plugin
				.getDifferenceGroupProviderRegistry();
		Descriptor defaultGroupProvider = groupProviderRegistry.getDefaultGroupProvider(comparisonScope,
				comparison);
		IDifferenceGroupProvider defaultGroup = null;
		if (defaultGroupProvider != null) {
			defaultGroup = defaultGroupProvider.createGroupProvider();
		}
		if (defaultGroup == null) {
			defaultGroup = new DefaultGroupProvider();
		}
		getStructureMergeViewerGrouper().setProvider(defaultGroup);
	}

	protected void initStructureMergeViewerFilter(Comparison comparison, IComparisonScope comparisonScope) {
		EMFCompareRCPUIPlugin plugin = EMFCompareRCPUIPlugin.getDefault();
		IDifferenceFilter.Registry filterRegistry = plugin.getDifferenceFilterRegistry();
		Collection<IDifferenceFilter> filters = filterRegistry.getFilters(comparisonScope, comparison);
		Collection<IDifferenceFilter> selectedFilters = Lists.newArrayList();
		Collection<IDifferenceFilter> unselectedFilters = Lists.newArrayList();
		for (IDifferenceFilter filter : filters) {
			if (filter.defaultSelected()) {
				selectedFilters.add(filter);
			} else {
				unselectedFilters.add(filter);
			}
		}
		getStructureMergeViewerFilter().init(selectedFilters, unselectedFilters);
	}

	public void setMergePreviewMode(MergeMode previewMergeMode) {
		Preconditions.checkNotNull(previewMergeMode);
		MergeMode oldValue = getMergePreviewMode();
		setProperty(PREVIEW_MERGE_MODE, previewMergeMode);
		getEventBus().post(new MergePreviewModeChange(oldValue, previewMergeMode));
	}

	public void setAdapterFactory(AdapterFactory adapterFactory) {
		setProperty(ADAPTER_FACTORY, adapterFactory);
	}

	private class PropertyChangeListener implements IPropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			fireChange(event.getProperty(), event.getOldValue(), event.getNewValue());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getStructureMergeViewerGrouper()
	 */
	public StructureMergeViewerGrouper getStructureMergeViewerGrouper() {
		return (StructureMergeViewerGrouper)getProperty(SMV_GROUP_PROVIDERS);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getStructureMergeViewerFilter()
	 */
	public StructureMergeViewerFilter getStructureMergeViewerFilter() {
		return (StructureMergeViewerFilter)getProperty(SMV_FILTERS);
	}

	public void setLabelProvider(Class<? extends ICompareInput> inputType,
			ICompareInputLabelProvider labelProvider) {
		labelProviders.put(inputType, labelProvider);
	}

	@Override
	public String getLeftLabel(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final String leftLabel = labelProvider.getLeftLabel(element);
			if (leftLabel != null) {
				return leftLabel;
			}
		}
		return super.getLeftLabel(element);
	}

	@Override
	public Image getLeftImage(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final Image leftImage = labelProvider.getLeftImage(element);
			if (leftImage != null) {
				return leftImage;
			}
		}
		return super.getLeftImage(element);
	}

	@Override
	public String getRightLabel(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final String rightLabel = labelProvider.getRightLabel(element);
			if (rightLabel != null) {
				return rightLabel;
			}
		}
		return super.getRightLabel(element);
	}

	@Override
	public Image getRightImage(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final Image rightImage = labelProvider.getRightImage(element);
			if (rightImage != null) {
				return rightImage;
			}
		}
		return super.getRightImage(element);
	}

	@Override
	public String getAncestorLabel(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final String ancestorLabel = labelProvider.getAncestorLabel(element);
			if (ancestorLabel != null) {
				return ancestorLabel;
			}
		}
		return super.getAncestorLabel(element);
	}

	@Override
	public Image getAncestorImage(Object element) {
		final ICompareInputLabelProvider labelProvider = getLabelProviderForType(element);
		if (labelProvider != null) {
			final Image ancestorImage = labelProvider.getAncestorImage(element);
			if (ancestorImage != null) {
				return ancestorImage;
			}
		}
		return super.getLeftImage(element);
	}

	private ICompareInputLabelProvider getLabelProviderForType(Object element) {
		if (element instanceof ICompareInput) {
			return labelProviders.get(((ICompareInput)element).getClass());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ReadOnlyGraph<URI> getResourcesGraph() {
		return (ReadOnlyGraph<URI>)getProperty(RESOURCES_GRAPH);
	}

	@Override
	public void setResourcesGraph(ReadOnlyGraph<URI> graph) {
		setProperty(RESOURCES_GRAPH, graph);
	}

}
