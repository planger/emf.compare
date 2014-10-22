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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.impl.AbstractTypedElementAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem.Container;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * A specific {@link ICompareAccessor} for {@link Match} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public class MatchAccessor extends AbstractTypedElementAdapter implements ICompareAccessor {

	/** The match associated with this accessor. */
	private final Match fMatch;

	/** The side of this accessor. */
	private final MergeViewerSide fSide;

	/**
	 * Creates a new object wrapping the given <code>eObject</code>.
	 * 
	 * @param adapterFactory
	 *            the adapter factory used to create the accessor.
	 * @param match
	 *            the match to associate with this accessor.
	 * @param side
	 *            the side of this accessor.
	 */
	public MatchAccessor(AdapterFactory adapterFactory, Match match, MergeViewerSide side) {
		super(adapterFactory);
		fMatch = match;
		fSide = side;
	}

	/**
	 * Returns the side of this accessor.
	 * 
	 * @return the side of this accessor.
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	public String getName() {
		final EObject eObject = MergeViewerUtil.getBestSideEObject(fMatch, fSide);
		if (eObject != null) {
			return getItemDelegator().getText(eObject);
		} else {
			return getItemDelegator().getText(fMatch);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getImage()
	 */
	public Image getImage() {
		final EObject eObject = MergeViewerUtil.getBestSideEObject(fMatch, fSide);
		final Object image;
		if (eObject != null) {
			image = getItemDelegator().getImage(eObject);
		} else {
			image = getItemDelegator().getImage(fMatch);
		}
		return ExtendedImageRegistry.getInstance().getImage(image);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE_EMATCH;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getComparison()
	 */
	public Comparison getComparison() {
		return fMatch.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		return new MergeViewerItem.Container(fMatch.getComparison(), null, fMatch, fSide,
				getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		final ImmutableList.Builder<IMergeViewerItem> ret = ImmutableList.builder();

		EList<Match> matches = getComparison().getMatches();
		for (Match match : matches) {
			ResourceAttachmentChange diff = getFirst(filter(match.getDifferences(),
					ResourceAttachmentChange.class), null);
			Container container = null;
			if (getSide() != MergeViewerSide.ANCESTOR
					|| (getSide() == MergeViewerSide.ANCESTOR && match.getOrigin() != null)) {
				container = new MergeViewerItem.Container(getComparison(), diff, match.getLeft(), match
						.getRight(), match.getOrigin(), getSide(), getRootAdapterFactory());
				ret.add(container);
			}
		}

		return ret.build();
	}
}
