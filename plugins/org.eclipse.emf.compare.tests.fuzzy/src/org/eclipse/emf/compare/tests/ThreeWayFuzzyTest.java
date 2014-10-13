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
package org.eclipse.emf.compare.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Data;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Util;
import org.eclipse.emf.emfstore.fuzzy.emf.MutateUtil;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Fuzz test for three-way differencing and merging.
 * <p>
 * This test generates an origin version and creates two copies, a left-hand side copy and a right-hand side
 * copy. These two copies are then mutated before a three-way comparison is performed. Next, all
 * non-conflicting changes are applied to the left-hand side model (right-to-left) and all changes (including
 * conflicting ones) are applied to the right-hand side model (left-to-right). Ultimately, this test checks
 * whether both resulting models are equal to each other, which should be true since both versions should be
 * the origin version plus all changes from the left-hand side and all non-conflicting changes from the
 * right-hand side.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ThreeWayFuzzyTest {

	protected class ThreeWayMergeData {

		protected Notifier origin;

		protected Notifier left;

		protected Notifier right;

		protected ThreeWayMergeData(Notifier origin, Notifier left, Notifier right) {
			this.origin = origin;
			this.left = left;
			this.right = right;
		}

	}

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Data
	protected EObject generatedRootObject;

	@Util
	protected MutateUtil mutateUtil;

	private EObject originRootObject;

	private EObject leftRootObject;

	private EObject rightRootObject;

	@Before
	public void prepareTwoVersions() {
		createOriginRootObject();
		createLeftRootObject();
		createRightRootObject();
	}

	private void createOriginRootObject() {
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveOriginRootObject(generatedRootObject);
	}

	private void createLeftRootObject() {
		mutateUtil.mutate();
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveLeftRootObject(generatedRootObject);
	}

	private void createRightRootObject() {
		mutateUtil.mutate();
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveRightRootObject(generatedRootObject);
	}

	private void saveOriginRootObject(EObject eObject) {
		this.originRootObject = EcoreUtil.copy(eObject);
	}

	private void saveLeftRootObject(EObject eObject) {
		this.leftRootObject = EcoreUtil.copy(eObject);
	}

	private void saveRightRootObject(EObject eObject) {
		this.rightRootObject = EcoreUtil.copy(eObject);
	}

	protected Notifier getOriginNotifier() {
		return originRootObject;
	}

	protected Notifier getLeftNotifier() {
		return leftRootObject;
	}

	protected Notifier getRightNotifier() {
		return rightRootObject;
	}

	@Test
	public void threeWayDiffAndMerge() {
		final Notifier origin = getOriginNotifier();
		final Notifier left = getLeftNotifier();
		final Notifier right = getRightNotifier();

		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		// shuffle differences to cover different orders of diffs to be merged
		Collections.shuffle(differences, new Random(mutateUtil.getSeed()));

		final IBatchMerger merger = new BatchMerger(mergerRegistry);

		// apply all non-conflicting changes to left (right-to-left)
		final List<Diff> rightNonConflictingDiffs = filterDiffs(differences, DifferenceSource.RIGHT, true);
		merger.copyAllRightToLeft(rightNonConflictingDiffs, new BasicMonitor());

		// apply all changes to right (left-to-right)
		final List<Diff> leftDiffs = filterDiffs(differences, DifferenceSource.LEFT, false);
		merger.copyAllLeftToRight(leftDiffs, new BasicMonitor());

		// check for differences between left and right
		final IComparisonScope assertionScope = new DefaultComparisonScope(left, right, null);
		final Comparison assertionComparison = EMFCompare.builder().build().compare(assertionScope);
		final EList<Diff> assertionDiffs = assertionComparison.getDifferences();

		assertTrue(assertionDiffs.isEmpty());
	}

	private List<Diff> filterDiffs(List<Diff> diffs, DifferenceSource source, boolean filterConflicting) {
		List<Diff> filteredDiffs = new ArrayList<Diff>();
		for (Diff diff : diffs) {
			boolean isConflicting = diff.getConflict() != null && ConflictKind.REAL.equals(diff.getConflict().getKind());
			if (source.equals(diff.getSource()) && (!isConflicting || !filterConflicting)) {
				filteredDiffs.add(diff);
			}
		}
		return filteredDiffs;
	}

	protected void removeAllDuplicateCrossReferences(EObject contentRoot) {
		removeDuplicateCrossReferences(contentRoot);
		for (final Iterator<EObject> iter = contentRoot.eContents().iterator(); iter.hasNext();) {
			removeAllDuplicateCrossReferences(iter.next());
		}
	}

	private void removeDuplicateCrossReferences(EObject eObject) {
		for (EReference reference : eObject.eClass().getEAllReferences()) {
			if (!reference.isContainment() && !reference.isDerived() && reference.isMany()) {
				@SuppressWarnings("unchecked")
				final Iterator<EObject> crossReferences = ((List<EObject>)eObject.eGet(reference)).iterator();
				final Set<EObject> noDupes = Sets.newHashSet();
				while (crossReferences.hasNext()) {
					if (!noDupes.add(crossReferences.next())) {
						crossReferences.remove();
					}
				}
			}
		}
	}

}
