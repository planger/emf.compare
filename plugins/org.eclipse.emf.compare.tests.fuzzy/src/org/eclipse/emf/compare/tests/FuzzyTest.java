/**
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests;

import java.util.Iterator;
import java.util.List;
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
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Data;
import org.eclipse.emf.emfstore.fuzzy.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.Annotations.Util;
import org.eclipse.emf.emfstore.fuzzy.FuzzyRunner;
import org.eclipse.emf.emfstore.fuzzy.emf.EMFDataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.MutateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;

/**
 * A brute force test using fuzzy testing.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@RunWith(FuzzyRunner.class)
@DataProvider(EMFDataProvider.class)
@SuppressWarnings("nls")
public class FuzzyTest {

	private enum Direction {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}

	private final int MUTATION_COUNT = 3;

	@Data
	private EObject root;

	@Util
	private MutateUtil util;

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	private EObject leftWorkingRootObject;

	private EObject rightWorkingRootObject;

	private EObject leftOriginalRootObject;

	private EObject rightOriginalRootObject;

	@Before
	public void mutate() {
		// get and prepare left working root object
		leftWorkingRootObject = root;
		removeAllDuplicateCrossReferencesFrom(leftWorkingRootObject);

		// create copy to create right working root object
		rightWorkingRootObject = EcoreUtil.copy(leftWorkingRootObject);

		// mutate left working root
		util.mutate(EcorePackage.eINSTANCE, leftWorkingRootObject, MUTATION_COUNT);
		removeAllDuplicateCrossReferencesFrom(leftWorkingRootObject);

		// create copies of left and right working objects to preserve
		// original versions of left and right
		leftOriginalRootObject = EcoreUtil.copy(leftWorkingRootObject);
		rightOriginalRootObject = EcoreUtil.copy(rightWorkingRootObject);
	}

	private static void removeAllDuplicateCrossReferencesFrom(EObject contentRoot) {
		for (EReference reference : contentRoot.eClass().getEAllReferences()) {
			if (!reference.isContainment() && !reference.isDerived() && reference.isMany()) {
				@SuppressWarnings("unchecked")
				final Iterator<EObject> crossReferences = ((List<EObject>)contentRoot.eGet(reference))
						.iterator();
				final Set<EObject> noDupes = Sets.newHashSet();
				while (crossReferences.hasNext()) {
					if (!noDupes.add(crossReferences.next())) {
						crossReferences.remove();
					}
				}
			}
		}

		final Iterator<EObject> contentIterator = contentRoot.eContents().iterator();
		while (contentIterator.hasNext()) {
			removeAllDuplicateCrossReferencesFrom(contentIterator.next());
		}
	}

	@Test
	public void copyAllRightToLeft() {
		performBatchMergeAndEqualityCheckTest(Direction.RIGHT_TO_LEFT);
	}

	@Test
	public void copyAllLeftToRight() {
		performBatchMergeAndEqualityCheckTest(Direction.LEFT_TO_RIGHT);
	}

	private void performBatchMergeAndEqualityCheckTest(Direction direction) {
		EList<Diff> diffs = mergeAndRecompare(leftWorkingRootObject, rightWorkingRootObject, direction);
		assertNoDifferencesAndPrintDebugInfo(diffs, direction);
	}

	private EList<Diff> mergeAndRecompare(Notifier left, Notifier right, Direction direction) {
		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		// batch merging of all detected differences
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		switch (direction) {
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

	private void assertNoDifferencesAndPrintDebugInfo(EList<Diff> differences, Direction direction) {
		if (differences.size() > 0) {
			util.saveEObject(leftOriginalRootObject, getDebugFileName("left", direction), true);
			util.saveEObject(rightOriginalRootObject, getDebugFileName("right", direction), true);
		}
		Assert.assertEquals("We still have differences after merging all of them.", 0, differences.size());
	}

	private String getDebugFileName(String fileName, Direction direction) {
		final String directionString = getDirectionName(direction);
		return directionString + "_" + fileName;
	}

	private String getDirectionName(Direction direction) {
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
}
