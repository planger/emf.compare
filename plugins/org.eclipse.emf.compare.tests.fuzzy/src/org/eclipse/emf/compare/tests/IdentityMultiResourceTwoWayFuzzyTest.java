package org.eclipse.emf.compare.tests;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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
public class IdentityMultiResourceTwoWayFuzzyTest extends TwoWayFuzzyTest {
	
	@Mutator
	private ESCrossResourceReferencesModelMutator mutator;

	private ESCrossResourceReferencesModelMutator secondMutator;

	private ResourceSet leftSet;

	private ResourceSet rightSet;

	@Before
	public void prepareTwoVersions() {
		leftSet = new ResourceSetImpl();
		rightSet = new ResourceSetImpl();
		copyResourceSet(leftSet, generatedRootObject.eResource().getResourceSet());
		copyResourceSet(rightSet, generatedRootObject.eResource().getResourceSet());
		
		leftRootObject = leftSet.getResources().get(0).getContents().get(0);
		rightRootObject = (EObject)leftSet.getResources().get(0).getContents().get(0);
		
		secondMutator = new ESCrossResourceReferencesModelMutator(getConfig(rightRootObject));
		
		removeAllDuplicateCrossReferences(leftRootObject);
		removeAllDuplicateCrossReferences(rightRootObject);
		assertTrue(EcoreUtil.equals(leftRootObject, rightRootObject));
				
		mutator.mutate(Collections.<EStructuralFeature>emptySet());
		secondMutator.mutate(Collections.<EStructuralFeature>emptySet());
		removeAllDuplicateCrossReferences(leftRootObject);
		removeAllDuplicateCrossReferences(rightRootObject);

	}
	
	public static ResourceSet copyResourceSet(ResourceSet targetSet, ResourceSet sourceSet) {
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		for (Resource resource : sourceSet.getResources()) {
			Resource targetResource = targetSet.createResource(resource.getURI());
			targetResource.getContents().addAll(copier.copyAll(resource.getContents()));
		}
		copier.copyReferences();
		return targetSet;
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

}
