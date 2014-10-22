/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.util;

import static com.google.common.collect.Sets.newHashSet;

import com.google.common.base.Predicate;

import java.util.Set;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class JFaceUtil {

	/**
	 * All element filter tests must go through this method.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param object
	 *            the object to filter
	 * @param parent
	 *            the parent
	 * @return true if the element is filtered
	 */
	public static boolean isFiltered(StructuredViewer viewer, Object object, Object parent) {
		for (ViewerFilter filter : viewer.getFilters()) {
			if (!filter.select(viewer, parent, object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the set of visible elements from the given viewer filtered by the given predicate.
	 * 
	 * @param treeViewer
	 * @param predicate
	 * @return
	 */
	public static Set<?> filterVisibleElement(TreeViewer treeViewer, Predicate<? super Object> predicate) {
		Set<Object> acc = newHashSet();
		appendNonFilteredChildren(treeViewer, treeViewer.getTree().getItems(), null, predicate, acc);
		return acc;
	}

	/**
	 * @param cp
	 * @param children
	 * @param diffs
	 * @return
	 */
	private static void appendNonFilteredChildren(TreeViewer treeViewer, TreeItem[] elements, Object parent,
			Predicate<? super Object> predicate, Set<Object> acc) {
		for (TreeItem element : elements) {
			if (!isFiltered(treeViewer, element.getData(), parent) && predicate.apply(element.getData())) {
				acc.add(element.getData());
			}
			TreeItem[] children = element.getItems();
			appendNonFilteredChildren(treeViewer, children, element, predicate, acc);
		}
	}
}
