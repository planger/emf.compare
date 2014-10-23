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

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DependencyData {

	private final IEMFCompareConfiguration compareConfiguration;

	private Set<Diff> requires;

	private Set<Diff> rejectedDiffs;

	private final WrappableTreeViewer treeViewer;

	/** A map that links a diff with tree items. */
	private Multimap<Diff, TreeItem> diffToItemsMappings;

	public DependencyData(IEMFCompareConfiguration compareConfiguration, WrappableTreeViewer treeViewer) {
		this.compareConfiguration = compareConfiguration;
		this.treeViewer = treeViewer;
		requires = newHashSet();
		rejectedDiffs = newHashSet();
		diffToItemsMappings = HashMultimap.create();
	}

	/**
	 * @param selection
	 */
	public void updateDependencies(ISelection selection, IMerger.Registry mergerRegistry) {
		boolean leftEditable = compareConfiguration.isLeftEditable();
		boolean rightEditable = compareConfiguration.isRightEditable();
		if (leftEditable || rightEditable) {
			Iterable<Diff> selectedDiffs = filter(getSelectedComparisonObjects(selection), Diff.class);

			MergeMode mergePreviewMode = compareConfiguration.getMergePreviewMode();

			requires = newHashSet();
			rejectedDiffs = newHashSet();
			for (Diff diff : selectedDiffs) {
				boolean leftToRight = mergePreviewMode.isLeftToRight(diff, leftEditable, rightEditable);
				requires.addAll(MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
						!leftToRight));
				requires.remove(diff);
				rejectedDiffs.addAll(MergeDependenciesUtil.getAllResultingRejections(diff, mergerRegistry,
						!leftToRight));
				rejectedDiffs.remove(diff);
			}
		}
	}

	public void updateTreeItemMappings() {
		diffToItemsMappings = HashMultimap.create();

		Tree tree = treeViewer.getTree();

		TreeItem[] children = tree.getItems();
		// item with non created children has a fake child item with null data.
		if (children.length == 1 && children[0].getData() == null) {
			treeViewer.createChildren(tree);
		}

		for (TreeItem item : tree.getItems()) {
			associateTreeItem(item);
		}
	}

	/**
	 * Maps, if necessary, the given tree item and all his children with the given list of diffs.
	 * 
	 * @param item
	 *            the given tree item.
	 * @param diffs
	 *            the given list of diffs.
	 */
	private void associateTreeItem(TreeItem item) {
		Object itemData = item.getData();
		EObject eObject = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(itemData);

		if (eObject instanceof Diff) {
			diffToItemsMappings.put((Diff)eObject, item);
		}

		TreeItem[] children = item.getItems();
		// item with non created children has a fake child item with null data.
		if (children.length > 0 && children[0].getData() == null) {
			treeViewer.createChildren(item);
		}

		for (TreeItem child : item.getItems()) {
			associateTreeItem(child);
		}
	}

	private static List<EObject> getSelectedComparisonObjects(ISelection selection) {
		List<EObject> ret = newArrayList();
		if (selection instanceof IStructuredSelection) {
			List<?> selectedObjects = ((IStructuredSelection)selection).toList();
			Iterable<EObject> data = transform(selectedObjects, ADAPTER__TARGET__DATA);
			Iterable<EObject> notNullData = Iterables.filter(data, notNull());
			addAll(ret, notNullData);
		}
		return ret;
	}

	private static final Function<Object, EObject> ADAPTER__TARGET__DATA = new Function<Object, EObject>() {
		public EObject apply(Object object) {
			return EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(object);
		}
	};

	/**
	 * @return the requires
	 */
	public Set<Diff> getRequires() {
		return requires;
	}

	/**
	 * @return the unmergeables
	 */
	public Set<Diff> getRejections() {
		return rejectedDiffs;
	}

	public Collection<TreeItem> getTreeItems(Diff diff) {
		return diffToItemsMappings.get(diff);
	}
}
