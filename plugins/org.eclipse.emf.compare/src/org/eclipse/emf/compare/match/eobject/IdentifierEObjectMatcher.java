/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 449796
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This implementation of an {@link IEObjectMatcher} will create {@link Match}es based on the input EObjects
 * identifiers (either XMI:ID or attribute ID) alone.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class IdentifierEObjectMatcher implements IEObjectMatcher {
	/**
	 * This instance might have a delegate matcher. The delegate matcher will be called when no ID is found
	 * and its results are aggregated with the current matcher.
	 */
	private Optional<IEObjectMatcher> delegate;

	/**
	 * This will be used to determine what represents the "identifier" of an EObject. By default, we will use
	 * the following logic, in order (i.e. if condition 1 is fulfilled, we will not try conditions 2 and 3) :
	 * <ol>
	 * <li>If the given eObject is a proxy, it is uniquely identified by its URI fragment.</li>
	 * <li>If the eObject's EClass has an eIdAttribute set, use this attribute's value.</li>
	 * <li>If the eObject is located in an XMI resource and has an XMI ID, use this as its unique identifier.</li>
	 * </ol>
	 */
	private Function<EObject, String> idComputation = new DefaultIDFunction();

	/**
	 * This will be used to determine what represents the XMI ID of an EObject.
	 */
	private Function<EObject, String> xmiIDComputation = new XMIIDFunction();

	/**
	 * Creates an ID based matcher without any delegate.
	 */
	public IdentifierEObjectMatcher() {
		this(null, new DefaultIDFunction());
	}

	/**
	 * Creates an ID based matcher with the given delegate when no ID can be found.
	 * 
	 * @param delegateWhenNoID
	 *            the matcher to delegate to when no ID is found.
	 */
	public IdentifierEObjectMatcher(IEObjectMatcher delegateWhenNoID) {
		this(delegateWhenNoID, new DefaultIDFunction());
	}

	/**
	 * Creates an ID based matcher computing the ID with the given function.
	 * 
	 * @param idComputation
	 *            the function used to compute the ID.
	 */
	public IdentifierEObjectMatcher(Function<EObject, String> idComputation) {
		this(null, idComputation);
	}

	/**
	 * Create an ID based matcher with a delegate which is going to be called when no ID is found for a given
	 * EObject. It is computing the ID with the given function
	 * 
	 * @param delegateWhenNoID
	 *            the delegate matcher to use when no ID is found.
	 * @param idComputation
	 *            the function used to compute the ID.
	 */
	public IdentifierEObjectMatcher(IEObjectMatcher delegateWhenNoID, Function<EObject, String> idComputation) {
		this.delegate = Optional.fromNullable(delegateWhenNoID);
		this.idComputation = idComputation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createMatches(Comparison comparison, Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
			Monitor monitor) {
		final List<EObject> leftEObjectsNoID = Lists.newArrayList();
		final List<EObject> rightEObjectsNoID = Lists.newArrayList();
		final List<EObject> originEObjectsNoID = Lists.newArrayList();

		final Set<Match> matches = matchPerId(leftEObjects, rightEObjects, originEObjects, leftEObjectsNoID,
				rightEObjectsNoID, originEObjectsNoID);

		Iterables.addAll(comparison.getMatches(), matches);

		if (delegate.isPresent()) {
			doDelegation(comparison, leftEObjectsNoID, rightEObjectsNoID, originEObjectsNoID, monitor);
		} else {
			for (EObject eObject : leftEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setLeft(eObject);
				matches.add(match);
			}
			for (EObject eObject : rightEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setRight(eObject);
				matches.add(match);
			}
			for (EObject eObject : originEObjectsNoID) {
				Match match = CompareFactory.eINSTANCE.createMatch();
				match.setOrigin(eObject);
				matches.add(match);
			}

		}
	}

	/**
	 * Execute matching process for the delegated IEObjectMatcher.
	 * 
	 * @param comparison
	 *            the comparison object that contains the matches
	 * @param monitor
	 *            the monitor instance to track the matching progress
	 * @param leftEObjectsNoID
	 *            remaining left objects after matching
	 * @param rightEObjectsNoID
	 *            remaining right objects after matching
	 * @param originEObjectsNoID
	 *            remaining origin objects after matching
	 */
	protected void doDelegation(Comparison comparison, final List<EObject> leftEObjectsNoID,
			final List<EObject> rightEObjectsNoID, final List<EObject> originEObjectsNoID, Monitor monitor) {
		delegate.get().createMatches(comparison, leftEObjectsNoID.iterator(), rightEObjectsNoID.iterator(),
				originEObjectsNoID.iterator(), monitor);
	}

	/**
	 * Matches the EObject per ID.
	 * 
	 * @param leftEObjects
	 *            the objects to match (left side).
	 * @param rightEObjects
	 *            the objects to match (right side).
	 * @param originEObjects
	 *            the objects to match (origin side).
	 * @param leftEObjectsNoID
	 *            remaining left objects after matching
	 * @param rightEObjectsNoID
	 *            remaining right objects after matching
	 * @param originEObjectsNoID
	 *            remaining origin objects after matching
	 * @return the match built in the process.
	 */
	protected Set<Match> matchPerId(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
			final List<EObject> leftEObjectsNoID, final List<EObject> rightEObjectsNoID,
			final List<EObject> originEObjectsNoID) {
		final Set<Match> matches = Sets.newLinkedHashSet();

		// This lookup map will be used by iterations on right and origin to find the match in which they
		// should add themselves
		final Map<String, Match> defaultIDToMatch = Maps.newHashMap();
		final Map<String, Match> xmiIDToMatch = Maps.newHashMap();

		// We will try and mimic the structure of the input model.
		// These map do not need to be ordered, we only need fast lookup.
		final Map<EObject, Match> leftEObjectsToMatch = Maps.newHashMap();
		final Map<EObject, Match> rightEObjectsToMatch = Maps.newHashMap();
		final Map<EObject, Match> originEObjectsToMatch = Maps.newHashMap();

		// We'll only iterate once on each of the three sides, building the matches as we go
		while (leftEObjects.hasNext()) {
			final EObject left = leftEObjects.next();

			final String identifier = idComputation.apply(left);
			if (identifier == null) {
				leftEObjectsNoID.add(left);
				continue;
			}

			final Match match = CompareFactory.eINSTANCE.createMatch();
			match.setLeft(left);

			// Can we find a parent? Assume we're iterating in containment order
			final EObject parentEObject = left.eContainer();
			final Match parent = leftEObjectsToMatch.get(parentEObject);
			if (parent != null) {
				parent.getSubmatches().add(match);
			} else {
				matches.add(match);
			}

			defaultIDToMatch.put(identifier, match);
			leftEObjectsToMatch.put(left, match);

			// add xmi identifier for improving matches later on
			String xmiIdentifier = xmiIDComputation.apply(left);
			if (xmiIdentifier != null) {
				xmiIDToMatch.put(xmiIdentifier, match);
			}

		}

		// used for objects which wish to be better matched
		List<EObject> sndPriorityMatches;

		sndPriorityMatches = performDefaultIDMatching(rightEObjects, rightEObjectsNoID, xmiIDToMatch,
				defaultIDToMatch, rightEObjectsToMatch, matches, true);

		// second try with xmiID for right elements which were not matched via default id
		performXMIIDMatching(sndPriorityMatches.iterator(), xmiIDToMatch, defaultIDToMatch,
				rightEObjectsToMatch, matches, true);

		sndPriorityMatches = performDefaultIDMatching(originEObjects, originEObjectsNoID, xmiIDToMatch,
				defaultIDToMatch, originEObjectsToMatch, matches, false);

		// second try with xmiID for origin elements which were not matched via default id
		performXMIIDMatching(sndPriorityMatches.iterator(), xmiIDToMatch, defaultIDToMatch,
				originEObjectsToMatch, matches, false);

		return matches;
	}

	/**
	 * Matches the objects given by {@code objects} via their default id with objects contained in
	 * {@code defaultIDToMatch} and places them either in {@code noIDObjects} or within the match trees in
	 * {@code matches} and the {@code objectsToMatch} result mapping.
	 *
	 * @param objects
	 *            The objects to be matched.
	 * @param noIDObjects
	 *            The collection within the objects will be placed when no id can be calculated.
	 * @param xmiIDToMatch
	 *            The mapping representing all possible matches via the xmi id. Is modified when a new xmi id
	 *            is found.
	 * @param defaultIDToMatch
	 *            The mapping containing all possibles matches via the default id. Is modified when a new
	 *            match is created.
	 * @param objectsToMatch
	 *            The mapping which contains the result of this matching.
	 * @param matches
	 *            A collection of trees containing all previously found matches. Is modified when a new match
	 *            is created.
	 * @param isRight
	 *            <ul>
	 *            <li> {@code true} if the matches are to be modified on the right side</li>
	 *            <li> {@code false} if the matches are to be modified on the origin side</li>
	 *            </ul>
	 * @return Objects which were not matched via the default id but for which an xmi id exists. This
	 *         collection can be used to perform a better matching for those elements.
	 */
	private List<EObject> performDefaultIDMatching(Iterator<? extends EObject> objects,
			List<EObject> noIDObjects, Map<String, Match> xmiIDToMatch, Map<String, Match> defaultIDToMatch,
			Map<EObject, Match> objectsToMatch, Set<Match> matches, boolean isRight) {

		List<EObject> sndPriorityMatches = new LinkedList<EObject>();

		while (objects.hasNext()) {
			final EObject object = objects.next();

			final String identifier = idComputation.apply(object);
			if (identifier == null) {
				noIDObjects.add(object);
				continue;
			}

			// Do we have an existing match via default id?
			Match match = defaultIDToMatch.get(identifier);
			if (match != null) {
				if (isRight) {
					match.setRight(object);
				} else {
					match.setOrigin(object);
				}

				objectsToMatch.put(object, match);
			} else {
				match = CompareFactory.eINSTANCE.createMatch();
				if (isRight) {
					match.setRight(object);
				} else {
					match.setOrigin(object);
				}

				// Can we find a parent?
				final EObject parentEObject = object.eContainer();
				final Match parent = objectsToMatch.get(parentEObject);
				if (parent != null) {
					parent.getSubmatches().add(match);
				} else {
					matches.add(match);
				}

				objectsToMatch.put(object, match);
				defaultIDToMatch.put(identifier, match);

				// objects which were not matched via default id can maybe be matched via xmi id. Save them
				// and try to match in a second round to not "steal" a default-id matching pair
				String xmiIdentifier = xmiIDComputation.apply(object);
				if (xmiIdentifier != null) {
					Match xmiIdentMatch = xmiIDToMatch.get(xmiIdentifier);
					if (xmiIdentMatch != null) {
						// A potential match exists. Try to match later to see if it's still viable
						sndPriorityMatches.add(object);
					} else {
						// No potential match exists. Add this match so a potential origin can be matched via
						// xmi id
						xmiIDToMatch.put(xmiIdentifier, match);
					}
				}
			}
		}

		return sndPriorityMatches;
	}

	/**
	 * Tries to match the elements of {@code objects} with low priority. This means if a possible match is
	 * found, the object will only be matched if the corresponding partner is not already matched.
	 *
	 * @param objects
	 *            The objects for which a low priority matching is tried.
	 * @param xmiIDToMatch
	 *            The mapping containing all possible matches via the xmi id.
	 * @param defaultIDToMatch
	 *            The mapping containing all possibles matches via the default id. Is modified when a better
	 *            matching is found.
	 * @param objectsToMatch
	 *            The result mapping of the high priority matching. Is modified when a better matching is
	 *            found.
	 * @param matches
	 *            Collection containing all root matches.
	 * @param isRight
	 *            <ul>
	 *            <li> {@code true} if the matches are to be modified on the right side</li>
	 *            <li> {@code false} if the matches are to be modified on the origin side</li>
	 *            </ul>
	 */
	private void performXMIIDMatching(Iterator<EObject> objects, Map<String, Match> xmiIDToMatch,
			Map<String, Match> defaultIDToMatch, Map<EObject, Match> objectsToMatch, Set<Match> matches,
			boolean isRight) {
		while (objects.hasNext()) {
			EObject object = objects.next();
			String xmiIdentifier = xmiIDComputation.apply(object);
			Match potentialMatch = xmiIDToMatch.get(xmiIdentifier);

			if ((isRight && potentialMatch.getRight() == null)
					|| (!isRight && potentialMatch.getOrigin() == null)) {

				// better match possible - change previous results
				Match emptyMatch = objectsToMatch.get(object);
				potentialMatch.getSubmatches().addAll(emptyMatch.getSubmatches());
				if (matches.contains(emptyMatch)) {
					matches.remove(emptyMatch);
				}
				EcoreUtil.delete(emptyMatch);

				// add new results
				if (isRight) {
					potentialMatch.setRight(object);
				} else {
					potentialMatch.setOrigin(object);
				}

				objectsToMatch.put(object, potentialMatch);

				String defaultIdentifier = idComputation.apply(object);
				defaultIDToMatch.put(defaultIdentifier, potentialMatch);
			}
		}
	}

	/**
	 * The default function used to retrieve IDs from EObject. You might want to extend or compose with it if
	 * you want to reuse its behavior.
	 */
	public static class DefaultIDFunction implements Function<EObject, String> {
		/**
		 * Return an ID for an EObject, null if not found.
		 * 
		 * @param eObject
		 *            The EObject for which we need an identifier.
		 * @return The identifier for that EObject if we could determine one. <code>null</code> if no
		 *         condition (see description above) is fulfilled for the given eObject.
		 */
		public String apply(EObject eObject) {
			final String identifier;
			if (eObject == null) {
				identifier = null;
			} else if (eObject.eIsProxy()) {
				identifier = ((InternalEObject)eObject).eProxyURI().fragment();
			} else {
				String functionalId = EcoreUtil.getID(eObject);
				if (functionalId != null) {
					identifier = functionalId;
				} else {
					final Resource eObjectResource = eObject.eResource();
					if (eObjectResource instanceof XMIResource) {
						identifier = ((XMIResource)eObjectResource).getID(eObject);
					} else {
						identifier = null;
					}
				}
			}
			return identifier;
		}
	}

	/**
	 * The function to retrieve XMI IDs from EObject.
	 * 
	 * @since 3.2
	 */
	public static class XMIIDFunction implements Function<EObject, String> {
		/**
		 * Return XMI ID for an EObject, null if not found.
		 * 
		 * @param eObject
		 *            The EObject for which we need an identifier.
		 * @return The XMI ID for that EObject if we could determine one, <code>null</code> otherwise.
		 */
		public String apply(EObject eObject) {
			if (eObject == null) {
				return null;
			}

			final String identifier;
			final Resource eObjectResource = eObject.eResource();
			if (eObjectResource instanceof XMIResource) {
				identifier = ((XMIResource)eObjectResource).getID(eObject);
			} else {
				identifier = null;
			}
			return identifier;
		}
	}
}
