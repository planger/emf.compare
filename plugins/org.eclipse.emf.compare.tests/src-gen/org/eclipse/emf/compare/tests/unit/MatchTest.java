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
package org.eclipse.emf.compare.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link Match} class.
 * 
 * @generated
 */
public class MatchTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>submatches</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testSubmatches() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatch_Submatches();
		Match match = CompareFactory.eINSTANCE.createMatch();
		match.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match submatchesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();
		List<org.eclipse.emf.compare.Match> listSubmatches = new ArrayList<org.eclipse.emf.compare.Match>(1);
		listSubmatches.add(submatchesValue);

		assertFalse(match.eIsSet(feature));
		assertTrue(match.getSubmatches().isEmpty());

		match.getSubmatches().add(submatchesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getSubmatches().contains(submatchesValue));
		assertSame(match.getSubmatches(), match.eGet(feature));
		assertSame(match.getSubmatches(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getSubmatches().isEmpty());
		assertSame(match.getSubmatches(), match.eGet(feature));
		assertSame(match.getSubmatches(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));

		match.eSet(feature, listSubmatches);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getSubmatches().contains(submatchesValue));
		assertSame(match.getSubmatches(), match.eGet(feature));
		assertSame(match.getSubmatches(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>differences</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testDifferences() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatch_Differences();
		Match match = CompareFactory.eINSTANCE.createMatch();
		match.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff differencesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listDifferences = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listDifferences.add(differencesValue);

		assertFalse(match.eIsSet(feature));
		assertTrue(match.getDifferences().isEmpty());

		match.getDifferences().add(differencesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getDifferences().contains(differencesValue));
		assertSame(match.getDifferences(), match.eGet(feature));
		assertSame(match.getDifferences(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));
		assertTrue(differencesValue.getMatch() == match);

		match.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getDifferences().isEmpty());
		assertSame(match.getDifferences(), match.eGet(feature));
		assertSame(match.getDifferences(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));
		assertFalse(differencesValue.getMatch() == match);

		match.eSet(feature, listDifferences);
		assertTrue(notified);
		notified = false;
		assertTrue(match.getDifferences().contains(differencesValue));
		assertSame(match.getDifferences(), match.eGet(feature));
		assertSame(match.getDifferences(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));
		assertTrue(differencesValue.getMatch() == match);
	}

	/**
	 * Tests the behavior of reference <code>left</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testLeft() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatch_Left();
		Match match = CompareFactory.eINSTANCE.createMatch();
		match.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EObject leftValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEObject();

		assertFalse(match.eIsSet(feature));
		assertNull(match.getLeft());

		match.setLeft(leftValue);
		assertTrue(notified);
		notified = false;
		assertSame(leftValue, match.getLeft());
		assertSame(match.getLeft(), match.eGet(feature));
		assertSame(match.getLeft(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(match.getLeft());
		assertSame(match.getLeft(), match.eGet(feature));
		assertSame(match.getLeft(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));

		match.setLeft(leftValue);
		assertTrue(notified);
		notified = false;
		assertSame(leftValue, match.getLeft());
		assertSame(match.getLeft(), match.eGet(feature));
		assertSame(match.getLeft(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eSet(feature, leftValue);
		assertTrue(notified);
		notified = false;
		assertSame(leftValue, match.getLeft());
		assertSame(match.getLeft(), match.eGet(feature));
		assertSame(match.getLeft(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.setLeft(null);
		assertTrue(notified);
		notified = false;
		assertNull(match.getLeft());
		assertSame(feature.getDefaultValue(), match.getLeft());
		assertSame(match.getLeft(), match.eGet(feature));
		assertSame(match.getLeft(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>right</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRight() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatch_Right();
		Match match = CompareFactory.eINSTANCE.createMatch();
		match.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EObject rightValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEObject();

		assertFalse(match.eIsSet(feature));
		assertNull(match.getRight());

		match.setRight(rightValue);
		assertTrue(notified);
		notified = false;
		assertSame(rightValue, match.getRight());
		assertSame(match.getRight(), match.eGet(feature));
		assertSame(match.getRight(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(match.getRight());
		assertSame(match.getRight(), match.eGet(feature));
		assertSame(match.getRight(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));

		match.setRight(rightValue);
		assertTrue(notified);
		notified = false;
		assertSame(rightValue, match.getRight());
		assertSame(match.getRight(), match.eGet(feature));
		assertSame(match.getRight(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eSet(feature, rightValue);
		assertTrue(notified);
		notified = false;
		assertSame(rightValue, match.getRight());
		assertSame(match.getRight(), match.eGet(feature));
		assertSame(match.getRight(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.setRight(null);
		assertTrue(notified);
		notified = false;
		assertNull(match.getRight());
		assertSame(feature.getDefaultValue(), match.getRight());
		assertSame(match.getRight(), match.eGet(feature));
		assertSame(match.getRight(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>origin</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testOrigin() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatch_Origin();
		Match match = CompareFactory.eINSTANCE.createMatch();
		match.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EObject originValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEObject();

		assertFalse(match.eIsSet(feature));
		assertNull(match.getOrigin());

		match.setOrigin(originValue);
		assertTrue(notified);
		notified = false;
		assertSame(originValue, match.getOrigin());
		assertSame(match.getOrigin(), match.eGet(feature));
		assertSame(match.getOrigin(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(match.getOrigin());
		assertSame(match.getOrigin(), match.eGet(feature));
		assertSame(match.getOrigin(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));

		match.setOrigin(originValue);
		assertTrue(notified);
		notified = false;
		assertSame(originValue, match.getOrigin());
		assertSame(match.getOrigin(), match.eGet(feature));
		assertSame(match.getOrigin(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.eSet(feature, originValue);
		assertTrue(notified);
		notified = false;
		assertSame(originValue, match.getOrigin());
		assertSame(match.getOrigin(), match.eGet(feature));
		assertSame(match.getOrigin(), match.eGet(feature, false));
		assertTrue(match.eIsSet(feature));

		match.setOrigin(null);
		assertTrue(notified);
		notified = false;
		assertNull(match.getOrigin());
		assertSame(feature.getDefaultValue(), match.getOrigin());
		assertSame(match.getOrigin(), match.eGet(feature));
		assertSame(match.getOrigin(), match.eGet(feature, false));
		assertFalse(match.eIsSet(feature));
	}

}
