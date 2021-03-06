/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.table;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link IViewerCreator} that creates {@link TableContentMergeViewer}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TableContentMergeViewerCreator implements IViewerCreator {

	/**
	 * ID of the contribution to the org.eclipse.compare.contentMergeViewers extension point.
	 */
	public static final String EOBJECT_CONTENT_MERGE_VIEWER_ID = "org.eclipse.emf.compare.ide.ui.internal.EObjectContentListMergeViewer"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new TableContentMergeViewer(parent, new EMFCompareConfiguration(config));
	}

}
