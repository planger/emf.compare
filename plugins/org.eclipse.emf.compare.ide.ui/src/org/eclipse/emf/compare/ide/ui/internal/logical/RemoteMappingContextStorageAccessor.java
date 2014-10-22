/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;

/**
 * This will use a {@link RemoteResourceMappingContext} in order to fetch the content of the sides of a
 * comparison during model resolving.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class RemoteMappingContextStorageAccessor implements IStorageProviderAccessor {
	/** The underlying {@link RemoteResourceMappingContext}. */
	private final RemoteResourceMappingContext context;

	/**
	 * Wraps the given mapping context within this accessor.
	 * 
	 * @param context
	 *            The wrapped context.
	 */
	public RemoteMappingContextStorageAccessor(RemoteResourceMappingContext context) {
		this.context = checkNotNull(context);
	}

	/** {@inheritDoc} */
	public IStorageProvider getStorageProvider(IResource resource, DiffSide side) throws CoreException {
		if (resource instanceof IFile) {
			return new RemoteMappingStorageProvider(context, side, (IFile)resource);
		}
		return null;
	}

	/** {@inheritDoc} */
	public boolean isInSync(IResource resource) throws CoreException {
		final IProgressMonitor monitor = new NullProgressMonitor();
		return context.hasLocalChange(resource, monitor) || context.hasRemoteChange(resource, monitor);
	}
}
