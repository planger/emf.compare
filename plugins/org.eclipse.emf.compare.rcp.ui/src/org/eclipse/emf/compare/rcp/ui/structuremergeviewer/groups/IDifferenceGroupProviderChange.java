/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups;

import org.eclipse.emf.compare.rcp.ui.configuration.ICompareEvent;

/**
 * Stores selected {@link IDifferenceGroupProvider}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public interface IDifferenceGroupProviderChange extends ICompareEvent {

	/**
	 * Returns the selected {@link IDifferenceGroupProviderChange}.
	 * 
	 * @return the selected IDifferenceGroupProvider.
	 */
	IDifferenceGroupProvider getDifferenceGroupProvider();
}
