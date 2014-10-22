/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import com.google.common.collect.Iterators;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Item provider for node changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class NodeChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            The origin item provider adapter.
	 */
	public NodeChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.spec.ForwardingDiagramDiffItemProvider#getReferenceText(org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff)
	 */
	@Override
	protected String getReferenceText(DiagramDiff diagramDiff) {
		String result = ""; //$NON-NLS-1$
		Diff diff = null;
		DifferenceKind kind = diagramDiff.getKind();
		switch (kind) {
			case ADD:
			case DELETE:
				diff = Iterators.find(diagramDiff.getRefinedBy().iterator(), NodeChangeFactory
						.isMainDiffForAddOrDeleteNode(), null);
				break;
			case MOVE:
				diff = Iterators.find(diagramDiff.getRefinedBy().iterator(), NodeChangeFactory
						.isMainDiffForMoveNode(), null);
				break;
			default:
		}
		if (diff instanceof ReferenceChange) {
			result = ((ReferenceChange)diff).getReference().getName();
		}
		return result;

	}
}
