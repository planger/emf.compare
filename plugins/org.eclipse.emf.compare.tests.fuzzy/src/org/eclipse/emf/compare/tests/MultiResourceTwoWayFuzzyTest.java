package org.eclipse.emf.compare.tests;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.emfstore.fuzzy.emf.ESEMFDataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.DataProvider;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.Annotations.Mutator;
import org.eclipse.emf.emfstore.fuzzy.emf.junit.ESFuzzyRunner;
import org.eclipse.emf.emfstore.modelmutator.ESCrossResourceReferencesModelMutator;
import org.eclipse.emf.emfstore.modelmutator.ESModelMutatorConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ESFuzzyRunner.class)
@DataProvider(ESEMFDataProvider.class)
public class MultiResourceTwoWayFuzzyTest extends TwoWayFuzzyTest {
	
	@Mutator
	private ESCrossResourceReferencesModelMutator mutator;

	private ESCrossResourceReferencesModelMutator secondMutator;

	private ResourceSet leftSet;

	private ResourceSet rightSet;

	@Before
	public void prepareTwoVersions() {
		this.leftRootObject = EcoreUtil.copy(generatedRootObject);
		this.rightRootObject = EcoreUtil.copy(generatedRootObject);
		
		secondMutator = new ESCrossResourceReferencesModelMutator(getConfig(rightRootObject));
		
		removeAllDuplicateCrossReferences(leftRootObject);
		removeAllDuplicateCrossReferences(rightRootObject);
		assertTrue(EcoreUtil.equals(leftRootObject, rightRootObject));
				
		secondMutator.mutate(Collections.<EStructuralFeature>emptySet());
		removeAllDuplicateCrossReferences(rightRootObject);
		
		leftSet = mutator.getResourceSet();
		rightSet = secondMutator.getResourceSet();
	}
	
	@Test
	public void diffAndMergeAllRightToLeft() {
		final MergeDirection direction = MergeDirection.RIGHT_TO_LEFT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(leftSet, rightSet, direction));
	}

	@Test
	public void diffAndMergeAllLeftToRight() {
		final MergeDirection direction = MergeDirection.LEFT_TO_RIGHT;
		performBatchMergeAndEqualityCheckTest(new TwoWayMergeData(leftSet, rightSet, direction));
	}
	
	private ESModelMutatorConfiguration getConfig(EObject root) {
		final ESModelMutatorConfiguration mmc = new ESModelMutatorConfiguration(
			mutateUtil.getEPackages(), root, mutateUtil.getSeed());
		mmc.setAllowDuplicateIDs(false);
		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
		mmc.seteStructuralFeaturesToIgnore(mutateUtil.getEStructuralFeaturesToIgnore());
		mmc.seteClassesToIgnore(mutateUtil.getEClassesToIgnore());
		mmc.setMinObjectsCount(mutateUtil.getMinObjectsCount());
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
