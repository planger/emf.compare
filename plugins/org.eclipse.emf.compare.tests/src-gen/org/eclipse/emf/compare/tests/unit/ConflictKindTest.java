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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.emf.compare.ConflictKind;
import org.junit.Test;

/**
 * Tests the behavior of the {@link ConflictKind} enumeration.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class ConflictKindTest {
	/**
	 * Tests the behavior of the {@link ConflictKind#get(int)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetInt() {
		int highestValue = -1;
		for (ConflictKind value : ConflictKind.VALUES) {
			assertSame(ConflictKind.get(value.getValue()), value);
			if (value.getValue() > highestValue) {
				highestValue = value.getValue();
			}
		}
		assertNull(ConflictKind.get(++highestValue));
	}

	/**
	 * Tests the behavior of the {@link ConflictKind#get(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetString() {
		for (ConflictKind value : ConflictKind.VALUES) {
			assertSame(ConflictKind.get(value.getLiteral()), value);
		}
		assertNull(ConflictKind.get("ThisIsNotAValueOfTheTestedEnum"));
	}

	/**
	 * Tests the behavior of the {@link ConflictKind#getByName(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetByName() {
		for (ConflictKind value : ConflictKind.VALUES) {
			assertSame(ConflictKind.getByName(value.getName()), value);
		}
		assertNull(ConflictKind.getByName("ThisIsNotTheNameOfAValueFromTheTestedEnum"));
	}
}
