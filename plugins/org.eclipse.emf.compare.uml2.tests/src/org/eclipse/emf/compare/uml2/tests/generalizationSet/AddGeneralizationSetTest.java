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
package org.eclipse.emf.compare.uml2.tests.generalizationSet;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.referenceValueMatch;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.generalizationSet.data.GeneralizationSetInputData;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddGeneralizationSetTest extends AbstractUMLTest {

	private GeneralizationSetInputData input = new GeneralizationSetInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertEquals(6, differences.size());

		Predicate<? super Diff> addGeneralizationSetDescription = null;
		Predicate<? super Diff> addRefGeneralizationSetInClass2Description = null;
		Predicate<? super Diff> addRefGeneralizationSetInClass0Description = null;
		Predicate<? super Diff> addRefGeneralizationInGeneralizationSetDescription1 = null;
		Predicate<? super Diff> addRefGeneralizationInGeneralizationSetDescription2 = null;

		if (kind.equals(TestKind.DELETE)) {
			addGeneralizationSetDescription = removed("model.GeneralizationSet_Class2_Class0"); //$NON-NLS-1$
			addRefGeneralizationSetInClass2Description = removedFromReference1("model.Class2.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationSetInClass0Description = removedFromReference1("model.Class0.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationInGeneralizationSetDescription1 = removedFromReference2(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class0.Class1",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationInGeneralizationSetDescription2 = removedFromReference2(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class2.Class1",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
		} else {
			addGeneralizationSetDescription = added("model.GeneralizationSet_Class2_Class0"); //$NON-NLS-1$
			addRefGeneralizationSetInClass2Description = addedToReference1("model.Class2.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationSetInClass0Description = addedToReference1("model.Class0.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationInGeneralizationSetDescription1 = addedToReference2(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class0.Class1",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
			addRefGeneralizationInGeneralizationSetDescription2 = addedToReference2(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class2.Class1",
					UMLPackage.Literals.GENERALIZATION__GENERAL);
		}

		final Diff addGeneralizationSet = Iterators.find(differences.iterator(),
				addGeneralizationSetDescription);
		final Diff addRefGeneralizationSetInClass0 = Iterators.find(differences.iterator(),
				addRefGeneralizationSetInClass0Description);
		final Diff addRefGeneralizationSetInClass2 = Iterators.find(differences.iterator(),
				addRefGeneralizationSetInClass2Description);
		final Diff addRefGeneralizationInGeneralizationSet1 = Iterators.find(differences.iterator(),
				addRefGeneralizationInGeneralizationSetDescription1);
		final Diff addRefGeneralizationInGeneralizationSet2 = Iterators.find(differences.iterator(),
				addRefGeneralizationInGeneralizationSetDescription2);

		assertNotNull(addGeneralizationSet);
		assertNotNull(addRefGeneralizationSetInClass0);
		assertNotNull(addRefGeneralizationSetInClass2);
		assertNotNull(addRefGeneralizationInGeneralizationSet1);
		assertNotNull(addRefGeneralizationInGeneralizationSet2);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(GeneralizationSetChange.class)));
		Diff addUMLGeneralizationSet = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLGeneralizationSet = Iterators.find(differences.iterator(), and(
					instanceOf(GeneralizationSetChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLGeneralizationSet = Iterators.find(differences.iterator(), and(
					instanceOf(GeneralizationSetChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLGeneralizationSet);
		assertEquals(5, addUMLGeneralizationSet.getRefinedBy().size());
		assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addRefGeneralizationInGeneralizationSet1));
		assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addRefGeneralizationInGeneralizationSet2));
		assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addGeneralizationSet));
		assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addRefGeneralizationSetInClass0));
		assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addRefGeneralizationSetInClass2));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addRefGeneralizationInGeneralizationSet1.getRequires().size());
			assertTrue(addRefGeneralizationInGeneralizationSet1.getRequires().contains(addGeneralizationSet));

			assertEquals(1, addRefGeneralizationInGeneralizationSet2.getRequires().size());
			assertTrue(addRefGeneralizationInGeneralizationSet2.getRequires().contains(addGeneralizationSet));

			assertEquals(1, addRefGeneralizationSetInClass0.getRequires().size());
			assertTrue(addRefGeneralizationSetInClass0.getRequires().contains(addGeneralizationSet));

			assertEquals(1, addRefGeneralizationSetInClass2.getRequires().size());
			assertTrue(addRefGeneralizationSetInClass2.getRequires().contains(addGeneralizationSet));

			assertEquals(0, addGeneralizationSet.getRequires().size());
			assertEquals(0, addUMLGeneralizationSet.getRequires().size());
		} else {
			assertEquals(0, addRefGeneralizationInGeneralizationSet1.getRequires().size());

			assertEquals(0, addRefGeneralizationInGeneralizationSet2.getRequires().size());

			assertEquals(0, addRefGeneralizationSetInClass0.getRequires().size());

			assertEquals(0, addRefGeneralizationSetInClass2.getRequires().size());

			assertEquals(4, addGeneralizationSet.getRequires().size());
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationInGeneralizationSet1));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationInGeneralizationSet2));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationSetInClass0));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationSetInClass2));

			assertEquals(0, addUMLGeneralizationSet.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(2, comparison.getEquivalences().size());

		assertNotNull(addRefGeneralizationInGeneralizationSet1.getEquivalence());
		assertEquals(2, addRefGeneralizationInGeneralizationSet1.getEquivalence().getDifferences().size());
		assertTrue(addRefGeneralizationInGeneralizationSet1.getEquivalence().getDifferences().contains(
				addRefGeneralizationSetInClass0));

		assertNotNull(addRefGeneralizationInGeneralizationSet2.getEquivalence());
		assertEquals(2, addRefGeneralizationInGeneralizationSet2.getEquivalence().getDifferences().size());
		assertTrue(addRefGeneralizationInGeneralizationSet2.getEquivalence().getDifferences().contains(
				addRefGeneralizationSetInClass2));

		testIntersections(comparison);

	}

	public static Predicate<? super Diff> removedFromReference1(final String qualifiedName,
			final String referenceName, final String removedQualifiedName,
			final EStructuralFeature featureDelegate) {
		// This is only meant for multi-valued references
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName, featureDelegate),
				referenceValueMatch(referenceName, removedQualifiedName, true));
	}

	public static Predicate<? super Diff> removedFromReference2(final String qualifiedName,
			final String referenceName, final String removedQualifiedName,
			final EStructuralFeature featureDelegate) {
		// This is only meant for multi-valued references
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName), referenceValueMatch(
				referenceName, removedQualifiedName, true, featureDelegate));
	}

	public static Predicate<? super Diff> addedToReference1(final String qualifiedName,
			final String referenceName, final String removedQualifiedName,
			final EStructuralFeature featureDelegate) {
		// This is only meant for multi-valued references
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName, featureDelegate),
				referenceValueMatch(referenceName, removedQualifiedName, true));
	}

	public static Predicate<? super Diff> addedToReference2(final String qualifiedName,
			final String referenceName, final String removedQualifiedName,
			final EStructuralFeature featureDelegate) {
		// This is only meant for multi-valued references
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), referenceValueMatch(referenceName,
				removedQualifiedName, true, featureDelegate));
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
