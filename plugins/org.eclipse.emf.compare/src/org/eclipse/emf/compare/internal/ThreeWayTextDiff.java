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
	private String origin;

	/** The potentially modified left version of the origin. */
	private String left;

	/** The potentially modified right version of the origin. */
	private String right;

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
	}

	/**
	 * Returns the original version.
	 * 
	 * @return the original version.
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Returns the potentially modified left-hand side version of the origin.
	 * 
	 * @return the left version.
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * Returns the potentially modified right-hand side version of the origin.
	 * 
	 * @return the right version.
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
	 * TODO what to do when there is a conflict? Markers?
	 * </p>
	 * 
	 * @return a merged version of left and right.
	 */
	public String getMerged() {
		// TODO implement
		return "";
	}

}
