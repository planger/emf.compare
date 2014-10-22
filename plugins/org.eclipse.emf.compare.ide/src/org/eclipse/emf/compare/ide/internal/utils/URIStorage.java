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
package org.eclipse.emf.compare.ide.internal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;

/**
 * This implementation of an {@link IStorage} will allow us to keep track of the {@link URIHandler} that's
 * been used to load a given URI from this uri converter.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class URIStorage implements IStorage {
	/** The target URI of this storage. */
	private final URI uri;

	/** The URI Handler that's been used to retrieve this URI's contents. */
	private final URIHandler handler;

	/** The URI converter from which this storage was created. */
	private URIConverter converter;

	/**
	 * Creates an URIStorage for the given URI an its associated handler.
	 * 
	 * @param uri
	 *            The target uri of this storage.
	 * @param handler
	 *            The URI handler that can be used to retrieve this URI's contents.
	 * @param converter
	 *            The URI converter which created this storage.
	 */
	public URIStorage(URI uri, URIHandler handler, URIConverter converter) {
		this.uri = uri;
		this.handler = handler;
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getContents()
	 */
	public InputStream getContents() throws CoreException {
		final Map<?, ?> options = Collections.singletonMap(URIConverter.OPTION_URI_CONVERTER, converter);
		try {
			return handler.createInputStream(uri, options);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EMFCompareIDEPlugin.PLUGIN_ID, e.getMessage(),
					e));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getFullPath()
	 */
	public IPath getFullPath() {
		final String path;
		if (uri.isRelative()) {
			path = uri.toString();
		} else if (uri.isPlatformResource()) {
			path = uri.toPlatformString(true);
		} else {
			path = uri.toString();
		}
		return new Path(path);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getName()
	 */
	public String getName() {
		return URI.decode(uri.lastSegment());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#isReadOnly()
	 */
	public boolean isReadOnly() {
		final Map<?, ?> options = Collections.singletonMap(URIConverter.OPTION_REQUESTED_ATTRIBUTES,
				Collections.singleton(URIConverter.ATTRIBUTE_READ_ONLY));
		final Map<String, ?> attributes = handler.getAttributes(uri, options);
		return Boolean.TRUE.equals(attributes.get(URIConverter.ATTRIBUTE_READ_ONLY));
	}
}
