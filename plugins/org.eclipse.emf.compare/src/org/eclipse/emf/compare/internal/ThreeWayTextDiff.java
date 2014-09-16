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

import java.util.LinkedList;

import name.fraser.neil.plaintext.LineBasedDiff;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
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

	/** The {@link LineBasedDiff} instance. */
	private final LineBasedDiff diff = new LineBasedDiff();

	/** The diffs of the left-hand side. */
	private final LinkedList<Diff> leftDiffs;

	/** The diffs of the right-hand side. */
	private final LinkedList<Diff> rightDiffs;

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
		this.leftDiffs = computeLineBasedDiff(origin, left);
		this.rightDiffs = computeLineBasedDiff(origin, right);
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
		return diff.diff_lines_only(saveGet(base), saveGet(revision));
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
	 * are conflicting.
	 * <p>
	 * A conflict occurs if a line is changed on both sides (left and right), a changed line on one side has
	 * been deleted on the other side, or no total order of lines can be found (e.g., two additions of new
	 * lines at the same original line).
	 * </p>
	 * 
	 * @return <code>true</code> if left and right is in conflict; <code>false</code> otherwise.
	 */
	public boolean isConflicting() {

		// TODO implement

		return false;
	}

	/**
	 * Performs and returns a merge of the modifications of the left-hand side and the right-hand side.
	 * <p>
	 * If the left-hand side diff and the right-hand side diff is not conflicting, the merge operation is
	 * symmetric; that is, the merge result will always yield the same result, also when switching the
	 * left-hand side and the right-hand side. If the diffs are conflicting, this method will still return a
	 * merged version, taking the left-hand side and applying the patches of the right-hand side. However, in
	 * this case, the merge operation might be not be symmetric; that is, switching left and right might yield
	 * different merge results.
	 * </p>
	 * 
	 * @return A merged version of left and right.
	 */
	public String getMerged() {
		final LinkedList<Patch> rightPatches = computePatches(rightDiffs);
		final String patchedLeft = applyPatches(left, rightPatches);
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
	 * Computes and returns patches for the given {@code diffs}.
	 * 
	 * @param diffs
	 *            The diffs to compute the patches for.
	 * @return The computed patches.
	 */
	private LinkedList<Patch> computePatches(LinkedList<Diff> diffs) {
		return diff.patch_make(diffs);
	}

	/**
	 * Applies the given {@code patches} to the given {@code base} text.
	 * 
	 * @param base
	 *            The text to apply the {@code patches} to.
	 * @param patches
	 *            The patches to be applied.
	 * @return The patched version of {@code base}.
	 */
	private String applyPatches(String base, LinkedList<Patch> patches) {
		return (String)diff.patch_apply(patches, saveGet(base))[0];
	}

}
