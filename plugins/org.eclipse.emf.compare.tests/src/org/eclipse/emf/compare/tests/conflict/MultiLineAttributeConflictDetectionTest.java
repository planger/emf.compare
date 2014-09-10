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
package org.eclipse.emf.compare.tests.conflict;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValuedAttribute;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.compare.tests.nodes.impl.NodesFactoryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

/**
 * Tests the conflict detection of concurrent changes of multi-line string attributes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class MultiLineAttributeConflictDetectionTest {

	private static final String NL = "\n";

	@Test
	public void multiLineAttributeChangeRightSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final Comparison comparison = compare(origin, left, right);
		assertNoConflict(comparison);
	}

	@Test
	public void multiLineAttributeChangeLeftSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final Comparison comparison = compare(origin, left, right);
		assertNoConflict(comparison);
	}

	@Test
	public void conflictingMultiLineAttributeChange_UpdateSameLine() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it??" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		compareAndAssertConflictInBothDirections(origin, left, right);
	}

	@Test
	public void conflictingMultiLineAttributeChange_DeleteUpdateSameLine() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "" // removed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		compareAndAssertConflictInBothDirections(origin, left, right);
	}

	@Test
	public void mergableMultiLineAttributeChangeOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "" // removed
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "" // removed
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Maec."; // changed

		compareAndAssertNoConflictInBothDirections(origin, left, right);
	}

	// TODO @Test
	public void conflictingMultiLineMultiValuedAttributeChange() {
		fail("implement");
	}

	// TODO @Test
	public void mergableMultiLineMultiValuedAttributeChangeRight() {
		fail("implement");
	}

	// TODO @Test
	public void mergableMultiLineMultiValuedAttributeChangeLeft() {
		fail("implement");
	}

	@Test
	public void conflictingSingleLineAttributeChange() throws IOException {
		final Comparison comparison = compare("A", "B", "C");
		assertOneConflict(comparison);
	}

	@Test
	public void singleLineAttributeChangeRightSide() throws IOException {
		final Comparison comparison = compare("A", "A", "C");
		assertNoConflict(comparison);
	}

	@Test
	public void singleLineAttributeChangeLeftSide() throws IOException {
		final Comparison comparison = compare("A", "B", "A");
		assertNoConflict(comparison);
	}

	private void compareAndAssertNoConflictInBothDirections(String origin, String left, String right)
			throws IOException {
		final Comparison comparisonLeftRight = compare(origin, left, right);
		assertNoConflict(comparisonLeftRight);

		final Comparison comparisonRightLeft = compare(origin, right, left);
		assertNoConflict(comparisonRightLeft);
	}

	private void compareAndAssertConflictInBothDirections(final String origin, final String left,
			final String right) throws IOException {
		final Comparison comparisonLeftRight = compare(origin, left, right);
		assertOneConflict(comparisonLeftRight);

		final Comparison comparisonRightLeft = compare(origin, right, left);
		assertOneConflict(comparisonRightLeft);
	}

	private Comparison compare(String[] origin, String[] left, String[] right) throws IOException {
		final ThreeWayMergeScenario scenario = new ThreeWayMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		return comparison;
	}

	private Comparison compare(String origin, String left, String right) throws IOException {
		final ThreeWayMergeScenario scenario = new ThreeWayMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		return comparison;
	}

	private Comparison compare(final ThreeWayMergeScenario scenario) {
		final Resource origin = scenario.getOriginResource();
		final Resource left = scenario.getLeftResource();
		final Resource right = scenario.getRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		return comparison;
	}

	private void assertOneConflict(final Comparison comparison) {
		final List<Conflict> conflicts = comparison.getConflicts();
		assertEquals(1, conflicts.size());
	}

	private void assertNoConflict(final Comparison comparison) {
		final List<Conflict> conflicts = comparison.getConflicts();
		assertEquals(0, conflicts.size());
	}

	private class ThreeWayMergeScenario {

		private static final String NODE_NAME = "Node";

		private final NodesFactoryImpl nodesFactory = new NodesFactoryImpl();

		private final Resource.Factory resourceFactory = new XMIResourceFactoryImpl();

		private boolean multiValued;

		private final Resource originResource;

		private final Resource leftResource;

		private final Resource rightResource;

		public ThreeWayMergeScenario(String originValue, String leftValue, String rightValue)
				throws IOException {
			this(new String[] {originValue }, new String[] {leftValue }, new String[] {rightValue });
		}

		public ThreeWayMergeScenario(String[] originValues, String[] leftValues, String[] rightValues)
				throws IOException {
			multiValued = isAnyMultiValued(originValues, leftValues, rightValues);
			originResource = createNodeWithAttributeResource("origin", originValues);
			leftResource = createNodeWithAttributeResource("left", leftValues);
			rightResource = createNodeWithAttributeResource("right", rightValues);
		}

		private boolean isAnyMultiValued(String[] originValue, String[] leftValue, String[] rightValue) {
			return originValue.length > 0 || leftValue.length > 0 || rightValue.length > 0;
		}

		public Resource createNodeWithAttributeResource(String fileName, String[] attributeValues) {
			URI fakeUri = URI.createFileURI(fileName + ".nodes");
			Resource resource = resourceFactory.createResource(fakeUri);

			Node rootNode = nodesFactory.createNode();
			Node node = createNodeWithAttributeValue(attributeValues);
			node.setName(NODE_NAME);

			rootNode.getContainmentRef1().add(node);
			resource.getContents().add(rootNode);

			return resource;
		}

		private Node createNodeWithAttributeValue(String[] attributeValues) {
			final Node node;
			if (multiValued) {
				node = createMultiValuesAttributeNode(attributeValues);
			} else {
				final String firstValue = getFirstValueOrEmpty(attributeValues);
				node = createSingleValueAttributeNode(firstValue);
			}
			return node;
		}

		private Node createMultiValuesAttributeNode(String[] attributeValues) {
			NodeMultiValuedAttribute node = nodesFactory.createNodeMultiValuedAttribute();
			for (String value : attributeValues) {
				node.getMultiValuedAttribute().add(value);
			}
			return node;
		}

		private String getFirstValueOrEmpty(String[] values) {
			final String value;
			if (values.length > 0) {
				value = values[0];
			} else {
				value = "";
			}
			return value;
		}

		private Node createSingleValueAttributeNode(String attributeValue) {
			NodeSingleValueAttribute node = nodesFactory.createNodeSingleValueAttribute();
			node.setSingleValuedAttribute(attributeValue);
			return node;
		}

		public Resource getOriginResource() {
			return originResource;
		}

		public Resource getLeftResource() {
			return leftResource;
		}

		public Resource getRightResource() {
			return rightResource;
		}

	}
}
