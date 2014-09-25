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

import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.emfstore.fuzzy.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.FuzzyRunner;
import org.eclipse.emf.emfstore.fuzzy.emf.EMFDataProvider;
import org.junit.runner.RunWith;

/**
 * A {@link ResourceTwoWayFuzzyTest} for the {@link NodesPackage nodes metamodel}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@RunWith(FuzzyRunner.class)
@DataProvider(EMFDataProvider.class)
public class NodesResourceTwoWayFuzzyTest extends ResourceTwoWayFuzzyTest {

}
