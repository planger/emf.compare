/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.util;

import org.eclipse.emf.compare.internal.merge.MergeMode;

public class FuzzyUtil {
	
	public static String getDebugFileName(String fileName, MergeMode direction) {
		final String directionString = getDirectionName(direction);
		return directionString + "_" + fileName;
	}
	
	public static String getDirectionName(MergeMode direction) {
		final String directionString;
		switch (direction) {
			case RIGHT_TO_LEFT:
				directionString = "r2l";
				break;
			case LEFT_TO_RIGHT:
				directionString = "l2r";
				break;
			default:
				directionString = "";
		}
		return directionString;
	}
}
