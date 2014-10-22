package org.eclipse.emf.compare.uml2.tests.executionSpecification;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.executionSpecification.data.ExecutionSpecificationInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddBehaviorExecutionSpecificationTest extends AbstractUMLTest {

	private ExecutionSpecificationInputData input = new ExecutionSpecificationInputData();

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 14 differences
		assertEquals(14, differences.size());

		Predicate<? super Diff> addActionExecSpecDescription = null;
		Predicate<? super Diff> addCoveredInStartDescription = null;
		Predicate<? super Diff> addExecutionInStartDescription = null;
		Predicate<? super Diff> addCoveredInFinishDescription = null;
		Predicate<? super Diff> addExecutionInFinishDescription = null;
		Predicate<? super Diff> addCoveredByInLifeline1Description1 = null;
		Predicate<? super Diff> addCoveredByInLifeline1Description2 = null;
		Predicate<? super Diff> addCoveredByInLifeline1Description3 = null;
		Predicate<? super Diff> addFinishInActionExecSpecDescription = null;
		Predicate<? super Diff> addStartInActionExecSpecDescription = null;
		Predicate<? super Diff> addStartDescription = null;
		Predicate<? super Diff> addFinishDescription = null;
		Predicate<? super Diff> addCoveredInActionExecSpecDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addActionExecSpecDescription = removed("model.interaction0.BehaviorExecSpec0"); //$NON-NLS-1$
			addStartDescription = removed("model.interaction0.BehaviorExecSpec0Start0"); //$NON-NLS-1$
			addFinishDescription = removed("model.interaction0.BehaviorExecSpec0Finish0"); //$NON-NLS-1$
			addCoveredInStartDescription = removedFromReference("model.interaction0.BehaviorExecSpec0Start0",
					"covered", "model.interaction0.Lifeline2");
			addExecutionInStartDescription = changedReference("model.interaction0.BehaviorExecSpec0Start0",
					"execution", "model.interaction0.BehaviorExecSpec0", null);
			addCoveredInFinishDescription = removedFromReference(
					"model.interaction0.BehaviorExecSpec0Finish0", "covered", "model.interaction0.Lifeline2");
			addExecutionInFinishDescription = changedReference("model.interaction0.BehaviorExecSpec0Finish0",
					"execution", "model.interaction0.BehaviorExecSpec0", null);
			addFinishInActionExecSpecDescription = changedReference("model.interaction0.BehaviorExecSpec0",
					"finish", "model.interaction0.BehaviorExecSpec0Finish0", null);
			addStartInActionExecSpecDescription = changedReference("model.interaction0.BehaviorExecSpec0",
					"start", "model.interaction0.BehaviorExecSpec0Start0", null);
			addCoveredByInLifeline1Description1 = removedFromReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0Start0");
			addCoveredByInLifeline1Description2 = removedFromReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0Finish0");
			addCoveredByInLifeline1Description3 = removedFromReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0");
			addCoveredInActionExecSpecDescription = removedFromReference(
					"model.interaction0.BehaviorExecSpec0", "covered", "model.interaction0.Lifeline2");
		} else {
			addActionExecSpecDescription = added("model.interaction0.BehaviorExecSpec0"); //$NON-NLS-1$
			addStartDescription = added("model.interaction0.BehaviorExecSpec0Start0"); //$NON-NLS-1$
			addFinishDescription = added("model.interaction0.BehaviorExecSpec0Finish0");
			addCoveredInStartDescription = addedToReference("model.interaction0.BehaviorExecSpec0Start0",
					"covered", "model.interaction0.Lifeline2");
			addExecutionInStartDescription = changedReference("model.interaction0.BehaviorExecSpec0Start0",
					"execution", null, "model.interaction0.BehaviorExecSpec0");
			addCoveredInFinishDescription = addedToReference("model.interaction0.BehaviorExecSpec0Finish0",
					"covered", "model.interaction0.Lifeline2");
			addExecutionInFinishDescription = changedReference("model.interaction0.BehaviorExecSpec0Finish0",
					"execution", null, "model.interaction0.BehaviorExecSpec0");
			addFinishInActionExecSpecDescription = changedReference("model.interaction0.BehaviorExecSpec0",
					"finish", null, "model.interaction0.BehaviorExecSpec0Finish0");
			addStartInActionExecSpecDescription = changedReference("model.interaction0.BehaviorExecSpec0",
					"start", null, "model.interaction0.BehaviorExecSpec0Start0");
			addCoveredByInLifeline1Description1 = addedToReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0Start0");
			addCoveredByInLifeline1Description2 = addedToReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0Finish0");
			addCoveredByInLifeline1Description3 = addedToReference("model.interaction0.Lifeline2",
					"coveredBy", "model.interaction0.BehaviorExecSpec0");
			addCoveredInActionExecSpecDescription = addedToReference("model.interaction0.BehaviorExecSpec0",
					"covered", "model.interaction0.Lifeline2");
		}

		final Diff addActionExecSpec = Iterators.find(differences.iterator(), addActionExecSpecDescription);
		final Diff addCoveredInStart = Iterators.find(differences.iterator(), addCoveredInStartDescription);
		final Diff addExecutionInStart = Iterators.find(differences.iterator(),
				addExecutionInStartDescription);
		final Diff addCoveredInFinish = Iterators.find(differences.iterator(), addCoveredInFinishDescription);
		final Diff addExecutionInFinish = Iterators.find(differences.iterator(),
				addExecutionInFinishDescription);
		final Diff addFinishInActionExecSpec = Iterators.find(differences.iterator(),
				addFinishInActionExecSpecDescription);
		final Diff addStartInActionExecSpec = Iterators.find(differences.iterator(),
				addStartInActionExecSpecDescription);
		final Diff addCoveredByInLifeline1_1 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline1Description1);
		final Diff addCoveredByInLifeline1_2 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline1Description2);
		final Diff addCoveredByInLifeline1_3 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline1Description3);
		final Diff addStart = Iterators.find(differences.iterator(), addStartDescription);
		final Diff addFinish = Iterators.find(differences.iterator(), addFinishDescription);
		final Diff addCoveredInActionExecSpec = Iterators.find(differences.iterator(),
				addCoveredInActionExecSpecDescription);

		assertNotNull(addActionExecSpec);
		assertNotNull(addCoveredInStart);
		assertNotNull(addExecutionInStart);
		assertNotNull(addCoveredInFinish);
		assertNotNull(addExecutionInFinish);
		assertNotNull(addFinishInActionExecSpec);
		assertNotNull(addStartInActionExecSpec);
		assertNotNull(addCoveredByInLifeline1_1);
		assertNotNull(addCoveredByInLifeline1_2);
		assertNotNull(addCoveredByInLifeline1_3);
		assertNotNull(addStart);
		assertNotNull(addFinish);
		assertNotNull(addCoveredInActionExecSpec);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(ExecutionSpecificationChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(ExecutionSpecificationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(ExecutionSpecificationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLMessage);
		assertEquals(13, addUMLMessage.getRefinedBy().size());
		assertTrue(addUMLMessage.getRefinedBy().contains(addFinishInActionExecSpec));
		assertTrue(addUMLMessage.getRefinedBy().contains(addStartInActionExecSpec));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInFinish));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInStart));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInActionExecSpec));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline1_1));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline1_2));
		assertTrue(addUMLMessage.getRefinedBy().contains(addStart));
		assertTrue(addUMLMessage.getRefinedBy().contains(addFinish));
		assertTrue(addUMLMessage.getRefinedBy().contains(addActionExecSpec));
		assertTrue(addUMLMessage.getRefinedBy().contains(addExecutionInStart));
		assertTrue(addUMLMessage.getRefinedBy().contains(addExecutionInFinish));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline1_3));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addActionExecSpec.getRequires().size());

			assertEquals(1, addCoveredInStart.getRequires().size());
			assertTrue(addCoveredInStart.getRequires().contains(addStart));

			assertEquals(2, addExecutionInStart.getRequires().size());
			assertTrue(addExecutionInStart.getRequires().contains(addStart));
			assertTrue(addExecutionInStart.getRequires().contains(addActionExecSpec));

			assertEquals(1, addCoveredInFinish.getRequires().size());
			assertTrue(addCoveredInFinish.getRequires().contains(addFinish));

			assertEquals(2, addExecutionInFinish.getRequires().size());
			assertTrue(addExecutionInFinish.getRequires().contains(addFinish));
			assertTrue(addExecutionInFinish.getRequires().contains(addActionExecSpec));

			assertEquals(2, addFinishInActionExecSpec.getRequires().size());
			assertTrue(addFinishInActionExecSpec.getRequires().contains(addActionExecSpec));
			assertTrue(addFinishInActionExecSpec.getRequires().contains(addFinish));

			assertEquals(2, addStartInActionExecSpec.getRequires().size());
			assertTrue(addStartInActionExecSpec.getRequires().contains(addActionExecSpec));
			assertTrue(addStartInActionExecSpec.getRequires().contains(addStart));

			assertEquals(1, addCoveredByInLifeline1_1.getRequires().size());
			assertTrue(addCoveredByInLifeline1_1.getRequires().contains(addStart));

			assertEquals(1, addCoveredByInLifeline1_2.getRequires().size());
			assertTrue(addCoveredByInLifeline1_2.getRequires().contains(addFinish));

			assertEquals(1, addCoveredByInLifeline1_3.getRequires().size());
			assertTrue(addCoveredByInLifeline1_3.getRequires().contains(addActionExecSpec));

			assertEquals(0, addStart.getRequires().size());

			assertEquals(0, addFinish.getRequires().size());

			assertEquals(1, addCoveredInActionExecSpec.getRequires().size());
			assertTrue(addCoveredInActionExecSpec.getRequires().contains(addActionExecSpec));

		} else {
			assertEquals(6, addActionExecSpec.getRequires().size());
			assertTrue(addActionExecSpec.getRequires().contains(addFinishInActionExecSpec));
			assertTrue(addActionExecSpec.getRequires().contains(addStartInActionExecSpec));
			assertTrue(addActionExecSpec.getRequires().contains(addCoveredInActionExecSpec));
			assertTrue(addActionExecSpec.getRequires().contains(addExecutionInFinish));
			assertTrue(addActionExecSpec.getRequires().contains(addExecutionInStart));
			assertTrue(addActionExecSpec.getRequires().contains(addCoveredByInLifeline1_3));

			assertEquals(0, addCoveredInStart.getRequires().size());

			assertEquals(0, addExecutionInStart.getRequires().size());

			assertEquals(0, addCoveredInFinish.getRequires().size());

			assertEquals(0, addExecutionInFinish.getRequires().size());

			assertEquals(0, addFinishInActionExecSpec.getRequires().size());

			assertEquals(0, addStartInActionExecSpec.getRequires().size());

			assertEquals(0, addCoveredByInLifeline1_1.getRequires().size());

			assertEquals(0, addCoveredByInLifeline1_2.getRequires().size());

			assertEquals(0, addCoveredByInLifeline1_3.getRequires().size());

			assertEquals(4, addStart.getRequires().size());
			assertTrue(addStart.getRequires().contains(addStartInActionExecSpec));
			assertTrue(addStart.getRequires().contains(addCoveredByInLifeline1_1));
			assertTrue(addStart.getRequires().contains(addCoveredInStart));
			assertTrue(addStart.getRequires().contains(addExecutionInStart));

			assertEquals(4, addFinish.getRequires().size());
			assertTrue(addFinish.getRequires().contains(addFinishInActionExecSpec));
			assertTrue(addFinish.getRequires().contains(addCoveredByInLifeline1_2));
			assertTrue(addFinish.getRequires().contains(addCoveredInFinish));
			assertTrue(addFinish.getRequires().contains(addExecutionInFinish));

			assertEquals(0, addCoveredInActionExecSpec.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(3, comparison.getEquivalences().size());

		assertNotNull(addCoveredInStart.getEquivalence());
		assertEquals(2, addCoveredInStart.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInStart.getEquivalence().getDifferences().contains(addCoveredInStart));
		assertTrue(addCoveredInStart.getEquivalence().getDifferences().contains(addCoveredByInLifeline1_1));

		assertNotNull(addCoveredInFinish.getEquivalence());
		assertEquals(2, addCoveredInFinish.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInFinish.getEquivalence().getDifferences().contains(addCoveredInFinish));
		assertTrue(addCoveredInFinish.getEquivalence().getDifferences().contains(addCoveredByInLifeline1_2));

		assertNotNull(addCoveredInActionExecSpec.getEquivalence());
		assertEquals(2, addCoveredInActionExecSpec.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInActionExecSpec.getEquivalence().getDifferences().contains(
				addCoveredInActionExecSpec));
		assertTrue(addCoveredInActionExecSpec.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline1_3));

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
