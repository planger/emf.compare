/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - Adds additional test classes
 *******************************************************************************/
package org.eclipse.emf.compare.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.tests.command.CommandStackTestSuite;
import org.eclipse.emf.compare.tests.conflict.ConflictDetectionTest;
import org.eclipse.emf.compare.tests.conflict.MultiLineAttributeConflictDetectionTest;
import org.eclipse.emf.compare.tests.diff.ComparisonUtilTest;
import org.eclipse.emf.compare.tests.diff.DiffUtilTest;
import org.eclipse.emf.compare.tests.diff.LCSPerformanceTest;
import org.eclipse.emf.compare.tests.diff.ThreeWayTextDiffTest;
import org.eclipse.emf.compare.tests.diff.URIDistanceTest;
import org.eclipse.emf.compare.tests.edit.AllEditTests;
import org.eclipse.emf.compare.tests.equi.EquiComputingTest;
import org.eclipse.emf.compare.tests.fragmentation.FragmentationTest;
import org.eclipse.emf.compare.tests.fullcomparison.DynamicInstanceComparisonTest;
import org.eclipse.emf.compare.tests.fullcomparison.ExtLibraryTest;
import org.eclipse.emf.compare.tests.fullcomparison.IdentifierComparisonTest;
import org.eclipse.emf.compare.tests.fullcomparison.ProximityComparisonTest;
import org.eclipse.emf.compare.tests.match.MatchEngineFactoryRegistryTest;
import org.eclipse.emf.compare.tests.match.ProximityIndexTest;
import org.eclipse.emf.compare.tests.merge.ConflictMergeTest;
import org.eclipse.emf.compare.tests.merge.ExtensionMergeTest;
import org.eclipse.emf.compare.tests.merge.FeatureMaps2wayMergeTest;
import org.eclipse.emf.compare.tests.merge.FeatureMaps3wayMergeTest;
import org.eclipse.emf.compare.tests.merge.FeatureMapsConflictsMergeTest;
import org.eclipse.emf.compare.tests.merge.FeatureMapsPseudoConflictsMergeTest;
import org.eclipse.emf.compare.tests.merge.IndividualMergeOutOfScopeValuesTest;
import org.eclipse.emf.compare.tests.merge.IndividualMergeTest;
import org.eclipse.emf.compare.tests.merge.MultiLineAttributeMergeTest;
import org.eclipse.emf.compare.tests.merge.MultipleMergeTest;
import org.eclipse.emf.compare.tests.merge.PseudoConflictMergeTest;
import org.eclipse.emf.compare.tests.merge.TwoWayBatchMergingTest;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.compare.tests.postprocess.PostProcessorTest;
import org.eclipse.emf.compare.tests.rcp.AllRCPTests;
import org.eclipse.emf.compare.tests.req.ReqComputingTest;
import org.eclipse.emf.compare.tests.scope.DefaultComparisonScopeTest;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows us to launch all tests for EMF Compare at once.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({CompareTestSuite.class, DefaultComparisonScopeTest.class, IdentifierComparisonTest.class,
		ExtLibraryTest.class, ConflictDetectionTest.class, ReqComputingTest.class, EquiComputingTest.class,
		DiffUtilTest.class, ComparisonUtilTest.class, LCSPerformanceTest.class, MultipleMergeTest.class,
		PostProcessorTest.class, IndividualMergeTest.class, ExtensionMergeTest.class,
		IndividualMergeOutOfScopeValuesTest.class, ProximityComparisonTest.class,
		DynamicInstanceComparisonTest.class, URIDistanceTest.class, FragmentationTest.class,
		AllEditTests.class, CommandStackTestSuite.class, MatchEngineFactoryRegistryTest.class,
		ConflictMergeTest.class, PseudoConflictMergeTest.class, ProximityIndexTest.class, AllRCPTests.class,
		FeatureMaps2wayMergeTest.class, FeatureMaps3wayMergeTest.class, FeatureMapsConflictsMergeTest.class,
		FeatureMapsPseudoConflictsMergeTest.class, TwoWayBatchMergingTest.class,
		MultiLineAttributeConflictDetectionTest.class, ThreeWayTextDiffTest.class,
		MultiLineAttributeMergeTest.class })
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
		return new JUnit4TestAdapter(CompareTestSuite.class);
	}

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("nodes", //$NON-NLS-1$
				new NodesResourceFactoryImpl());
	}
}
