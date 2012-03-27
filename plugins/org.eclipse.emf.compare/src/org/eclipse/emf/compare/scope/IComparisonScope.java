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
package org.eclipse.emf.compare.scope;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This class defines the expected contract of EMF Compare scopes.
 * <p>
 * The scope will be called on every single Notifier in in order to determine the Notifier's contents; only
 * those Notifiers will be matched by EMF Compare.
 * </p>
 * <p>
 * An implementation using Predicates to filter out the children lists can be sub-classed instead, see
 * {@link FilterComparisonScope}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see FilterComparisonScope
 */
public interface IComparisonScope {
	/**
	 * This will be used by EMF Compare in order to retrieve the left "root" Notifier of this comparison; i.e
	 * the first object to be considered by the match engine, and from which the iteration over children
	 * should start.
	 * 
	 * @return The left root of this comparison. May not be <code>null</code>.
	 */
	Notifier getLeft();

	/**
	 * This will be used by EMF Compare in order to retrieve the right "root" Notifier of this comparison; i.e
	 * the first object to be considered by the match engine, and from which the iteration over children
	 * should start.
	 * 
	 * @return The right root of this comparison. May not be <code>null</code>.
	 */
	Notifier getRight();

	/**
	 * If EMF Compare should consider a Notifier as being the common ancestor of the "left" and "right"
	 * objects to compare, it should be returned from here.
	 * 
	 * @return The origin root for this comparison. May be <code>null</code>.
	 */
	Notifier getOrigin();

	/**
	 * This will be used by EMF Compare in order to know which Resources are under the given ResourceSet.
	 * 
	 * @param resourceSet
	 *            The resource set for which we need children Resources.
	 * @return An iterator over the {@link Resource}s within the given {@link ResourceSet} which are part of
	 *         this scope.
	 */
	Iterator<? extends Resource> getChildren(ResourceSet resourceSet);

	/**
	 * This will be used by EMF Compare in order to determine the EObjects that it should iterate over.
	 * 
	 * @param resource
	 *            The resource for which we need to determine the content.
	 * @return An iterator over the {@link EObject}s within the given {@link Resource} which are part of this
	 *         scope.
	 */
	Iterator<? extends EObject> getChildren(Resource resource);

	/**
	 * This will be used by EMF Compare in order to know which EObjects are located under the given EObject.
	 * 
	 * @param eObject
	 *            The EObject for which we need to determine the content.
	 * @return An iterator over the Resources within the given {@link EObject} which are part of this scope.
	 */
	Iterator<? extends EObject> getChildren(EObject eObject);
}