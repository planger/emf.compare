package org.eclipse.emf.compare.tests;

import static org.eclipse.emf.compare.utils.ReferenceUtil.getAsList;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

import com.google.common.base.Optional;

/**
 * Diff engine that ignores the ordering in multi valued features while
 * performing a two way diff.
 *
 */
public class OrderIgnoringDiffEngine extends DefaultDiffEngine {

	@Override
	protected void computeMultiValuedFeatureDifferencesTwoWay(Match match, EStructuralFeature feature,
			boolean checkOrdering) {
		final Comparison comparison = match.getComparison();
		final IEqualityHelper equality = comparison.getEqualityHelper();

		// We won't use iterables here since we need random access collections for fast LCS.
		final List<Object> leftValues = getAsList(match.getLeft(), feature);
		final List<Object> rightValues = getAsList(match.getRight(), feature);

		final List<Object> lcs = DiffUtil.longestCommonSubsequence(comparison, rightValues, leftValues);

		int lcsCursor = 0;
		Optional<Object> lcsCurrent = getIfPresent(lcs, lcsCursor);

		boolean equalSets = true;
		for (Object leftValue : leftValues) {
			if (!contains(comparison, rightValues, leftValue)) {
				equalSets = false;
				break;
			}
		}

		for (Object diffCandidate : leftValues) {
			// See bug 405000 for this strange iteration on the LCS
			if (equality.matchingValues(diffCandidate, lcsCurrent.orNull())) {
				lcsCursor++;
				lcsCurrent = getIfPresent(lcs, lcsCursor);
				continue;
			}

			if (contains(comparison, rightValues, diffCandidate)) {
				if (checkOrdering && !equalSets) {
					featureChange(match, feature, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				}
			} else if (FeatureMapUtil.isFeatureMap(feature) && diffCandidate instanceof FeatureMap.Entry) {
				// A value of a FeatureMap changed his key
				if (isFeatureMapEntryKeyChange(equality, (FeatureMap.Entry)diffCandidate, rightValues)) {
					featureChange(match, feature, diffCandidate, DifferenceKind.CHANGE, DifferenceSource.LEFT);
				} else if (isFeatureMapEntryMove(comparison, (FeatureMap.Entry)diffCandidate,
						DifferenceSource.LEFT)) {
					featureChange(match, feature, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				} else {
					featureChange(match, feature, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
				}
			} else {
				featureChange(match, feature, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
			}
		}

		// deleted
		for (Object diffCandidate : rightValues) {
			// A value that is in the right but not in the left has been deleted.
			// However, we do not want attribute changes on removed elements and in case of a FeatureMapChange
			// of kind DifferenceKind.CHANGE or DifferenceKind.MOVE
			if (!contains(comparison, leftValues, diffCandidate)) {
				if ((feature instanceof EReference || match.getLeft() != null)
						&& !isFeatureMapChangeOrMove(comparison, feature, diffCandidate, leftValues,
								DifferenceSource.LEFT)) {
					featureChange(match, feature, diffCandidate, DifferenceKind.DELETE, DifferenceSource.LEFT);
				}
			}
		}
	}
	
}
