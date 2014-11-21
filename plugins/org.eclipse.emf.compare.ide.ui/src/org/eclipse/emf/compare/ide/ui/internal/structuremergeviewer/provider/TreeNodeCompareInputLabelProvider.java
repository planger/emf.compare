/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.internal.utils.StoragePathAdapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * Provides labels for the content viewer pane.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class TreeNodeCompareInputLabelProvider implements ICompareInputLabelProvider {
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public String getAncestorLabel(Object input) {
		final Match match = getMatch(getTreeNode(input));
		if (match != null) {
			if (match.getOrigin() != null) {
				final EObject origin = match.getOrigin();
				final Resource originResource = origin.eResource();
				final StoragePathAdapter adapter = getStoragePathAdapter(originResource);
				if (adapter != null) {
					return computeLabel(adapter);
				}
			} else {
				return computeFallbackLabel(match, Side.ORIGIN);
			}
		}
		return null;
	}

	public Image getAncestorImage(Object input) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLeftLabel(Object input) {
		final Match match = getMatch(getTreeNode(input));
		if (match != null) {
			if (match.getLeft() != null) {
				final EObject left = match.getLeft();
				final Resource leftResource = left.eResource();
				final StoragePathAdapter adapter = getStoragePathAdapter(leftResource);
				if (adapter != null) {
					return computeLabel(adapter);
				}
			} else {
				return computeFallbackLabel(match, Side.LEFT);
			}
		}
		return null;
	}

	public Image getLeftImage(Object input) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRightLabel(Object input) {
		final Match match = getMatch(getTreeNode(input));
		if (match != null) {
			if (match.getRight() != null) {
				final EObject right = match.getRight();
				final Resource rightResource = right.eResource();
				final StoragePathAdapter adapter = getStoragePathAdapter(rightResource);
				if (adapter != null) {
					return computeLabel(adapter);
				}
			} else {
				return computeFallbackLabel(match, Side.RIGHT);
			}
		}
		return null;
	}

	public Image getRightImage(Object input) {
		// TODO Auto-generated method stub
		return null;
	}

	private TreeNode getTreeNode(Object input) {
		if (input instanceof TreeNodeCompareInput
				&& ((TreeNodeCompareInput)input).getTarget() instanceof TreeNode) {
			return (TreeNode)((TreeNodeCompareInput)input).getTarget();
		}
		return null;
	}

	private Match getMatch(EObject object) {
		final Match match;
		if (object instanceof Match) {
			match = (Match)object;
		} else if (object instanceof Diff) {
			match = ((Diff)object).getMatch();
		} else if (object instanceof TreeNode) {
			match = getMatch(((TreeNode)object).getData());
		} else {
			match = null;
		}
		return match;
	}

	private StoragePathAdapter getStoragePathAdapter(Resource resource) {
		for (Adapter adapter : resource.eAdapters()) {
			if (adapter instanceof StoragePathAdapter) {
				return (StoragePathAdapter)adapter;
			}
		}
		return null;
	}

	private String computeLabel(StoragePathAdapter adapter) {
		if (adapter.isLocal()) {
			return "Local: " + adapter.getStoragePath();
		} else {
			return "Remote: " + adapter.getStoragePath();
		}
	}

	private String computeFallbackLabel(Match match, Side side) {
		// The "correct" way to find the fall back label would be to try and find the match resource for the
		// given match. Then, if its value for the given 'side' is not null, use that. Otherwise it is a
		// fragment that only exists for one side; in which case we need to find another MatchResource which
		// value for 'side' isn't null, and use this MatchResource's storage path adapter to determine if it
		// is local or not, then retrieve the given match's MatchResource, find a non-null side on it that has
		// a non-null resource, and use that resource for the "path" part of our label.
		// This gives a code very complex and costly only to solve the rare occurence of an added/deleted
		// fragment.
		// We'll instead try and find the first parent of 'Match' which value for 'side' is not null and use
		// that, even if the 'path' part of the label may be wrong with that algorithm.
		EObject parent = match.eContainer();
		while (parent instanceof Match) {
			final Resource resource = getResourceOnSide((Match)parent, side);
			if (resource != null) {
				final StoragePathAdapter adapter = getStoragePathAdapter(resource);
				return computeLabel(adapter);
			}
			parent = parent.eContainer();
		}
		return null;
	}

	private Resource getResourceOnSide(Match match, Side side) {
		Resource resource = null;
		switch (side) {
			case LEFT:
				if (match.getLeft() != null) {
					resource = match.getLeft().eResource();
				}
				break;
			case RIGHT:
				if (match.getRight() != null) {
					resource = match.getRight().eResource();
				}
				break;
			case ORIGIN:
				if (match.getOrigin() != null) {
					resource = match.getOrigin().eResource();
				}
				break;
			default:
				break;
		}
		return resource;
	}

	private static enum Side {
		LEFT, RIGHT, ORIGIN;
	}
}
