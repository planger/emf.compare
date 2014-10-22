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
package org.eclipse.emf.compare.tests.scope;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.generics.GenericsMatchInputData;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * This class will allow us to test the behavior of the {@link DefaultComparisonScope}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultComparisonScopeTest {
	@Test
	public void testGetRoots() throws IOException {
		final IComparisonScope nullScope = createNullScope();
		assertNull(nullScope.getLeft());
		assertNull(nullScope.getRight());
		assertNull(nullScope.getOrigin());

		// These are only getters, they should return us the unchanged object
		final IdentifierMatchInputData mockModel = new IdentifierMatchInputData();
		final Resource leftResource = mockModel.getExtlibraryLeft();
		final Resource rightResource = mockModel.getExtlibraryRight();
		final Resource originResource = mockModel.getExtlibraryOrigin();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(rightResource, false);
		final Iterator<EObject> originContent = EcoreUtil.getAllProperContents(originResource, false);

		while (leftContent.hasNext() && rightContent.hasNext() && originContent.hasNext()) {
			final EObject left = leftContent.next();
			final EObject right = rightContent.next();
			final EObject origin = originContent.next();
			final IComparisonScope eObjectScope = new DefaultComparisonScope(left, right, origin);

			assertSame(left, eObjectScope.getLeft());
			assertSame(right, eObjectScope.getRight());
			assertSame(origin, eObjectScope.getOrigin());
		}

		final IComparisonScope resourceScope = new DefaultComparisonScope(leftResource, rightResource,
				originResource);
		assertSame(leftResource, resourceScope.getLeft());
		assertSame(rightResource, resourceScope.getRight());
		assertSame(originResource, resourceScope.getOrigin());

		final ResourceSet leftRS = newResourceSet(leftResource);
		final ResourceSet rightRS = newResourceSet(rightResource);
		final ResourceSet originRS = newResourceSet(originResource);

		final IComparisonScope resourceSetScope = new DefaultComparisonScope(leftRS, rightRS, originRS);
		assertSame(leftRS, resourceSetScope.getLeft());
		assertSame(rightRS, resourceSetScope.getRight());
		assertSame(originRS, resourceSetScope.getOrigin());
	}

	@Test
	public void testGetChildrenForNull() {
		final IComparisonScope nullScope = createNullScope();

		assertFalse(nullScope.getChildren((EObject)null).hasNext());
		assertFalse(nullScope.getCoveredEObjects((Resource)null).hasNext());
		assertFalse(nullScope.getCoveredResources((ResourceSet)null).hasNext());
	}

	@Test
	public void testGetEObjectCHildren() throws IOException {
		final IComparisonScope resourceScope = createResourceScope();

		assertTrue(resourceScope.getLeft() instanceof Resource);
		assertTrue(resourceScope.getRight() instanceof Resource);
		assertTrue(resourceScope.getOrigin() instanceof Resource);

		final Resource leftResource = (Resource)resourceScope.getLeft();
		final Resource rightResource = (Resource)resourceScope.getRight();
		final Resource originResource = (Resource)resourceScope.getOrigin();

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(rightResource, false);
		final Iterator<EObject> originContent = EcoreUtil.getAllProperContents(originResource, false);
		final Iterator<EObject> allEObjects = Iterators.concat(leftContent, rightContent, originContent);

		boolean empty = true;
		while (allEObjects.hasNext()) {
			empty = false;
			final EObject root = allEObjects.next();

			final Iterator<? extends EObject> scopeChildren = resourceScope.getChildren(root);
			final List<EObject> children = Lists.newArrayList(Iterators.filter(EcoreUtil
					.getAllProperContents(root, false), EObject.class));

			while (scopeChildren.hasNext()) {
				assertTrue(children.remove(scopeChildren.next()));
			}
			// We want the default scope to avoid all EGenericTypes
			for (EObject outOfScope : children) {
				assertTrue(outOfScope instanceof EGenericType);
			}
		}
		assertFalse(empty);
	}

	@Test
	public void testGetResourceChildren() throws IOException {
		final IComparisonScope resourceScope = createResourceScope();

		assertTrue(resourceScope.getLeft() instanceof Resource);
		assertTrue(resourceScope.getRight() instanceof Resource);
		assertTrue(resourceScope.getOrigin() instanceof Resource);

		final Resource leftResource = (Resource)resourceScope.getLeft();
		final Resource rightResource = (Resource)resourceScope.getRight();
		final Resource originResource = (Resource)resourceScope.getOrigin();

		final Iterator<? extends EObject> scopeLeftChildren = resourceScope.getCoveredEObjects(leftResource);
		final List<EObject> leftChildren = Lists.newArrayList(Iterators.filter(EcoreUtil
				.getAllProperContents(leftResource, false), EObject.class));
		while (scopeLeftChildren.hasNext()) {
			assertTrue(leftChildren.remove(scopeLeftChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : leftChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}

		final Iterator<? extends EObject> scopeRightChildren = resourceScope
				.getCoveredEObjects(rightResource);
		final List<EObject> rightChildren = Lists.newArrayList(Iterators.filter(EcoreUtil
				.getAllProperContents(rightResource, false), EObject.class));
		while (scopeRightChildren.hasNext()) {
			assertTrue(rightChildren.remove(scopeRightChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : rightChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}

		final Iterator<? extends EObject> scopeOriginChildren = resourceScope
				.getCoveredEObjects(originResource);
		final List<EObject> originChildren = Lists.newArrayList(Iterators.filter(EcoreUtil
				.getAllProperContents(originResource, false), EObject.class));
		while (scopeOriginChildren.hasNext()) {
			assertTrue(originChildren.remove(scopeOriginChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : originChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}
	}

	@Test
	public void testGetResourceSetChildren() throws IOException {
		final IComparisonScope resourceScope = createResourceSetScope();

		assertTrue(resourceScope.getLeft() instanceof ResourceSet);
		assertTrue(resourceScope.getRight() instanceof ResourceSet);
		assertTrue(resourceScope.getOrigin() instanceof ResourceSet);

		final ResourceSet leftResourceSet = (ResourceSet)resourceScope.getLeft();
		final ResourceSet rightResourceSet = (ResourceSet)resourceScope.getRight();
		final ResourceSet originResourceSet = (ResourceSet)resourceScope.getOrigin();

		final Iterator<? extends Resource> scopeLeftChildren = resourceScope
				.getCoveredResources(leftResourceSet);
		final List<Resource> leftChildren = Lists.newArrayList(leftResourceSet.getResources());
		while (scopeLeftChildren.hasNext()) {
			Resource child = scopeLeftChildren.next();

			assertTrue(leftChildren.remove(child));
		}
		assertTrue(leftChildren.isEmpty());

		final Iterator<? extends Resource> scopeRightChildren = resourceScope
				.getCoveredResources(rightResourceSet);
		final List<Resource> rightChildren = Lists.newArrayList(rightResourceSet.getResources());
		while (scopeRightChildren.hasNext()) {
			Resource child = scopeRightChildren.next();

			assertTrue(rightChildren.remove(child));
		}
		assertTrue(rightChildren.isEmpty());

		final Iterator<? extends Resource> scopeOriginChildren = resourceScope
				.getCoveredResources(originResourceSet);
		final List<Resource> originChildren = Lists.newArrayList(originResourceSet.getResources());
		while (scopeOriginChildren.hasNext()) {
			Resource child = scopeOriginChildren.next();

			assertTrue(originChildren.remove(child));
		}
		assertTrue(originChildren.isEmpty());
	}

	@Test
	public void testGetRootsWithGenerics() throws IOException {
		// These are only getters, they should return us the unchanged object
		final GenericsMatchInputData mockModel = new GenericsMatchInputData();
		final Resource leftResource = mockModel.getLeft();
		final Resource rightResource = mockModel.getRight();

		assertNotNull(leftResource);
		assertNotNull(rightResource);

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(rightResource, false);

		while (leftContent.hasNext() && rightContent.hasNext()) {
			final EObject left = leftContent.next();
			final EObject right = rightContent.next();
			final IComparisonScope eObjectScope = new DefaultComparisonScope(left, right, null);

			assertSame(left, eObjectScope.getLeft());
			assertSame(right, eObjectScope.getRight());
		}

		final IComparisonScope resourceScope = new DefaultComparisonScope(leftResource, rightResource, null);
		assertSame(leftResource, resourceScope.getLeft());
		assertSame(rightResource, resourceScope.getRight());

		final ResourceSet leftRS = newResourceSet(leftResource);
		final ResourceSet rightRS = newResourceSet(rightResource);

		final IComparisonScope resourceSetScope = new DefaultComparisonScope(leftRS, rightRS, null);
		assertSame(leftRS, resourceSetScope.getLeft());
		assertSame(rightRS, resourceSetScope.getRight());

	}

	@Test
	public void testGetEObjectChildrenWithGenerics() throws IOException {
		// These are only getters, they should return us the unchanged object
		final GenericsMatchInputData mockModel = new GenericsMatchInputData();

		final Resource leftResource = mockModel.getLeft();
		final Resource rightResource = mockModel.getRight();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		final IComparisonScope resourceScope = new DefaultComparisonScope(leftResource, rightResource, null);

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(rightResource, false);
		final Iterator<EObject> allEObjects = Iterators.concat(leftContent, rightContent);

		assertTrue(allEObjects.hasNext());
		while (allEObjects.hasNext()) {
			final EObject root = allEObjects.next();

			final Iterator<? extends EObject> scopeChildren = resourceScope.getChildren(root);
			final List<EObject> children = Lists.newArrayList(Iterators.filter(EcoreUtil
					.getAllProperContents(root, false), EObject.class));

			while (scopeChildren.hasNext()) {
				assertTrue(children.remove(scopeChildren.next()));
			}
			// We want the default scope to avoid all EGenericTypes without parameters
			for (EObject outOfScope : children) {
				assertTrue(outOfScope instanceof EGenericType);
				assertTrue(EMFComparePredicates.IS_EGENERIC_TYPE_WITHOUT_PARAMETERS.apply(outOfScope));
			}
		}
	}

	private static IComparisonScope createNullScope() {
		return new DefaultComparisonScope(null, null, null);
	}

	private static IComparisonScope createResourceScope() throws IOException {
		final IdentifierMatchInputData mockModel = new IdentifierMatchInputData();
		final Resource leftResource = mockModel.getExtlibraryLeft();
		final Resource rightResource = mockModel.getExtlibraryRight();
		final Resource originResource = mockModel.getExtlibraryOrigin();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		return new DefaultComparisonScope(leftResource, rightResource, originResource);
	}

	private static IComparisonScope createResourceSetScope() throws IOException {
		final IdentifierMatchInputData mockModel = new IdentifierMatchInputData();
		final Resource leftResource = mockModel.getExtlibraryLeft();
		final Resource rightResource = mockModel.getExtlibraryRight();
		final Resource originResource = mockModel.getExtlibraryOrigin();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		final ResourceSet leftRS = newResourceSet(leftResource);
		final ResourceSet rightRS = newResourceSet(rightResource);
		final ResourceSet originRS = newResourceSet(originResource);

		return new DefaultComparisonScope(leftRS, rightRS, originRS);
	}

	private static ResourceSet newResourceSet(Resource... resources) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		for (int i = 0; i < resources.length; i++) {
			resourceSet.getResources().add(resources[i]);
		}
		return resourceSet;
	}
}
