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
package org.eclipse.emf.compare.uml2.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.uml2.tests.association.AddAssociation2Test;
import org.eclipse.emf.compare.uml2.tests.association.AddAssociation3Test;
import org.eclipse.emf.compare.uml2.tests.association.AddAssociationTest;
import org.eclipse.emf.compare.uml2.tests.association.ChangeAssociationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddAbstractionTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddDependencyTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddInterfaceRealizationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddRealizationTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddSubstitutionTest;
import org.eclipse.emf.compare.uml2.tests.dependency.AddUsageTest;
import org.eclipse.emf.compare.uml2.tests.dependency.ChangeDependencyTest;
import org.eclipse.emf.compare.uml2.tests.dependency.ChangeUsageTest;
import org.eclipse.emf.compare.uml2.tests.edit.provider.DynamicStereotypedElementItemProviderTest;
import org.eclipse.emf.compare.uml2.tests.edit.provider.StaticStereotypedElementItemProviderTest;
import org.eclipse.emf.compare.uml2.tests.executionSpecification.AddActionExecutionSpecificationTest;
import org.eclipse.emf.compare.uml2.tests.executionSpecification.AddBehaviorExecutionSpecificationTest;
import org.eclipse.emf.compare.uml2.tests.extend.AddExtendTest;
import org.eclipse.emf.compare.uml2.tests.generalizationSet.AddGeneralizationSetTest;
import org.eclipse.emf.compare.uml2.tests.implications.ImplicationsAssociationTest;
import org.eclipse.emf.compare.uml2.tests.implications.ImplicationsInterfaceRealizationTest;
import org.eclipse.emf.compare.uml2.tests.implications.ImplicationsTransitionTest;
import org.eclipse.emf.compare.uml2.tests.include.AddIncludeTest;
import org.eclipse.emf.compare.uml2.tests.merge.ExtensionMergeTest;
import org.eclipse.emf.compare.uml2.tests.message.AddMessageTest;
import org.eclipse.emf.compare.uml2.tests.profiles.DynamicProfileTest;
import org.eclipse.emf.compare.uml2.tests.profiles.StaticProfileTest;
import org.eclipse.emf.compare.uml2.tests.stereotypes.DynamicStereotypeTest;
import org.eclipse.emf.compare.uml2.tests.stereotypes.DynamicStereotypedElementChangeTests;
import org.eclipse.emf.compare.uml2.tests.stereotypes.StaticStereotypeTest;
import org.eclipse.emf.compare.uml2.tests.stereotypes.StaticStereotypedElementChangeTests;
import org.eclipse.emf.compare.uml2.tests.timeConstraint.AddTimeConstraintTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows us to launch all tests for EMF Compare at once.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({AddDependencyTest.class, AddAbstractionTest.class, AddAssociationTest.class,
		ExtensionMergeTest.class, AddAssociation2Test.class, ChangeAssociationTest.class,
		ChangeDependencyTest.class, ChangeUsageTest.class, AddAssociation3Test.class, AddExtendTest.class,
		AddGeneralizationSetTest.class, AddInterfaceRealizationTest.class, AddRealizationTest.class,
		AddSubstitutionTest.class, AddUsageTest.class, AddMessageTest.class,
		AddActionExecutionSpecificationTest.class, AddBehaviorExecutionSpecificationTest.class,
		AddIncludeTest.class, AddTimeConstraintTest.class, StaticProfileTest.class, DynamicProfileTest.class,
		StaticStereotypeTest.class, StaticStereotypedElementChangeTests.class, DynamicStereotypeTest.class,
		DynamicStereotypedElementChangeTests.class, ImplicationsAssociationTest.class,
		ImplicationsTransitionTest.class, ImplicationsInterfaceRealizationTest.class,
		StaticStereotypedElementItemProviderTest.class, DynamicStereotypedElementItemProviderTest.class })
public class AllTests {

	/**
	 * Standalone launcher for all of compare's tests.
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * This will return a suite populated with all tests available through this class.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}

}
