package org.eclipse.emf.compare.uml2.tests.message;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.MessageChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.message.data.MessageInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddMessageTest extends AbstractUMLTest {

	private MessageInputData input = new MessageInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right);
		testAB2(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(right, left);
		testAB2(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, right);
		testAB2(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, left);
		testAB2(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right);
		testAB3(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(right, left);
		testAB3(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA30UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right, right);
		testAB3(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA31UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right, left);
		testAB3(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testA40UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA41UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA40UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA41UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 12 differences
		assertEquals(12, differences.size());

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Send0Description = null;
		Predicate<? super Diff> addMessageInMessage0Send0Description = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline1Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addSendEventInMessageDescription = null;
		Predicate<? super Diff> addMessageSendDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageSendDescription = removed("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Recv0"); //$NON-NLS-1$
			addCoveredInMessage0Send0Description = removedFromReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Send0Description = changedReference("model.interaction0.Message0Send0",
					"message", "model.interaction0.Message0", null);
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", "model.interaction0.Message0Recv0", null);
			addSendEventInMessageDescription = changedReference("model.interaction0.Message0", "sendEvent",
					"model.interaction0.Message0Send0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Send0");
			addCoveredByInLifeline1Description = removedFromReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Recv0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageSendDescription = added("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Recv0");
			addCoveredInMessage0Send0Description = addedToReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Send0Description = changedReference("model.interaction0.Message0Send0",
					"message", null, "model.interaction0.Message0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", null, "model.interaction0.Message0Recv0");
			addSendEventInMessageDescription = changedReference("model.interaction0.Message0", "sendEvent",
					null, "model.interaction0.Message0Send0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Send0");
			addCoveredByInLifeline1Description = addedToReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Recv0");
		}

		final Diff addMessage = Iterators.find(differences.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Send0 = Iterators.find(differences.iterator(),
				addCoveredInMessage0Send0Description);
		final Diff addMessageInMessage0Send0 = Iterators.find(differences.iterator(),
				addMessageInMessage0Send0Description);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(differences.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(differences.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(differences.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addSendEventInMessage = Iterators.find(differences.iterator(),
				addSendEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addCoveredByInLifeline1 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline1Description);
		final Diff addMessageSend = Iterators.find(differences.iterator(), addMessageSendDescription);
		final Diff addMessageRecv = Iterators.find(differences.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Send0);
		assertNotNull(addMessageInMessage0Send0);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addSendEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addCoveredByInLifeline1);
		assertNotNull(addMessageSend);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(differences.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
		} else {
			addUMLMessage = Iterators.find(differences.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLMessage);
		assertEquals(11, addUMLMessage.getRefinedBy().size());
		assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addSendEventInMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Send0));

		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageSend));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageRecv));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline1));

		assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageInMessage0Send0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageInMessage0Recv0));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addMessage.getRequires().size());

			assertEquals(1, addCoveredInMessage0Send0.getRequires().size());
			assertTrue(addCoveredInMessage0Send0.getRequires().contains(addMessageSend));

			assertEquals(2, addMessageInMessage0Send0.getRequires().size());
			assertTrue(addMessageInMessage0Send0.getRequires().contains(addMessageSend));
			assertTrue(addMessageInMessage0Send0.getRequires().contains(addMessage));

			assertEquals(1, addCoveredInMessage0Recv0.getRequires().size());
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertEquals(2, addMessageInMessage0Recv0.getRequires().size());
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertEquals(2, addReceiveEventInMessage.getRequires().size());
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertEquals(2, addSendEventInMessage.getRequires().size());
			assertTrue(addSendEventInMessage.getRequires().contains(addMessage));
			assertTrue(addSendEventInMessage.getRequires().contains(addMessageSend));

			assertEquals(1, addCoveredByInLifeline0.getRequires().size());
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageSend));

			assertEquals(1, addCoveredByInLifeline1.getRequires().size());
			assertTrue(addCoveredByInLifeline1.getRequires().contains(addMessageRecv));

			assertEquals(0, addMessageSend.getRequires().size());

			assertEquals(0, addMessageRecv.getRequires().size());

		} else {
			assertEquals(4, addMessage.getRequires().size());
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addSendEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Send0));

			assertEquals(0, addCoveredInMessage0Send0.getRequires().size());

			assertEquals(0, addMessageInMessage0Send0.getRequires().size());

			assertEquals(0, addCoveredInMessage0Recv0.getRequires().size());

			assertEquals(0, addMessageInMessage0Recv0.getRequires().size());

			assertEquals(0, addReceiveEventInMessage.getRequires().size());

			assertEquals(0, addSendEventInMessage.getRequires().size());

			assertEquals(0, addCoveredByInLifeline0.getRequires().size());

			assertEquals(0, addCoveredByInLifeline1.getRequires().size());

			assertEquals(4, addMessageSend.getRequires().size());
			assertTrue(addMessageSend.getRequires().contains(addSendEventInMessage));
			assertTrue(addMessageSend.getRequires().contains(addCoveredByInLifeline0));
			assertTrue(addMessageSend.getRequires().contains(addCoveredInMessage0Send0));
			assertTrue(addMessageSend.getRequires().contains(addMessageInMessage0Send0));

			assertEquals(4, addMessageRecv.getRequires().size());
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline1));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
		}

		// CHECK EQUIVALENCE
		assertEquals(2, comparison.getEquivalences().size());

		assertNotNull(addCoveredInMessage0Send0.getEquivalence());
		assertEquals(2, addCoveredInMessage0Send0.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInMessage0Send0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Send0));
		assertTrue(addCoveredInMessage0Send0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertEquals(2, addCoveredInMessage0Recv0.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline1));

		testIntersections(comparison);

	}

	private void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		final Iterator<Diff> itUseFullDiffs = Iterators.filter(differences.iterator(),
				not(ofKind(DifferenceKind.MOVE)));
		final List<Diff> useFullDiffs = new ArrayList<Diff>();
		while (itUseFullDiffs.hasNext()) {
			Diff diff = (Diff)itUseFullDiffs.next();
			useFullDiffs.add(diff);
		}

		assertEquals(7, useFullDiffs.size());

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Recv0"); //$NON-NLS-1$
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", "model.interaction0.Message0Recv0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Recv0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Recv0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", null, "model.interaction0.Message0Recv0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Recv0");
		}

		final Diff addMessage = Iterators.find(useFullDiffs.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(useFullDiffs.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addMessageRecv = Iterators.find(useFullDiffs.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertEquals(1, count(useFullDiffs, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
		} else {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLMessage);
		assertEquals(6, addUMLMessage.getRefinedBy().size());
		assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageRecv));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageInMessage0Recv0));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addMessage.getRequires().size());

			assertEquals(1, addCoveredInMessage0Recv0.getRequires().size());
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertEquals(2, addMessageInMessage0Recv0.getRequires().size());
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertEquals(2, addReceiveEventInMessage.getRequires().size());
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertEquals(1, addCoveredByInLifeline0.getRequires().size());
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageRecv));

			assertEquals(0, addMessageRecv.getRequires().size());

		} else {
			assertEquals(2, addMessage.getRequires().size());
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));

			assertEquals(0, addCoveredInMessage0Recv0.getRequires().size());

			assertEquals(0, addMessageInMessage0Recv0.getRequires().size());

			assertEquals(0, addReceiveEventInMessage.getRequires().size());

			assertEquals(0, addCoveredByInLifeline0.getRequires().size());

			assertEquals(4, addMessageRecv.getRequires().size());
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline0));
		}

		// CHECK EQUIVALENCE
		assertEquals(1, comparison.getEquivalences().size());

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertEquals(2, addCoveredInMessage0Recv0.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		assertFalse("No move expected", Iterators.filter(differences.iterator(), ofKind(DifferenceKind.MOVE))
				.hasNext());

		testIntersections(comparison);
	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		final Iterator<Diff> itUseFullDiffs = Iterators.filter(differences.iterator(),
				not(ofKind(DifferenceKind.MOVE)));
		final List<Diff> useFullDiffs = new ArrayList<Diff>();
		while (itUseFullDiffs.hasNext()) {
			Diff diff = (Diff)itUseFullDiffs.next();
			useFullDiffs.add(diff);
		}

		assertEquals(7, useFullDiffs.size());

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Send0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"sendEvent", "model.interaction0.Message0Send0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Send0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Send0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Send0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"sendEvent", null, "model.interaction0.Message0Send0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Send0");
		}

		final Diff addMessage = Iterators.find(useFullDiffs.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(useFullDiffs.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addMessageRecv = Iterators.find(useFullDiffs.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertEquals(1, count(useFullDiffs, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
		} else {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLMessage);
		assertEquals(6, addUMLMessage.getRefinedBy().size());
		assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageRecv));
		assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredByInLifeline0));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		assertTrue(addUMLMessage.getRefinedBy().contains(addMessageInMessage0Recv0));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addMessage.getRequires().size());

			assertEquals(1, addCoveredInMessage0Recv0.getRequires().size());
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertEquals(2, addMessageInMessage0Recv0.getRequires().size());
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertEquals(2, addReceiveEventInMessage.getRequires().size());
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertEquals(1, addCoveredByInLifeline0.getRequires().size());
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageRecv));

			assertEquals(0, addMessageRecv.getRequires().size());

		} else {
			assertEquals(2, addMessage.getRequires().size());
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));

			assertEquals(0, addCoveredInMessage0Recv0.getRequires().size());

			assertEquals(0, addMessageInMessage0Recv0.getRequires().size());

			assertEquals(0, addReceiveEventInMessage.getRequires().size());

			assertEquals(0, addCoveredByInLifeline0.getRequires().size());

			assertEquals(4, addMessageRecv.getRequires().size());
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline0));
		}

		// CHECK EQUIVALENCE
		assertEquals(1, comparison.getEquivalences().size());

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertEquals(2, addCoveredInMessage0Recv0.getEquivalence().getDifferences().size());
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		assertFalse("No move expected", Iterators.filter(differences.iterator(), ofKind(DifferenceKind.MOVE))
				.hasNext());

		testIntersections(comparison);
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
