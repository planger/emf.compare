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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider;

import static com.google.common.collect.Iterators.any;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.transform;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.provider.TreeNodeItemProvider;

/**
 * A specific implementation of {@link TreeNodeItemProvider}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public class TreeNodeItemProviderSpec extends TreeNodeItemProvider implements IItemStyledLabelProvider, IItemColorProvider, IItemFontProvider {

	/** A map of IDifferenceGroupProvider, GroupItemProviderAdapter. */
	private Multimap<IDifferenceGroupProvider, GroupItemProviderAdapter> groupItemProviderAdapters;

	/**
	 * This constructs an instance from a factory.
	 * 
	 * @param adapterFactory
	 *            the given factory
	 */
	public TreeNodeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.tree.provider.TreeNodeItemProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object object) {
		Object parent = null;
		TreeNode treeNode = (TreeNode)object;
		TreeNode superParent = (TreeNode)super.getParent(object);
		if (superParent == null) {
			GroupItemProviderAdapter groupItemProvider = (GroupItemProviderAdapter)EcoreUtil
					.getExistingAdapter(treeNode, GroupItemProviderAdapter.class);
			return groupItemProvider;
		} else if (object instanceof GroupItemProviderAdapter) {
			parent = ((GroupItemProviderAdapter)object).getParent(null);
		} else {
			parent = superParent;
		}
		return parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		TreeNode treeNode = (TreeNode)object;
		EObject data = treeNode.getData();
		if (data instanceof Comparison) {
			IDifferenceGroupProvider groupProvider = (IDifferenceGroupProvider)EcoreUtil.getExistingAdapter(
					treeNode, IDifferenceGroupProvider.class);
			Comparison comparison = (Comparison)data;
			Collection<? extends IDifferenceGroup> groups = groupProvider.getGroups(comparison);
			if (groups.isEmpty()) {
				return ImmutableList.of();
			} else if (groups.size() == 1) {
				// do not display group if there is only one.
				return groups.iterator().next().getChildren();
			} else {
				if (groupItemProviderAdapters == null) {
					groupItemProviderAdapters = ArrayListMultimap.create();
					initMapping(groups, groupProvider, treeNode);
					return groupItemProviderAdapters.get(groupProvider);
				} else {
					Collection<GroupItemProviderAdapter> adapters = groupItemProviderAdapters
							.get(groupProvider);
					if (adapters.isEmpty()) {
						initMapping(groups, groupProvider, treeNode);
					}
					return adapters;
				}
			}
		} else {
			return super.getChildren(object);
		}
	}

	/**
	 * Init the mapping.
	 * 
	 * @param groups
	 *            the list of IDifferenceGroup to map with {@link GroupItemProviderAdapter}s.
	 * @param groupProvider
	 *            the IDifferenceGroupProvider used to create a {@link GroupItemProviderAdapter}.
	 * @param treeNode
	 *            the TreeNode used to create a {@link GroupItemProviderAdapter}.
	 */
	protected void initMapping(Collection<? extends IDifferenceGroup> groups,
			IDifferenceGroupProvider groupProvider, TreeNode treeNode) {
		for (IDifferenceGroup differenceGroup : groups) {
			groupItemProviderAdapters.put(groupProvider, new GroupItemProviderAdapter(adapterFactory,
					treeNode, differenceGroup));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IComposedStyledString getStyledText(Object object) {
		TreeNode treeNode = (TreeNode)object;
		EObject data = treeNode.getData();
		ComposedStyledString styledString = new ComposedStyledString();
		if (data instanceof Match) {
			Iterator<EObject> eAllContents = transform(treeNode.eAllContents(),
					IDifferenceGroup.TREE_NODE_DATA);
			Iterator<Diff> allDifferences = filter(eAllContents, Diff.class);
			if (any(allDifferences, hasState(DifferenceState.UNRESOLVED))) {
				styledString.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
			}
		}
		IComposedStyledString treeNodeText = ((ExtendedAdapterFactoryItemDelegator)itemDelegator)
				.getStyledText(treeNode.getData());
		return styledString.append(treeNodeText);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getBackground(java.lang.Object)
	 */
	@Override
	public Object getBackground(Object object) {
		TreeNode treeNode = (TreeNode)object;
		return itemDelegator.getBackground(treeNode.getData());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		TreeNode treeNode = (TreeNode)object;
		return itemDelegator.getForeground(treeNode.getData());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getBackground(java.lang.Object, int)
	 */
	@Override
	public Object getBackground(Object object, int columnIndex) {
		TreeNode treeNode = (TreeNode)object;
		return itemDelegator.getBackground(treeNode.getData(), columnIndex);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object, int)
	 */
	@Override
	public Object getForeground(Object object, int columnIndex) {
		TreeNode treeNode = (TreeNode)object;
		return itemDelegator.getForeground(treeNode.getData(), columnIndex);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getFont(java.lang.Object)
	 */
	@Override
	public Object getFont(Object object) {
		TreeNode treeNode = (TreeNode)object;
		return itemDelegator.getFont(treeNode.getData());
	}

	@Override
	public void dispose() {
		super.dispose();
		if (groupItemProviderAdapters != null) {
			groupItemProviderAdapters.clear();
		}
	}
}
