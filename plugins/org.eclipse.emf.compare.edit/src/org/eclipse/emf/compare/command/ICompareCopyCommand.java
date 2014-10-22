/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.command;

import org.eclipse.emf.common.command.Command;

/**
 * Interface of compare copy (merge) command. It knows on which side of the copy will be applied.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface ICompareCopyCommand extends Command {

	/**
	 * Returns true if the command will be applied from left to right side, false otherwise.
	 * 
	 * @return true if the command will be applied from left to right side, false otherwise.
	 */
	boolean isLeftToRight();
}
