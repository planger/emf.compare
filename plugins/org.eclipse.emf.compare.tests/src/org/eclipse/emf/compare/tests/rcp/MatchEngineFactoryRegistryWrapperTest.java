/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.rcp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.match.MatchEngineFactoryRegistryWrapper;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.rcp.data.EcoreInputData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Test class for {@link MatchEngineFactoryRegistryWrapper}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings({"restriction", "nls" })
public class MatchEngineFactoryRegistryWrapperTest {

	private MatchEngineFactoryRegistryWrapper registryWrapper;

	private IEclipsePreferences preferences;

	/**
	 * Creates a comparison scope from Ecore model.
	 * 
	 * @return {@link IComparisonScope}
	 * @throws IOException
	 */
	private IComparisonScope createComparisonScope() throws IOException {
		EcoreInputData ecoreData = new EcoreInputData();
		return new DefaultComparisonScope(ecoreData.getLeft(), ecoreData.getRight(), ecoreData.getOrigin());
	}

	// For Helios compatibility.
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws BackingStoreException {
		ItemRegistry<Factory> registry = new ItemRegistry<IMatchEngine.Factory>();
		// Mock preference node.
		preferences = new InstanceScope()
				.getNode("org.eclipse.emf.compare.tests.rcp.MatchEngineFactoryRegistryWrapperTest");
		preferences.clear();
		registryWrapper = new MatchEngineFactoryRegistryWrapper(registry, preferences);
	}

	@After
	public void tearDown() throws BackingStoreException {
		preferences.clear();
	}

	/**
	 * Nominal use case: Adds {@link IMatchEngine.Factory} in the registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAdd() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);
		IComparisonScope createComparisonScope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(createComparisonScope), factory);
		assertEquals(registryWrapper.getMatchEngineFactories(createComparisonScope).size(), 1);
	}

	/**
	 * Adds a factory with no ranking set into the registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddFactoryWithNoRanking() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		registryWrapper.add(factory);
		IMatchEngine.Factory factory2 = new MockMatchEngineFactory2();
		factory2.setRanking(10);
		registryWrapper.add(factory2);
		IComparisonScope createComparisonScope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(createComparisonScope), factory2);
	}

	/**
	 * Adds null to the registry.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddNull() {
		registryWrapper.add(null);
	}

	/**
	 * Adds two factories with identical id in the registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddSameID() throws IOException {
		MockMatchEngineFactory1 factory1 = new MockMatchEngineFactory1();
		factory1.setRanking(10);
		registryWrapper.add(factory1);
		MockMatchEngineFactory1 factory2 = new MockMatchEngineFactory1();
		Factory oldValue = registryWrapper.add(factory2);
		assertTrue(oldValue == factory1);
		IComparisonScope createComparisonScope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(createComparisonScope), factory2);
	}

	/**
	 * Nominal use case: Gets the highest ranking match engine factory.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testHighestRankingMatchEngineFactory() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);
		IMatchEngine.Factory factory2 = new MockMatchEngineFactory2();
		factory2.setRanking(20);
		registryWrapper.add(factory2);
		IMatchEngine.Factory factory3 = new MockMatchEngineFactory3();
		factory3.setRanking(30);
		registryWrapper.add(factory3);

		IComparisonScope scope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(scope), factory3);
		assertTrue(registryWrapper.getMatchEngineFactories(scope).containsAll(
				Lists.newArrayList(factory, factory2, factory3)));
	}

	/**
	 * Tries to get the highest ranking match engine factory from an empty registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testHighestRankingMatchEngineFactoryEmpty() throws IOException {
		IComparisonScope scope = createComparisonScope();
		assertEquals(registryWrapper.getHighestRankingMatchEngineFactory(scope), null);
		assertTrue(registryWrapper.getMatchEngineFactories(scope).isEmpty());
	}

	/**
	 * Nominal use case: Gets the highest ranking match engine factory with more complex registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testHighestRankingMatchEngineFactoryWithDisabledFactories() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);

		IMatchEngine.Factory factory2 = new MockMatchEngineFactory2();
		factory2.setRanking(20);
		registryWrapper.add(factory2);

		IMatchEngine.Factory factory3 = new MockMatchEngineFactory3();
		factory3.setRanking(30);
		registryWrapper.add(factory3);

		IMatchEngine.Factory disabledFactory1 = new MockDisabledMatchEngineFactory1();
		disabledFactory1.setRanking(40);
		registryWrapper.add(disabledFactory1);

		IMatchEngine.Factory disabledFactory2 = new MockDisabledMatchEngineFactory2();
		disabledFactory2.setRanking(50);
		registryWrapper.add(disabledFactory2);

		IMatchEngine.Factory disabledFactory3 = new MockDisabledMatchEngineFactory3();
		disabledFactory2.setRanking(60);
		registryWrapper.add(disabledFactory3);

		IComparisonScope scope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(scope), factory3);
		assertTrue(registryWrapper.getMatchEngineFactories(scope).containsAll(
				Lists.newArrayList(factory, factory2, factory3)));

	}

	/**
	 * Nominal use case: Removes a factory from the registry.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRemove() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);

		Factory oldValue = registryWrapper.remove(MockMatchEngineFactory1.class.getName());
		assertSame(oldValue, factory);
		assertTrue(registryWrapper.getMatchEngineFactories(createComparisonScope()).isEmpty());
	}

	/**
	 * Tries to remove null from the registry.
	 */
	@Test(expected = NullPointerException.class)
	public void testRemoveNull() {
		registryWrapper.remove(null);
	}

