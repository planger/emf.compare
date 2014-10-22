/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * In the case of stereotype application changes, we will display to the user the whole list of stereotypes
 * applied to the element instead of just the changed one. This will allow us to display which of the
 * stereotypes of this list have changed, much like we display the list of all referenced objects on a
 * reference change.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UMLStereotypeApplicationChangeFeatureAccessor extends ManyStructuralFeatureAccessorImpl {

	/** Fake {@link EReference} for stereotype application change. */
	private static final EReference STEREOTYPE_APPLICATION = EcoreFactory.eINSTANCE.createEReference();

	static {
		STEREOTYPE_APPLICATION.setName("stereotypeApplications"); //$NON-NLS-1$
		STEREOTYPE_APPLICATION.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		STEREOTYPE_APPLICATION.setEType(UMLPackage.Literals.STEREOTYPE);
	}

	/**
	 * Creates a specialized accessor for the stereotype application differences.
	 * 
	 * @param adapterFactory
	 *            The {@link AdapterFactory} used to create the accessor.
	 * @param diff
	 *            The diff for which we need an accessor.
	 * @param side
	 *            The side on which this accessor will be used.
	 */
	public UMLStereotypeApplicationChangeFeatureAccessor(AdapterFactory adapterFactory,
			StereotypeApplicationChange diff, MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * Returns the list of applied stereotypes for the underlying difference's target on the given side.
	 * 
	 * @param side
	 *            The side for which we need the list of applied steretoypes.
	 * @return The list of stereotypes to display for this side.
	 */
	@Override
	protected List<Stereotype> getFeatureValues(MergeViewerSide side) {
		final EObject eObject = getEObject(side);
		if (eObject instanceof Element) {
			return ((Element)eObject).getAppliedStereotypes();
		}
		return ImmutableList.of();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getStructuralFeature()
	 */
	@Override
	public EStructuralFeature getStructuralFeature() {
		return STEREOTYPE_APPLICATION;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#getValueFromDiff(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected Object getValueFromDiff(Diff diff, MergeViewerSide side) {
		// super should give us a stereotype application. If not, we have a problem :).
		final Object superValue = super.getValueFromDiff(diff, side);
		if (superValue instanceof EObject) {
			return UMLUtil.getStereotype((EObject)superValue);
		} else {
			// superValue is "null"... or we have yet another problem.
			// return it anyway.
		}
		return superValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getAffectedFeature(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		if (diff instanceof UMLDiff) {
			return ((UMLDiff)diff).getEReference();
		}
		return super.getAffectedFeature(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#getDiffValue(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Object getDiffValue(Diff diff) {
		if (diff instanceof UMLDiff) {
			return ((UMLDiff)diff).getDiscriminant();
		}
		return super.getDiffValue(diff);
	}

	@Override
	protected ImmutableList<Diff> computeDifferences() {
		List<Diff> siblingDifferences = getInitialDiff().getMatch().getDifferences();
		// We'll display all diffs of the same type, excluding the pseudo conflicts.
		Predicate<? super Diff> diffFilter = not(hasConflict(ConflictKind.PSEUDO));

		return ImmutableList.copyOf(filter(filter(siblingDifferences, diffFilter), getInitialDiff()
				.getClass()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLManyStructuralFeatureAccessor#findInsertionIndex(org.eclipse.emf.compare.Diff,
	 *      boolean)
	 */
	@Override
	protected int findInsertionIndex(Diff diff, boolean rightToLeft) {
		// We can't use the "default" implementation of DiffUtil : we have no actual reference for the list of
		// applied stereotypes.
		final StereotypeApplicationChange stereotypeChange = (StereotypeApplicationChange)diff;
		final Match match = diff.getMatch();
		final Comparison comparison = match.getComparison();

		final List<Stereotype> sourceList;
		if (match.getOrigin() != null && diff.getKind() == DifferenceKind.DELETE) {
			sourceList = getFeatureValues(MergeViewerSide.ANCESTOR);
		} else if (rightToLeft) {
			sourceList = getFeatureValues(MergeViewerSide.RIGHT);
		} else {
			sourceList = getFeatureValues(MergeViewerSide.LEFT);
		}

		final List<Stereotype> targetList;
		if (rightToLeft) {
			targetList = getFeatureValues(MergeViewerSide.LEFT);
		} else {
			targetList = getFeatureValues(MergeViewerSide.RIGHT);
		}

		final Iterable<Stereotype> ignoredElements;
		if (comparison.isThreeWay() && diff.getKind() == DifferenceKind.DELETE) {
			ignoredElements = computeIgnoredElements(targetList, stereotypeChange);
		} else {
			ignoredElements = Lists.newArrayList();
		}

		final Stereotype stereotype = UMLUtil.getStereotype(stereotypeChange.getDiscriminant());

		return DiffUtil.findInsertionIndex(comparison, ignoredElements, sourceList, targetList, stereotype);
	}

	/**
	 * When computing the insertion index of a stereotype in the list of applied stereotypes, we need to
	 * ignore all stereotypes that feature unresolved Diffs.
	 * 
	 * @param candidates
	 *            The sequence in which we need to compute an insertion index.
	 * @param diff
	 *            The diff we are computing an insertion index for.
	 * @param <E>
	 *            Type of the list's content.
	 * @return The list of elements that should be ignored when computing the insertion index for a new
	 *         element in {@code candidates}.
	 */
	private static <E> Iterable<E> computeIgnoredElements(Iterable<E> candidates,
			final StereotypeApplicationChange diff) {
		return Iterables.filter(candidates, new Predicate<Object>() {
			public boolean apply(final Object element) {
				if (element instanceof EObject) {
					final Comparison comparison = diff.getMatch().getComparison();
					final Match match = comparison.getMatch((EObject)element);

					return Iterables.any(match.getDifferences(), and(
							instanceOf(StereotypeApplicationChange.class),
							hasState(DifferenceState.UNRESOLVED)));
				}
				return false;
			}
		});
	}
}
