/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.merge;

import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.getOpaqueElementLanguages;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.isChangeOfOpaqueElementBodyAttribute;
import static org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil.isChangeOfOpaqueElementLanguageAttribute;

import com.google.common.base.Optional;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.AttributeChangeMerger;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for {@link OpaqueElementBodyChange body changes} of OpaqueActions, OpaqueBehaviors and
 * OpaqueExpressions.
 * <p>
 * This merger handles all {@link Diff differences} that are either {@link OpaqueElementBodyChange opaque
 * element body changes} themselves or that are {@link AttributeChange attribute changes} refining opaque
 * element body changes. Note that this merger forces the merging of the entire
 * {@link OpaqueElementBodyChange}, also if it is invoked only for one {@link AttributeChange} that refines a
 * {@link OpaqueElementBodyChange}.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangeMerger extends AttributeChangeMerger {

	@Override
	public boolean isMergerFor(Diff target) {
		return isOrRefinesOpaqueElementBodyChange(target);
	}

	/**
	 * Specifies whether the given {@code target} either is an {@link OpaqueElementBodyChange} or refines an
	 * {@link OpaqueElementBodyChange}.
	 * 
	 * @param target
	 *            The difference to check.
	 * @return <code>true</code> if it is or refines an {@link OpaqueElementBodyChange}, <code>false</code>
	 *         otherwise.
	 */
	private boolean isOrRefinesOpaqueElementBodyChange(Diff target) {
		return getOpaqueElementBodyChange(target).isPresent();
	}

	/**
	 * Returns an the {@code Optional optional} {@link OpaqueElementBodyChange} for the given {@code diff}.
	 * <p>
	 * This is either the {@code diff} itself or the difference that is refined by the given {@code diff}. If
	 * neither {@code diff} itself nor the differences it refines is an {@link OpaqueElementBodyChange}, this
	 * method returns {@code Optional#absent()}.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to get the {@link OpaqueElementBodyChange} for.
	 * @return The {@link OpaqueElementBodyChange} or {@code Optional#absent()} if not available.
	 */
	private Optional<OpaqueElementBodyChange> getOpaqueElementBodyChange(Diff diff) {
		final Optional<OpaqueElementBodyChange> bodyChange;
		if (diff instanceof OpaqueElementBodyChange) {
			bodyChange = Optional.of((OpaqueElementBodyChange)diff);
		} else if (!diff.getRefines().isEmpty()) {
			bodyChange = getRefinedOpaqueElementBodyChange(diff);
		} else {
			bodyChange = Optional.absent();
		}
		return bodyChange;
	}

	/**
	 * Returns the {@link Optional optional} {@link OpaqueElementBodyChange} that is refined by the given
	 * {@code diff} if available.
	 * 
	 * @param diff
	 *            The difference to get the refined {@link OpaqueElementBodyChange} for.
	 * @return The refined {@link OpaqueElementBodyChange} or {@code Optional#absent()} if not available.
	 */
	private Optional<OpaqueElementBodyChange> getRefinedOpaqueElementBodyChange(Diff diff) {
		for (Diff refinedDiff : diff.getRefines()) {
			if (refinedDiff instanceof OpaqueElementBodyChange) {
				return Optional.of((OpaqueElementBodyChange)refinedDiff);
			}
		}
		return Optional.absent();
	}

	@Override
	protected void accept(Diff diff, boolean rightToLeft) {
		final Optional<OpaqueElementBodyChange> possibleBodyChange = getOpaqueElementBodyChange(diff);
		if (possibleBodyChange.isPresent()) {
			final OpaqueElementBodyChange bodyChange = getOpaqueElementBodyChange(diff).get();
			switch (bodyChange.getKind()) {
				case ADD:
					acceptRefiningDiffs(bodyChange, rightToLeft);
					break;
				case DELETE:
					acceptRefiningDiffs(bodyChange, rightToLeft);
					break;
				case CHANGE:
					changeElement(bodyChange, rightToLeft);
					break;
				case MOVE:
					moveElement(bodyChange, rightToLeft);
					break;
				default:
					break;

			}
		}
	}

	@Override
	protected void reject(Diff diff, boolean rightToLeft) {
		final Optional<OpaqueElementBodyChange> possibleBodyChange = getOpaqueElementBodyChange(diff);
		if (possibleBodyChange.isPresent()) {
			final OpaqueElementBodyChange bodyChange = possibleBodyChange.get();
			switch (bodyChange.getKind()) {
				case ADD:
					rejectRefiningDiffs(bodyChange, rightToLeft);
					break;
				case DELETE:
					rejectRefiningDiffs(bodyChange, rightToLeft);
					break;
				case CHANGE:
					changeElement(bodyChange, rightToLeft);
					break;
				case MOVE:
					moveElement(bodyChange, rightToLeft);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Delegates the accept of the refining differences of the given {@code bodyChange} to the super class (
	 * {@link AttributeChangeMerger}).
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to be delegated.
	 * @param rightToLeft
	 *            The direction of merging.
	 */
	private void acceptRefiningDiffs(OpaqueElementBodyChange bodyChange, boolean rightToLeft) {
		bodyChange.setState(DifferenceState.MERGED);
		final List<Diff> sortedRefiningDiffs = sortByMergePriority(bodyChange.getRefinedBy());
		for (Diff refiningDiff : sortedRefiningDiffs) {
			super.accept(refiningDiff, rightToLeft);
		}
	}

	/**
	 * Delegates the reject of the refining differences of the given {@code bodyChange} to the super class (
	 * {@link AttributeChangeMerger}).
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to be delegated.
	 * @param rightToLeft
	 *            The direction of merging.
	 */
	private void rejectRefiningDiffs(OpaqueElementBodyChange bodyChange, boolean rightToLeft) {
		bodyChange.setState(DifferenceState.MERGED);
		final List<Diff> sortedRefiningDiffs = sortByMergePriority(bodyChange.getRefinedBy());
		for (Diff refiningDiff : sortedRefiningDiffs) {
			super.reject(refiningDiff, rightToLeft);
		}
	}

	/**
	 * Creates a new list of the given {@code refiningDiffs} sorted by priority of merging.
	 * <p>
	 * The priority of merging is first merge the potentially existing language attribute value differences,
	 * then merge the body attribute value differences. This is important in order to maintain the correct
	 * order of both. We first merge the language attribute value, because #findInsertionIndex(Comparison,
	 * Diff, boolean) works better for language attribute values than for bodies.
	 * </p>
	 * 
	 * @param refiningDiffs
	 *            The list of refining differences.
	 * @return The sorted list of refining differnces.
	 */
	private List<Diff> sortByMergePriority(List<Diff> refiningDiffs) {
		final LinkedList<Diff> sortedRefiningDiffs = new LinkedList<Diff>(refiningDiffs);
		Collections.sort(sortedRefiningDiffs, new Comparator<Diff>() {
			/*
			 * Note: this comparator imposes orderings that are inconsistent with equals. We only want to
			 * ensure that language attribute values are sorted in before body attribute values.
			 */
			public int compare(Diff diff1, Diff diff2) {
				final int compare;
				if (isChangeOfOpaqueElementLanguageAttribute(diff1)
						&& !isChangeOfOpaqueElementLanguageAttribute(diff2)) {
					compare = -1;
				} else if (!isChangeOfOpaqueElementLanguageAttribute(diff1)
						&& isChangeOfOpaqueElementLanguageAttribute(diff2)) {
					compare = 1;
				} else {
					compare = 0;
				}
				return compare;
			}
		});
		return sortedRefiningDiffs;
	}

	/**
	 * Performs the change represented by the given {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to be performed.
	 * @param rightToLeft
	 *            The direction of merging.
	 */
	private void changeElement(OpaqueElementBodyChange bodyChange, boolean rightToLeft) {
		final Match match = bodyChange.getMatch();
		final Comparison comparison = match.getComparison();
		final String language = bodyChange.getLanguage();

		final EObject leftContainer = match.getLeft();
		final EObject rightContainer = match.getRight();

		final String leftBody = UMLCompareUtil.getOpaqueElementBody(leftContainer, language);
		final String rightBody = UMLCompareUtil.getOpaqueElementBody(rightContainer, language);

		final String newBody;
		if (comparison.isThreeWay()) {
			final EObject originContainer = match.getOrigin();
			final String originBody = UMLCompareUtil.getOpaqueElementBody(originContainer, language);
			if (isAcceptingChange(bodyChange, rightToLeft)) {
				newBody = performThreeWayTextMerge(leftBody, rightBody, originBody);
			} else {
				newBody = originBody;
			}
		} else if (rightToLeft) {
			newBody = rightBody;
		} else {
			newBody = leftBody;
		}

		if (rightToLeft) {
			setBody(leftContainer, newBody, language);
		} else {
			setBody(rightContainer, newBody, language);
		}

		// we merged the body change as a whole, so set all refining to merged too
		setRefiningDiffsMerged(bodyChange);
	}

	/**
	 * Specifies whether the given {@code diff} is an accept in the context of the given direction of merging
	 * specified in {@code rightToLeft}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @param rightToLeft
	 *            The direction of the merging.
	 * @return <code>true</code> if it is an accept, <code>false</code> otherwise.
	 */
	private boolean isAcceptingChange(Diff diff, boolean rightToLeft) {
		return (diff.getSource() == DifferenceSource.LEFT && !rightToLeft)
				|| (diff.getSource() == DifferenceSource.RIGHT && rightToLeft);
	}

	/**
	 * Sets {@code newBody} as the contents of the body for the given {@code language} in the given
	 * {@code container}.
	 * 
	 * @param container
	 *            The {@link EObject} to set the body.
	 * @param newBody
	 *            The content of the body to set.
	 * @param language
	 *            The language at which the body shall be set.
	 */
	private void setBody(EObject container, String newBody, String language) {
		final List<String> languages = UMLCompareUtil.getOpaqueElementLanguages(container);
		final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(container);
		final int index = languages.indexOf(language);
		bodies.set(index, newBody);
	}

	/**
	 * Performs the move represented by the given {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to be performed.
	 * @param rightToLeft
	 *            The direction of merging.
	 */
	private void moveElement(OpaqueElementBodyChange bodyChange, boolean rightToLeft) {
		final Match match = bodyChange.getMatch();
		final Comparison comparison = match.getComparison();
		final String language = bodyChange.getLanguage();

		final Diff languageAttributeMove = getLanguageAttributeMove(bodyChange).get();
		final EObject container = getTargetContainer(match, rightToLeft);
		final int sourceIndex = getLanguageIndex(container, language);
		final int targetIndex = DiffUtil.findInsertionIndex(comparison, languageAttributeMove, rightToLeft);

		doMove(container, sourceIndex, targetIndex);

		// we merged the body change as a whole, so set all refining to merged too
		setRefiningDiffsMerged(bodyChange);
	}

	/**
	 * Returns the {@link Optional optional} refining {@link AttributeChange} representing the move of the
	 * language attribute value in the given {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to get the move difference from.
	 * @return The {@link AttributeChange} representing the move of the language attribute value.
	 */
	private Optional<Diff> getLanguageAttributeMove(OpaqueElementBodyChange bodyChange) {
		for (Diff diff : bodyChange.getRefinedBy()) {
			if (diff instanceof AttributeChange && DifferenceKind.MOVE.equals(diff.getKind())) {
				return Optional.of(diff);
			}
		}
		return Optional.absent();
	}

	/**
	 * Returns the target container of the given {@code match} in the context of the direction of the merging
	 * specified in {@code rightToLeft}.
	 * 
	 * @param match
	 *            The {@link Match} to get the target container from.
	 * @param rightToLeft
	 *            The direction of merging.
	 * @return The target container, that is, the object to be changed.
	 */
	private EObject getTargetContainer(final Match match, boolean rightToLeft) {
		if (rightToLeft) {
			return match.getLeft();
		} else {
			return match.getRight();
		}
	}

	/**
	 * Returns the index of the given {@code language} in the language values of the given {@code container}.
	 * 
	 * @param container
	 *            The container to get the index of the language value for.
	 * @param language
	 *            The language value.
	 * @return The index of {@code language} in the list of languages in {@code container}.
	 */
	private int getLanguageIndex(EObject container, String language) {
		return UMLCompareUtil.getOpaqueElementLanguages(container).indexOf(language);
	}

	/**
	 * Performs the move of the language and body value from the current {@code sourceIndex} to the given
	 * {@code targetIndex}.
	 * 
	 * @param container
	 *            The container to perform the move in.
	 * @param sourceIndex
	 *            The source index specifying the values to be moved.
	 * @param targetIndex
	 *            The target index of the move.
	 */
	private void doMove(EObject container, int sourceIndex, int targetIndex) {
		final List<String> languages = UMLCompareUtil.getOpaqueElementLanguages(container);
		final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(container);

		final String bodyValueToMove = bodies.get(sourceIndex);
		final String languageValueToMove = languages.get(sourceIndex);

		int insertionIndex = targetIndex;
		if (sourceIndex < targetIndex) {
			insertionIndex--;
		}

		move(languages, languageValueToMove, insertionIndex);
		move(bodies, bodyValueToMove, insertionIndex);
	}

	/**
	 * Performs the move of the {@code value} in the given {@code list} to the given {@targetIndex}.
	 * 
	 * @param list
	 *            The list in which the move is to be performed.
	 * @param value
	 *            The value to be moved.
	 * @param targetIndex
	 *            The target index of the move to be performed.
	 */
	private void move(final List<String> list, final String value, int targetIndex) {
		list.remove(value);
		if (targetIndex < 0 || targetIndex > list.size()) {
			list.add(value);
		} else {
			list.add(targetIndex, value);
		}
	}

	/**
	 * Sets the {@link DifferenceState state} of all refining differences to merged.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to set its refining differences to merged.
	 */
	private void setRefiningDiffsMerged(OpaqueElementBodyChange bodyChange) {
		for (Diff refiningDiff : bodyChange.getRefinedBy()) {
			refiningDiff.setState(DifferenceState.MERGED);
		}
	}

	@Override
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		/*
		 * We take care of merging the refining diffs in this merger anyway, so we remove them from the direct
		 * merge dependencies to avoid being called for them again before we even have completed the merge of
		 * the body change itself.
		 */
		Set<Diff> dependencies = super.getDirectMergeDependencies(diff, mergeRightToLeft);
		dependencies.removeAll(diff.getRefinedBy());
		return dependencies;
	}

	@Override
	protected int findInsertionIndex(Comparison comparison, Diff diff, boolean rightToLeft) {
		/*
		 * If body values are added (also if deletions are rejected), we have to make sure that the insertion
		 * index corresponds to the value of the language value they belong to. Therefore, above we made sure
		 * (by sorting differences) that language value changes are applied before body changes and here we
		 * ensure that the body value will be inserted at the index of the language value of the
		 * OpaqueElementBodyChange it belongs to.
		 */
		if (shouldUseInsertionIndexOfAffectedLanguage(diff, rightToLeft)) {
			final EObject expectedContainer = getExpectedContainer(diff, rightToLeft);
			final Optional<String> language = getAffectedLanguage(diff);
			return getOpaqueElementLanguages(expectedContainer).indexOf(language.get());
		} else {
			return super.findInsertionIndex(comparison, diff, rightToLeft);
		}
	}

	/**
	 * Returns the container that will be modified by the given {@code diff} in the current merging. This will
	 * be different depending on the merge direction specified in {@code rightToLeft}.
	 * 
	 * @param diff
	 *            The difference to get the container for.
	 * @param rightToLeft
	 *            The direction of the current merge.
	 * @return The expected container.
	 */
	private EObject getExpectedContainer(Diff diff, boolean rightToLeft) {
		final EObject expectedContainer;
		final Match match = diff.getMatch();
		if (rightToLeft) {
			expectedContainer = match.getLeft();
		} else {
			expectedContainer = match.getRight();
		}
		return expectedContainer;
	}

	/**
	 * Returns the {@link Optional optional} language value that is affected by the given {@code diff}.
	 * <p>
	 * The affected language is resolved by obtaining the {@link OpaqueElementBodyChange} that is refined by
	 * the given {@code diff} and returning its {@link OpaqueElementBodyChange#getLanguate() language. Thus,
	 * if the given {@code diff} is not refining an opaque element body change, the language value will be
	 * absent.
	 * 
	 * @param diff
	 *            The difference to get the corresponding language value for.
	 * @return The language value affected by the given difference.
	 */
	private Optional<String> getAffectedLanguage(Diff diff) {
		final Optional<OpaqueElementBodyChange> bodyChange = getRefinedOpaqueElementBodyChange(diff);
		if (bodyChange.isPresent()) {
			return Optional.of(bodyChange.get().getLanguage());
		} else {
			return Optional.absent();
		}
	}

	/**
	 * Specifies whether we should use the index of the corresponding language value as an insertion index
	 * when merging the given {@code diff}.
	 * <p>
	 * The corresponding language index is the index of the language value that is affected by the given
	 * {@code diff}. This index has to be used if there is an addition of a body value for which we can obtain
	 * a corresponding and existing language value in the expected container.
	 * </p>
	 * 
	 * @param diff
	 *            The diff to determine whether we should use the index of the affected language.
	 * @param rightToLeft
	 *            The direction of merging, used for obtaining expected container
	 * @return <code>true</code> if the affected language value index should be used, <code>false</code>
	 *         otherwise.
	 */
	private boolean shouldUseInsertionIndexOfAffectedLanguage(Diff diff, boolean rightToLeft) {
		if (isChangeOfOpaqueElementBodyAttribute(diff)) {
			final Optional<String> affectedLanguage = getAffectedLanguage(diff);
			final EObject expectedContainer = getExpectedContainer(diff, rightToLeft);
			return affectedLanguage.isPresent()
					&& getOpaqueElementLanguages(expectedContainer).contains(affectedLanguage.get());
		} else {
			return false;
		}
	}

}
