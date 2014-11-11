package org.eclipse.emf.compare.tests;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.ESEMFDataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Mutator;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.ESDefaultModelMutator;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.ESFuzzyRunner;
import org.eclipse.emf.emfstore.modelmutator.ESCrossResourceReferencesModelMutator;
import org.eclipse.emf.emfstore.modelmutator.ESModelMutatorConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ESFuzzyRunner.class)
@DataProvider(ESEMFDataProvider.class)
public class NodesIdentityTwoWayFuzzyTest extends TwoWayFuzzyTest {
	
	private ESDefaultModelMutator mutator;
	
	private ESDefaultModelMutator secondMutator;


	@Before
	public void prepareTwoVersions() {
		this.leftRootObject = EcoreUtil.copy(generatedRootObject);
		this.rightRootObject = EcoreUtil.copy(generatedRootObject);
		
		mutator = new ESDefaultModelMutator(getConfig(leftRootObject));
		secondMutator = new ESDefaultModelMutator(getConfig(rightRootObject));
		
		removeAllDuplicateCrossReferences(leftRootObject);
		removeAllDuplicateCrossReferences(rightRootObject);
		assertTrue(EcoreUtil.equals(leftRootObject, rightRootObject));
				
		mutator.mutate(Collections.<EStructuralFeature>emptySet());
		secondMutator.mutate(Collections.<EStructuralFeature>emptySet());
		removeAllDuplicateCrossReferences(leftRootObject);
		removeAllDuplicateCrossReferences(rightRootObject);
	}
	
	@Test
	public void diffAndMergeAllRightToLeft() {
		final MergeMode direction = MergeMode.RIGHT_TO_LEFT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(leftRootObject, rightRootObject, direction));
	}

	@Test
	public void diffAndMergeAllLeftToRight() {
		final MergeMode direction = MergeMode.LEFT_TO_RIGHT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(leftRootObject, rightRootObject, direction));
	}
	
	private ESModelMutatorConfiguration getConfig(EObject root) {
		final ESModelMutatorConfiguration mmc = new ESModelMutatorConfiguration(
			mutateUtil.getEPackages(), root, mutateUtil.getSeed());
		mmc.setAllowDuplicateIDs(false);
		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
		mmc.seteStructuralFeaturesToIgnore(mutateUtil.getEStructuralFeaturesToIgnore());
		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
		mmc.setMinObjectsCount(mutateUtil.getMinObjectsCount());
		mmc.setMutationCount(mutateUtil.getMutationCount());
		return mmc;
	}
	
//	private ESModelMutatorConfiguration copyConfig(ESModelMutatorConfiguration config) {
//		final ESModelMutatorConfiguration mmc = new ESModelMutatorConfiguration(
//			mutateUtil.getEPackages(), root, mutateUtil.getSeed());
//		mmc.setAllowDuplicateIDs(false);
//		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
//		mmc.seteStructuralFeaturesToIgnore(mutateUtil.getEStructuralFeaturesToIgnore());
//		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
//		mmc.setMinObjectsCount(mutateUtil.getMinObjectsCount());
//		return mmc;
//	}
}
