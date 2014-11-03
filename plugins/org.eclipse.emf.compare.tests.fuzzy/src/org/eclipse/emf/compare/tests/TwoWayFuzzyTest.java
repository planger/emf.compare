/*******************************************************************************
 * Copyright (c) EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.ESEMFDataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.ESMutateUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Data;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Util;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.ESFuzzyRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;

/**
 * Fuzz test for two-way differencing and batch merging.
 * <p>
 * This test generates a left-hand side version, copies it into a right-hand side version, mutates the
 * right-hand side version, and then compare the left-hand side version with the right-hand side version.
 * Finally, it merges the obtained differences from the left-hand side into the version of the right-hand side
 * using the {@link BatchMerger} and tests whether the resulting merged version is equal to the original
 * left-hand side version, which should be true since by applying all changes of the left-hand side to the
 * right-hand side model should transform the right-hand side model into the left-hand side model. The same
 * procedure is then repeated in the opposite direction; that is merging right-hand side version into the
 * left-hand side version and testing whether the merged version is equal to the original right-hand side
 * version.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@RunWith(ESFuzzyRunner.class)
@DataProvider(ESEMFDataProvider.class)
public class TwoWayFuzzyTest {

	public enum MergeDirection {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}

	public class TwoWayMergeData {

		protected Notifier left;

		protected Notifier right;

		protected MergeDirection direction;

		protected TwoWayMergeData(Notifier left, Notifier right, MergeDirection direction) {
			this.left = left;
			this.right = right;
			this.direction = direction;
		}

	}

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Data
	protected EObject generatedRootObject;

	@Util
	protected ESMutateUtil mutateUtil;

	protected EObject leftRootObject;

	protected EObject rightRootObject;

	protected EObject leftWorkingRootObject;

	protected EObject rightWorkingRootObject;

	@Before
	public void prepareTwoVersions() {
		createLeftRootObject();
		createRightRootObject();
		saveLeftWorkingRootObject();
		saveRightWorkingRootObject();
	}

	private void createLeftRootObject() {
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveLeftRootObject(generatedRootObject);
	}

	private void createRightRootObject() {
		mutateUtil.mutate();
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveRightRootObject(generatedRootObject);
	}

	protected void saveLeftRootObject(EObject eObject) {
		this.leftRootObject = EcoreUtil.copy(eObject);
	}

	private void saveRightRootObject(EObject eObject) {
		this.rightRootObject = EcoreUtil.copy(eObject);
	}

	protected void saveLeftWorkingRootObject() {
		this.leftWorkingRootObject = EcoreUtil.copy(leftRootObject);
	}

	protected void saveRightWorkingRootObject() {
		this.rightWorkingRootObject = EcoreUtil.copy(rightRootObject);
	}

	protected Notifier getRightOriginalNotifier() {
		return rightRootObject;
	}

	protected Notifier getLeftOriginalNotifier() {
		return leftRootObject;
	}

	protected Notifier getRightWorkingNotifier() {
		return rightWorkingRootObject;
	}

	protected Notifier getLeftWorkingNotifier() {
		return leftWorkingRootObject;
	}

	@Test
	public void diffAndMergeAllRightToLeft() {
		final Notifier left = getLeftWorkingNotifier();
		final Notifier right = getRightWorkingNotifier();
		final MergeDirection direction = MergeDirection.RIGHT_TO_LEFT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(left, right, direction));
	}

	@Test
	public void diffAndMergeAllLeftToRight() {
		final Notifier left = getLeftWorkingNotifier();
		final Notifier right = getRightWorkingNotifier();
		final MergeDirection direction = MergeDirection.LEFT_TO_RIGHT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(left, right, direction));
	}

	protected void performBatchMergeAndEqualityCheckTest(TwoWayMergeData data) {
		EList<Diff> diffs = mergeAndRecompare(data);
		assertNoDifferencesAndPrintDebugInfo(data, diffs);
	}

	protected EList<Diff> mergeAndRecompare(TwoWayMergeData data) {
		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(data.left, data.right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		// shuffle differences to cover different orders of diffs to be merged
		Collections.shuffle(differences, new Random(mutateUtil.getSeed()));

		// batch merging of all detected differences
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		switch (data.direction) {
			case LEFT_TO_RIGHT:
				merger.copyAllLeftToRight(differences, new BasicMonitor());
				break;
			case RIGHT_TO_LEFT:
				merger.copyAllRightToLeft(differences, new BasicMonitor());
				break;
		}

		// re-compare and return detected differences
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		return assertionComparison.getDifferences();
	}

	private void assertNoDifferencesAndPrintDebugInfo(TwoWayMergeData data, EList<Diff> differences) {
		final int success = differences.size();
		if (success > 0) {
			final Notifier rightOriginal = getRightOriginalNotifier();
			final Notifier leftOriginal = getLeftOriginalNotifier();
			TwoWayMergeData originalData = new TwoWayMergeData(leftOriginal, rightOriginal, data.direction);
			reportFailure(originalData, differences);
		}
		Assert.assertEquals("We still have differences after merging all of them.", 0, success);
	}

	protected void reportFailure(TwoWayMergeData data, EList<Diff> differences) {
		mutateUtil.saveEObject((EObject)data.left, getDebugFileName("left", data.direction), true);
		mutateUtil.saveEObject((EObject)data.right, getDebugFileName("right", data.direction), true);
	}

	private String getDebugFileName(String fileName, MergeDirection direction) {
		final String directionString = getDirectionName(direction);
		return directionString + "_" + fileName;
	}

	private String getDirectionName(MergeDirection direction) {
		final String directionString;
		switch (direction) {
			case RIGHT_TO_LEFT:
				directionString = "r2l";
				break;
			case LEFT_TO_RIGHT:
				directionString = "l2r";
				break;
			default:
				directionString = "";
		}
		return directionString;
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

	/**
	 * Since the ModelMutator might leave the model in an invalid state, especially regarding references to
	 * deleted elements, this method can be used to clean up the model
	 * 
	 * @param contentRoot
	 *            The root of the model
	 */
	protected void removeAllInvalidReferences(EObject contentRoot) {
		Set<EObject> allObjects = new HashSet<EObject>();
		for (Iterator<EObject> it = EcoreUtil.getAllProperContents(contentRoot, true); it.hasNext();) {
			allObjects.add(it.next());
		}

		for (EObject object : allObjects) {
			for (EObject reference : new ArrayList<EObject>(object.eCrossReferences())) {
				if (!allObjects.contains(reference)) {
					for (EStructuralFeature feat : object.eClass().getEAllStructuralFeatures()) {
						EcoreUtil.remove(object, feat, reference);
					}
				}
			}
		}
	}

}
