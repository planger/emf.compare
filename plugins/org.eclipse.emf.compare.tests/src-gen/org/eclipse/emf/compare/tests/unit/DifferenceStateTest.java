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

import org.eclipse.emf.compare.DifferenceState;
import org.junit.Test;

/**
 * Tests the behavior of the {@link DifferenceState} enumeration.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class DifferenceStateTest {
	/**
	 * Tests the behavior of the {@link DifferenceState#get(int)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetInt() {
		int highestValue = -1;
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.get(value.getValue()), value);
			if (value.getValue() > highestValue) {
				highestValue = value.getValue();
			}
		}
		assertNull(DifferenceState.get(++highestValue));
	}

	/**
	 * Tests the behavior of the {@link DifferenceState#get(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetString() {
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.get(value.getLiteral()), value);
		}
		assertNull(DifferenceState.get("ThisIsNotAValueOfTheTestedEnum"));
	}

	/**
	 * Tests the behavior of the {@link DifferenceState#getByName(java.lang.String)} method.
	 * 
	 * @generated
	 */
	@Test
	public void testGetByName() {
		for (DifferenceState value : DifferenceState.VALUES) {
			assertSame(DifferenceState.getByName(value.getName()), value);
		}
		assertNull(DifferenceState.getByName("ThisIsNotTheNameOfAValueFromTheTestedEnum"));
	}
}
