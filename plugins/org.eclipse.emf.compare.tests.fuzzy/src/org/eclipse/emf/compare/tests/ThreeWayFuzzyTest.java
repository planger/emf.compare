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

import java.util.Collections;
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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.ESMutateUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Data;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Util;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Fuzz test for three-way differencing and merging.
 * <p>
 * This test generates an origin version and creates two copies, a left-hand side copy and a right-hand side
 * copy. One of these two copies is then mutated before a three-way comparison is performed. Next, all changes
 * are applied to the side model that has not been mutated. Ultimately, this test checks whether both
 * resulting models are equal to each other, which should be true since both versions should correspond to the
 * mutated model version.
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
	protected ESMutateUtil mutateUtil;

	protected EObject originRootObject;

	protected EObject leftRootObject;

	protected EObject rightRootObject;

	protected boolean mutateLeft;

	@Before
	public void prepareTwoVersions() {
		createOriginRootObject();
		mutateLeft = new Random(mutateUtil.getSeed()).nextBoolean();
		if (mutateLeft) {
			createRightRootObject();
			mutateUtil.mutate();
			createLeftRootObject();
		} else {
			createLeftRootObject();
			mutateUtil.mutate();
			createRightRootObject();
		}
	}

	private void createOriginRootObject() {
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveOriginRootObject(generatedRootObject);
	}

	private void createLeftRootObject() {
		removeAllDuplicateCrossReferences(generatedRootObject);
		saveLeftRootObject(generatedRootObject);
	}

	private void createRightRootObject() {
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

//		mutateUtil.saveEObject((EObject)origin, "origin_", false);
//		mutateUtil.saveEObject((EObject)left, "left_", false);
//		mutateUtil.saveEObject((EObject)right, "right_", false);

		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		// shuffle differences to cover different orders of diffs to be merged
		Collections.shuffle(differences, new Random(mutateUtil.getSeed()));

		// merge mutated side into non-mutated side
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (mutateLeft) {
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		} else {
			merger.copyAllRightToLeft(differences, new BasicMonitor());
		}

		// check for differences between left and right
		final IComparisonScope assertionScope = new DefaultComparisonScope(left, right, null);
		final Comparison assertionComparison = EMFCompare.builder().build().compare(assertionScope);
		final EList<Diff> assertionDiffs = assertionComparison.getDifferences();

		assertTrue(assertionDiffs.isEmpty());
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
