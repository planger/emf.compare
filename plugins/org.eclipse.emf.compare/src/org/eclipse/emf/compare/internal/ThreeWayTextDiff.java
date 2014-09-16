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
package org.eclipse.emf.compare.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import name.fraser.neil.plaintext.LineBasedDiff;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import name.fraser.neil.plaintext.diff_match_patch.Patch;

/**
 * Three-way differencing utility for plain text.
 * <p>
 * Taking three versions of a plain text as input (the origin, left, and right version), a three-way diff
 * provides the information on whether the left and right version underwent conflicting changes and, if not,
 * enables to compute a new merged version of the text so that all changes of the left and right version are
 * combined.
 * </p>
 * <p>
 * The differencing, conflict detection, and merging is line-based; that is, a conflict-free merge can only be
 * performed if each single line is changed only on one side (left or right), each changed line on one side
 * has not been deleted on the other side, and a total order of lines can be found (e.g., no additions of new
 * lines at the same line). Otherwise, the modifications of the left version and the right version are in
 * conflict.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ThreeWayTextDiff {

	/** The original version of the plain text. */
	private final String origin;

	/** The potentially modified left version of the origin. */
	private final String left;

	/** The potentially modified right version of the origin. */
	private final String right;

	/** The line operations of the left-hand side. */
	private final LineOperations leftOperations;

	/** The line operations of the right-hand side. */
	private final LineOperations rightOperations;

	/**
	 * Constructs a {@link ThreeWayTextDiff} for the given {@code origin} of a plain text and the two
	 * potentially modified versions of it, {@code left} and {@code right}.
	 * 
	 * @param origin
	 *            The original version of the plain text.
	 * @param left
	 *            The potentially modified left version of the origin.
	 * @param right
	 *            The potentially modified right version of the origin.
	 */
	public ThreeWayTextDiff(String origin, String left, String right) {
		this.origin = origin;
		this.left = left;
		this.right = right;
		this.leftOperations = new LineOperations(origin, left);
		this.rightOperations = new LineOperations(origin, right);
	}

	/**
	 * Returns the original version.
	 * 
	 * @return The original version.
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Returns the potentially modified left-hand side version of the origin.
	 * 
	 * @return The left version.
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * Returns the potentially modified right-hand side version of the origin.
	 * 
	 * @return The right version.
	 */
	public String getRight() {
		return right;
	}

	/**
	 * Specifies whether the modification on the left-hand side and the modifications on the right-hand side
	 * are conflicting. TODO check docs
	 * <p>
	 * A conflict occurs if a line is changed on both sides (left and right), a line is changed on one side
	 * and is deleted on the other side, or no total order of lines can be found (e.g., two additions of new
	 * lines at the same original line).
	 * </p>
	 * <p>
	 * When considering each change as a deletion and addition of a line, the conflict detection boils down to
	 * finding additions on both sides that are inserted before or after the same line.
	 * </p>
	 * 
	 * @return <code>true</code> if left and right is in conflict; <code>false</code> otherwise.
	 */
	public boolean isConflicting() {
		boolean isConflicting = false;

		final int numberOfLinesInOrigin = getNumberOfLinesInOrigin();
		for (int currentIndex = 0; currentIndex < numberOfLinesInOrigin; currentIndex++) {
			if (isConflicting(currentIndex)) {
				isConflicting = true;
				break;
			}
		}

		return isConflicting;
	}

	private int getNumberOfLinesInOrigin() {
		final String[] originLines = saveGet(origin).split("\n"); //$NON-NLS-1$
		return originLines.length;
	}

	private boolean isConflicting(int lineIndex) {
		return isInsertInsertAt(lineIndex) || isInsertDeleteAt(lineIndex) || isDeleteInsertAt(lineIndex);
	}

	private boolean isInsertInsertAt(int lineIndex) {
		return leftOperations.isInsertAtOriginalLineIndex(lineIndex)
				&& rightOperations.isInsertAtOriginalLineIndex(lineIndex);
	}

	private boolean isInsertDeleteAt(int currentLineIndex) {
		return leftOperations.isDeleteInsertAtOriginalLineIndex(currentLineIndex)
				&& rightOperations.isDeleteAtOriginalLineIndex(currentLineIndex);
	}

	private boolean isDeleteInsertAt(int currentLineIndex) {
		return leftOperations.isDeleteAtOriginalLineIndex(currentLineIndex)
				&& rightOperations.isDeleteInsertAtOriginalLineIndex(currentLineIndex);
	}

	/**
	 * Performs and returns a merge of the modifications of the left-hand side and the right-hand side.
	 * <p>
	 * If the left-hand side diff and the right-hand side diff is not conflicting, the merge operation is
	 * symmetric; that is, the merge result will always yield the same result, also when switching the
	 * left-hand side and the right-hand side. If the left-hand and right-hand side diffs are conflicting,
	 * this method will still return a merged version, taking the left-hand side and applying the patches of
	 * the right-hand side. However, in this case, the merge operation might be not be symmetric; that is,
	 * switching left and right might yield different merge results.
	 * </p>
	 * 
	 * @return A merged version of left and right.
	 */
	public String getMerged() {
		final String patchedLeft = rightOperations.apply(left);
		final String merged = nullIfUnset(patchedLeft);
		return merged;
	}

	/**
	 * If {@link #left} or {@link #right} is {@link #isLeftOrRightUnset() unset} and the given
	 * {@code mergeResult} is empty (i.e., the unset constitutes the merge result), this method returns
	 * <code>null</code> to indicate an unset. Otherwise, this method returns {@code mergeResult}.
	 * 
	 * @param mergeResult
	 *            The merge result to check for unset.
	 * @return <code>null</code> if the merge result is an unset; otherwise {@code mergeResult}.
	 */
	private String nullIfUnset(final String mergeResult) {
		if (mergeResult.length() < 1 && isLeftOrRightUnset()) {
			return null;
		} else {
			return mergeResult;
		}
	}

	/**
	 * Specifies whether {@link #left} or {@link #right} has been unset.
	 * 
	 * @return <code>true</code> if left or right has been unset, <code>false</code> otherwise.
	 */
	private boolean isLeftOrRightUnset() {
		return origin != null && (left == null || right == null);
	}

	/**
	 * Returns an empty string if the given {@code text} is <code>null</code>, otherwise it returns
	 * {@code text}.
	 * 
	 * @param text
	 *            The string to get safely.
	 * @return An empty string if {@code text} is <code>null</code>, otherwise {@code text}.
	 */
	private String saveGet(String text) {
		if (text == null) {
			return ""; //$NON-NLS-1$
		} else {
			return text;
		}
	}

	/**
	 * The {@link LineStatus} of each line of the origin version in the context of a diff.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class LineOperations {

		/** The {@link LineBasedDiff} instance. */
		private final LineBasedDiff lbDiff = new LineBasedDiff();

		/** The diffs described in this line operations. */
		private LinkedList<Diff> diffs;

		/** The list of operations. */
		private List<Operation> operations = new ArrayList<Operation>();

		/**
		 * Creates the line operations for the given {@code base} version of a text and its {@code revision}.
		 * 
		 * @param base
		 *            The original version of the plain text.
		 * @param revision
		 *            The revised version of the plain text.
		 */
		public LineOperations(String base, String revision) {
			super();
			this.diffs = computeLineBasedDiff(base, revision);
			initialize();
		}

		/**
		 * Initializes the list of operations.
		 */
		private void initialize() {
			for (Diff diff : diffs) {
				addLineOperations(diff);
			}
		}

		/**
		 * Computes a line-based diff between {@code base} and {@code revision}.
		 * 
		 * @param base
		 *            The original version of the plain text.
		 * @param revision
		 *            The revised version of the plain text.
		 * @return The line-based differences between {@code base} and {@code revision}.
		 */
		private LinkedList<Diff> computeLineBasedDiff(String base, String revision) {
			return lbDiff.diff_lines_only(saveGet(base), saveGet(revision));
		}

		/**
		 * Adds the line operations of the given {@code diff}.
		 * 
		 * @param diff
		 *            The diff to add line operations of.
		 */
		private void addLineOperations(Diff diff) {
			final int numberOfLines = getNumberOfLines(diff);
			for (int i = 0; i < numberOfLines; i++) {
				operations.add(diff.operation);
			}
		}

		/**
		 * Returns the number of lines inserted, deleted, or left equal of the given {@code diff}.
		 * 
		 * @param diff
		 *            The diff to get the number of inserted, deleted, or equal lines for.
		 * @return The number of inserted, deleted, or equal lines for {@code diff}.
		 */
		private int getNumberOfLines(Diff diff) {
			return diff.text.split("\n").length; //$NON-NLS-1$
		}

		/**
		 * Applies the line operations to the given {@code base} text.
		 * 
		 * @param base
		 *            The text to apply the {@code patches} to.
		 * @return The patched version of {@code base}.
		 */
		public String apply(String base) {
			final LinkedList<Patch> patches = computePatches();
			return (String)lbDiff.patch_apply(patches, saveGet(base))[0];
		}

		/**
		 * Computes and returns patches for this line operations.
		 * 
		 * @return The computed patches.
		 */
		private LinkedList<Patch> computePatches() {
			return lbDiff.patch_make(diffs);
		}

		public boolean isInsertAtOriginalLineIndex(int currentLineIndex) {
			final int lineIndex = getLineIndexForOriginalLineIndex(currentLineIndex);
			return isInsertAt(lineIndex);
		}

		public boolean isDeleteAtOriginalLineIndex(int originalLineIndex) {
			final int lineIndex = getLineIndexForOriginalLineIndex(originalLineIndex);
			return isDeleteAt(lineIndex);
		}

		public boolean isDeleteInsertAtOriginalLineIndex(int originalLineIndex) {
			final int lineIndex = getLineIndexForOriginalLineIndex(originalLineIndex);
			return isDeleteInsertAt(lineIndex);
		}

		private int getLineIndexForOriginalLineIndex(int originalLineIndex) {
			int currentOriginalLineIndex = 0;
			int lineIndex = 0;
			while (currentOriginalLineIndex < originalLineIndex) {
				final Operation operation = getOperation(lineIndex);
				if (!isInsert(operation)) {
					currentOriginalLineIndex++;
				}
				lineIndex++;
			}
			return lineIndex;
		}

		private boolean isInsertAt(int lineIndex) {
			return hasIndex(lineIndex) && isInsert(getOperation(lineIndex));
		}

		private boolean isDeleteAt(int lineIndex) {
			return hasIndex(lineIndex) && isDelete(getOperation(lineIndex));
		}

		private boolean isDeleteInsertAt(int lineIndex) {
			return (isDeleteAt(lineIndex) && isFollowedByInsert(lineIndex)) || isInsertAt(lineIndex);
		}

		private boolean isFollowedByInsert(int lineIndex) {
			final boolean isFollowedByInsert;
			final int nextLineIndex = lineIndex + 1;
			if (hasIndex(nextLineIndex) && !isEqual(getOperation(nextLineIndex))) {
				final Operation nextOperation = getOperation(nextLineIndex);
				isFollowedByInsert = isInsert(nextOperation) || isFollowedByInsert(nextLineIndex);
			} else {
				isFollowedByInsert = false;
			}
			return isFollowedByInsert;
		}

		private Operation getOperation(int index) {
			return operations.get(index);
		}

		private boolean hasIndex(int index) {
			return operations.size() > index;
		}

		private boolean isInsert(Operation operation) {
			return Operation.INSERT.equals(operation);
		}

		private boolean isDelete(final Operation operation) {
			return Operation.DELETE.equals(operation);
		}

		private boolean isEqual(final Operation operation) {
			return Operation.EQUAL.equals(operation);
		}
	}
}
