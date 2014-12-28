/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.emf.compare.internal.dmp.LineBasedDiff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Diff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Operation;

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

	/** Constant for new line String. */
	private static final String NL = "\n"; //$NON-NLS-1$

	/** The left-hand side line operations. */
	private LineDifferences leftLineOperations;

	/** The right-hand side line operations. */
	private LineDifferences rightLineOperations;

	/** The computed three-way line differences. */
	private LinkedList<ThreeWayLineDifference> threeWayLineDifferences;

	/**
	 * Constructs a {@link ThreeWayTextDiff} for the given {@code origin} of a plain text and the two
	 * potentially modified versions of it, {@code left} and {@code right}.
	 * 
	 * @param origin
	 *            The common ancestor version of the plain text.
	 * @param left
	 *            The potentially modified left-hand side version of the common ancestor.
	 * @param right
	 *            The potentially modified right-hand side version of the common ancestor.
	 */
	public ThreeWayTextDiff(String origin, String left, String right) {
		this.leftLineOperations = new LineDifferences(origin, left);
		this.rightLineOperations = new LineDifferences(origin, right);
		computeThreeWayLineDifferences();
	}

	private void computeThreeWayLineDifferences() {
		threeWayLineDifferences = new LinkedList<ThreeWayLineDifference>();
		try {
			String originLine;
			final Iterator<Diff> leftDiffIterator = leftLineOperations.diffs.iterator();
			final Iterator<Diff> rightDiffIterator = leftLineOperations.diffs.iterator();
			final BufferedReader originReader = createBufferedReader(getOrigin());
			while ((originLine = originReader.readLine()) != null) {
				final ThreeWayLineDifference lineDifference = computeThreeWayLineDifference(originLine,
						leftDiffIterator, rightDiffIterator);
				threeWayLineDifferences.add(lineDifference);
			}
			originReader.close();
		} catch (IOException e) {
			// must never happen as we read strings
		}
	}

	private ThreeWayLineDifference computeThreeWayLineDifference(final String originLine,
			final Iterator<Diff> leftDiffIterator, final Iterator<Diff> rightDiffIterator) {

		final ThreeWayLineDifference threeWayLineDifference = new ThreeWayLineDifference(originLine);

		if (leftDiffIterator.hasNext()) {
			boolean hitEqualForOriginLine = false;
			for (Diff leftDiff = leftDiffIterator.next(); leftDiffIterator.hasNext()
					&& !isDeleteOtherLine(originLine, leftDiff) && !isEqualOtherLine(originLine, leftDiff)
					&& !hitEqualForOriginLine; leftDiff = leftDiffIterator.next()) {
				threeWayLineDifference.addLeftDiff(leftDiff);
				hitEqualForOriginLine = isEqualSameLine(originLine, leftDiff);
			}
		}

		for (Diff rightDiff = rightDiffIterator.next(); rightDiffIterator.hasNext()
				&& !isEqualSameLine(originLine, rightDiff); rightDiff = rightDiffIterator.next()) {
			threeWayLineDifference.addRightDiff(rightDiff);

		}
		return threeWayLineDifference;
	}

	private boolean isEqualSameLine(final String line, Diff diff) {
		return diff != null && Operation.EQUAL.equals(diff.operation) && line.equals(diff.text);
	}

	private boolean isEqualOtherLine(final String line, Diff diff) {
		return diff != null && Operation.EQUAL.equals(diff.operation) && !line.equals(diff.text);
	}

	private boolean isDeleteSameLine(final String line, Diff diff) {
		return diff != null && Operation.DELETE.equals(diff.operation) && line.equals(diff.text);
	}

	private boolean isDeleteOtherLine(final String line, Diff diff) {
		return diff != null && Operation.DELETE.equals(diff.operation) && !line.equals(diff.text);
	}

	/**
	 * Returns the common ancestor version.
	 * 
	 * @return The common ancestor version.
	 */
	public String getOrigin() {
		return leftLineOperations.getOrigin();
	}

	/**
	 * Returns the potentially modified left-hand side version of the origin.
	 * 
	 * @return The left version.
	 */
	public String getLeft() {
		return leftLineOperations.getRevised();
	}

	/**
	 * Returns the potentially modified right-hand side version of the origin.
	 * 
	 * @return The right version.
	 */
	public String getRight() {
		return rightLineOperations.getRevised();
	}

	/**
	 * Specifies whether the modification on the left-hand side and the modifications on the right-hand side
	 * are conflicting.
	 * <p>
	 * A conflict occurs if a line is changed on both sides (left and right), a line is changed on one side
	 * and is deleted on the other side, or no total order of lines can be found (e.g., two additions of new
	 * lines at the same original line).
	 * </p>
	 * 
	 * @return <code>true</code> if left and right is in conflict; <code>false</code> otherwise.
	 */
	public boolean isConflicting() {
		// TODO
		return false;
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
		// TODO
		return "";
	}

	/**
	 * Creates a {@link BufferedReader} over the given {@code string}.
	 * 
	 * @param string
	 *            The string to create the reader for.
	 * @return The created reader.
	 */
	private BufferedReader createBufferedReader(String string) {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());
		return new BufferedReader(new InputStreamReader(inputStream));
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
		return getOrigin() != null && (getLeft() == null || getRight() == null);
	}

	/**
	 * Returns an empty string if the given {@code text} is <code>null</code>, otherwise it returns
	 * {@code text}.
	 * 
	 * @param text
	 *            The string to get safely.
	 * @return An empty string if {@code text} is <code>null</code>, otherwise {@code text}.
	 */
	private String nullToEmpty(String text) {
		if (text == null) {
			return ""; //$NON-NLS-1$
		} else {
			return text;
		}
	}

	/**
	 * The line operations (delete, insert, or equal) of each line of an origin version in the context of a
	 * diff.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class LineDifferences {

		/** The {@link LineBasedDiff} instance. */
		private final LineBasedDiff lbDiff = new LineBasedDiff();

		/** The origin version of the text. */
		private String origin;

		/** The revised version of the text. */
		private String revised;

		/** The differences between {@link #origin} and {@link #revised}. */
		private LinkedList<Diff> diffs;

		/**
		 * Creates the line differences for the given {@code origin} version of a text and its {@code revised}
		 * version.
		 * 
		 * @param origin
		 *            The origin version of the plain text.
		 * @param revised
		 *            The revised version of the plain text.
		 */
		public LineDifferences(String origin, String revised) {
			super();
			this.origin = origin;
			this.revised = revised;
			computeLineBasedDiff();
		}

		/**
		 * Computes and sets the line-based differences between {@link #origin} and {@link #revised}.
		 * <p>
		 * This method computes flattened line-based differences. Flattened differences contain one difference
		 * entry for each added or deleted line. If a addition or deletion spans across multiple lines, the
		 * flattened difference will show one addition or deletion for each line within the added or deleted
		 * block of lines.
		 * </p>
		 */
		private void computeLineBasedDiff() {
			diffs = new LinkedList<Diff>();
			for (Diff diff : lbDiff.computeLineBasedDiff(origin, revised)) {
				String line;
				try {
					final BufferedReader reader = createBufferedReader(diff.text);
					while ((line = reader.readLine()) != null) {
						diffs.add(new Diff(diff.operation, line));
					}
					reader.close();
				} catch (IOException e) {
					// this must never happen as we are reading strings
				}
			}
		}

		/**
		 * Returns the original version of the text.
		 * 
		 * @return The original version.
		 */
		public String getOrigin() {
			return origin;
		}

		/**
		 * Returns the revised version of the text.
		 * 
		 * @return The revised version.
		 */
		public String getRevised() {
			return revised;
		}
	}

	private class ThreeWayLineDifference {

		private String originLine;

		private LinkedList<Diff> leftDiffs = new LinkedList<Diff>();

		private LinkedList<Diff> rightDiffs = new LinkedList<Diff>();

		public ThreeWayLineDifference(String originLine) {
			this.originLine = originLine;
		}

		public void addLeftDiff(Diff leftDiff) {
			leftDiffs.add(leftDiff);
		}

		public void addRightDiff(Diff rightDiff) {
			rightDiffs.add(rightDiff);
		}

		public boolean isConflicting() {
			// TODO
			return false;
		}

		public String getMerged() {
			// TODO
			return "";
		}

	}
}
