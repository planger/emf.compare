/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.handler;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.ISources;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Abstract Handler that manages a merge of a all non-conflicting difference in case of both sides of the
 * comparison are editable.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public abstract class AbstractMergeAllHandler extends AbstractHandler {

	/** The compare configuration object used to get the compare model. */
	private CompareConfiguration configuration;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object editorInput = HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (editorInput instanceof CompareEditorInput) {
			setConfiguration(((CompareEditorInput)editorInput).getCompareConfiguration());
			copyAllDiffs();
		}
		return null;
	}

	/**
	 * Copy all non-conflicting differences.
	 */
	protected abstract void copyAllDiffs();

	/**
	 * Get the compare configuration object.
	 * 
	 * @return the configuration
	 */
	public CompareConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Set the compare configuration object.
	 * 
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(CompareConfiguration configuration) {
		this.configuration = configuration;
	}
}