	/**
	 * Tries to remove a factory from the registry with an incoherent id.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRemoveWrongID() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);

		Factory oldValue = registryWrapper.remove("IncohereId");
		assertNull(oldValue);
		assertTrue(!registryWrapper.getMatchEngineFactories(createComparisonScope()).isEmpty());
	}

	/**
	 * Nominal use case: Disables Match Engine factory from preferences. Checks that the registry to do use
	 * them anymore.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDisablingMatchEngine() throws IOException {
		IMatchEngine.Factory factory = new MockMatchEngineFactory1();
		factory.setRanking(10);
		registryWrapper.add(factory);

		IMatchEngine.Factory factory2 = new MockMatchEngineFactory2();
		factory2.setRanking(20);
		registryWrapper.add(factory2);

		IMatchEngine.Factory factory3 = new MockMatchEngineFactory3();
		factory3.setRanking(30);
		registryWrapper.add(factory3);

		IComparisonScope scope = createComparisonScope();
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(scope), factory3);

		disableEngine(preferences, EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, Collections
				.singleton(factory3.getClass().getName()));
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(scope), factory2);
		assertTrue(!registryWrapper.getMatchEngineFactories(scope).contains(factory3));

		disableEngine(preferences, EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, Lists.newArrayList(
				factory3.getClass().getName(), factory2.getClass().getName()));
		assertSame(registryWrapper.getHighestRankingMatchEngineFactory(scope), factory);
		assertTrue(!registryWrapper.getMatchEngineFactories(scope).contains(factory2));

	}

	/**
	 * Disables engine in preferences.
	 * 
	 * @param preference
	 *            {@link IEclipsePreferences}
	 * @param key
	 * @param toDisable
	 *            {@link Collection} of {@link IItemDescriptor} to disable.
	 */
	private void disableEngine(IEclipsePreferences preference, String key, Collection<String> toDisable) {
		if (toDisable != null && !toDisable.isEmpty()) {
			String newPreferenceValue = Joiner.on(ItemUtil.PREFERENCE_DELIMITER).join(toDisable);
			preference.put(key, newPreferenceValue);
		} else {
			preference.remove(key);
		}
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that does not handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockDisabledMatchEngineFactory1 extends MatchEngineFactoryImpl {
		@Override
		public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
			return false;
		}
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that does not handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockDisabledMatchEngineFactory2 extends MockDisabledMatchEngineFactory1 {
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that does not handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockDisabledMatchEngineFactory3 extends MockDisabledMatchEngineFactory1 {
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that can handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockMatchEngineFactory1 extends MatchEngineFactoryImpl {
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that can handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockMatchEngineFactory2 extends MatchEngineFactoryImpl {
	}

	/**
	 * Mock {@link IMatchEngine.Factory} that can handle any {@link IComparisonScope}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockMatchEngineFactory3 extends MatchEngineFactoryImpl {
	}
}
