/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;

import com.google.common.base.Predicate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * A change factory for creating {@link OpaqueElementBodyChange changes of bodies} of {@link OpaqueAction
 * opaque actions}, {@link OpaqueBehavior opaque behaviors}, and {@link OpaqueExpression opaque expressions}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class UMLOpaqueElementBodyChangeFactory extends AbstractUMLChangeFactory {

	/** Predicate checking whether a {@link Diff} is an addition. */
	private static final Predicate<? super Diff> IS_ADD = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return ADD.equals(diff.getKind());
		}
	};

	/** Predicate checking whether a {@link Diff} is a deletion. */
	private static final Predicate<? super Diff> IS_DELETE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return DELETE.equals(diff.getKind());
		}
	};

	/** Predicate checking whether a {@link Diff} is a move. */
	private static final Predicate<? super Diff> IS_MOVE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return MOVE.equals(diff.getKind());
		}
	};

	@Override
	public boolean handles(Diff input) {
		final boolean handlesDiff;
		if (input instanceof AttributeChange && input.getRefines().isEmpty()) {
			final AttributeChange attributeChange = (AttributeChange)input;
			handlesDiff = isChangeOfBodyAttributeWithLanguage(attributeChange)
					|| isMoveOfLanguageAttributeValue(attributeChange);
		} else {
			handlesDiff = false;
		}
		return handlesDiff;
	}

	/**
	 * Specifies whether the given {@code diff} is a change of a body attribute value of an
	 * {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression} for which a corresponding
	 * language attribute value exists.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a change of a body value that has a language attribute value,
	 *         <code>false</code> otherwise.
	 */
	private boolean isChangeOfBodyAttributeWithLanguage(AttributeChange diff) {
		return isChangeOfBodyAttribute(diff) && affectsBodyWithLanguage(diff);
	}

	/**
	 * Specifies whether the given {@code diff} is a change of the body attribute of {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * 
	 * @param diff
	 *            The attribute change to check.
	 * @return <code>true</code> if it is a change of the body attribute, <code>false</code> otherwise.
	 */
	private boolean isChangeOfBodyAttribute(AttributeChange diff) {
		final EAttribute attribute = diff.getAttribute();
		return UMLPackage.eINSTANCE.getOpaqueAction_Body().equals(attribute)
				|| UMLPackage.eINSTANCE.getOpaqueBehavior_Body().equals(attribute)
				|| UMLPackage.eINSTANCE.getOpaqueExpression_Body().equals(attribute);
	}

	/**
	 * Specifies whether the given {@code diff} is a change of the language attribute of {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * 
	 * @param diff
	 *            The attribute change to check.
	 * @return <code>true</code> if it is a change of the language attribute, <code>false</code> otherwise.
	 */
	private boolean isChangeOfLanguageAttribute(AttributeChange diff) {
		final EAttribute attribute = diff.getAttribute();
		return UMLPackage.eINSTANCE.getOpaqueAction_Language().equals(attribute)
				|| UMLPackage.eINSTANCE.getOpaqueBehavior_Language().equals(attribute)
				|| UMLPackage.eINSTANCE.getOpaqueExpression_Language().equals(attribute);
	}

	/**
	 * Specifies whether the given {@code diff} affects a body value for which a language value exists or
	 * whether it affects a language value directly.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it affects a body value with a language value, <code>false</code>
	 *         otherwise.
	 */
	private boolean affectsBodyWithLanguage(AttributeChange diff) {
		return getAffectedLanguage(diff) != null;
	}

	/**
	 * Returns the language value of the body value that is changed by the given {@code diff} or, if
	 * {@code diff} represents a change of a language value directly, this method returns the changed language
	 * value.
	 * 
	 * @param diff
	 *            The difference to get the language value for.
	 * @return The language value.
	 */
	private String getAffectedLanguage(AttributeChange diff) {
		String language = null;
		if (isChangeOfBodyAttribute(diff)) {
			final EObject changedObject = getEObjectContainingChangedValue(diff);
			final List<String> languages = UMLCompareUtil.getOpaqueElementLanguages(changedObject);
			final List<String> bodies = UMLCompareUtil.getOpaqueElementBodies(changedObject);
			final int changedIndex = bodies.indexOf(diff.getValue());
			language = safeGet(languages, changedIndex);
		} else if (isChangeOfLanguageAttribute(diff)) {
			language = (String)diff.getValue();
		}
		return language;
	}

	/**
	 * Returns the object that contains the value changed by the given {@code diff} after the modification. If
	 * {@code diff} is a deletion, it will return the object that still contains the deleted value; that is,
	 * the object in the origin model or in the model of the opposite side of the deletion.
	 * 
	 * @param diff
	 *            The difference to get the object containing the changed value for.
	 * @return The object containing the changed value.
	 */
	private EObject getEObjectContainingChangedValue(AttributeChange diff) {
		final EObject changedObject;
		final Match match = diff.getMatch();
		if (ADD.equals(diff.getKind())) {
			if (DifferenceSource.LEFT.equals(diff.getSource())) {
				changedObject = match.getLeft();
			} else {
				changedObject = match.getRight();
			}
		} else {
			if (match.getComparison().isThreeWay()) {
				changedObject = match.getOrigin();
			} else {
				if (DifferenceSource.RIGHT.equals(diff.getSource())) {
					changedObject = match.getLeft();
				} else {
					changedObject = match.getRight();
				}
			}
		}
		return changedObject;
	}

	/**
	 * Returns the value at {@code index} from the given {@code list}.
	 * <p>
	 * If the index is out of bounds or -1, this method will return <code>null</code>.
	 * </p>
	 * 
	 * @param list
	 *            The list to get the value from.
	 * @param index
	 *            The index to get from the list.
	 * @return The value at index, or <code>null</code> if index is out of bounds or -1.
	 */
	private String safeGet(final List<String> list, final int index) {
		final String item;
		if (index != -1 && list.size() > index) {
			item = list.get(index);
		} else {
			item = null;
		}
		return item;
	}

	/**
	 * Specifies whether the given {@code diff} represents a move of a value of a language attribute.
	 * 
	 * @param diff
	 *            The {@code diff} to check.
	 * @return <code>true</code> if it is a move of a value in a language attribute.
	 */
	private boolean isMoveOfLanguageAttributeValue(AttributeChange diff) {
		return MOVE.equals(diff.getKind()) && isChangeOfLanguageAttribute(diff);
	}

	@Override
	public Diff create(Diff input) {
		final OpaqueElementBodyChange extension = (OpaqueElementBodyChange)super.create(input);
		extension.setLanguage(getAffectedLanguage((AttributeChange)input));
		extension.setKind(computeDifferenceKind(extension));
		extension.getImplies().addAll(extension.getRefinedBy());
		return extension;
	}

	/**
	 * Determines the difference kind of the given {@code bodyChange} based on its refining differences.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to get its difference kind.
	 * @return The difference kind of {@code bodyChange}.
	 */
	private DifferenceKind computeDifferenceKind(OpaqueElementBodyChange bodyChange) {
		final DifferenceKind differenceKind;
		final List<Diff> refiningDiffs = bodyChange.getRefinedBy();
		if (all(refiningDiffs, IS_ADD)) {
			differenceKind = ADD;
		} else if (all(refiningDiffs, IS_DELETE)) {
			differenceKind = DELETE;
		} else if (all(refiningDiffs, IS_MOVE)) {
			differenceKind = MOVE;
		} else {
			differenceKind = CHANGE;
		}
		return differenceKind;
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		final OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)extension;
		bodyChange.setLanguage(getLanguage((AttributeChange)refiningDiff));
		bodyChange.setSource(refiningDiff.getSource());
		bodyChange.getRefinedBy().add(refiningDiff);
		collectRefiningChanges(bodyChange);
	}

	/**
	 * Collects all {@link #isRefiningDiff(OpaqueElementBodyChange) refining differences} for the given
	 * {@code bodyChange} from all differences of the match of the {@code bodyChange} and adds it to the
	 * {@link Diff#getRefinedBy() refining differences} of {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to collect the refining changes for.
	 */
	private void collectRefiningChanges(OpaqueElementBodyChange bodyChange) {
		final Diff refiningDiff = bodyChange.getRefinedBy().get(0);
		final EList<Diff> differencesOfMatch = refiningDiff.getMatch().getDifferences();
		for (Diff otherRefiningDiff : filter(differencesOfMatch, isRefiningDiff(bodyChange))) {
			bodyChange.getRefinedBy().add(otherRefiningDiff);
		}
	}

	/**
	 * Returns a predicate determining whether a {@link Diff} is a difference that refines the given
	 * {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check whether it is refined by {@link Diff}.
	 * @return The predicate that can be used for checking whether a {@link Diff} refines {@code bodyChange}.
	 */
	private Predicate<Diff> isRefiningDiff(final OpaqueElementBodyChange bodyChange) {
		return new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				return diff instanceof AttributeChange
						&& isRefiningAttributeChange(bodyChange, (AttributeChange)diff);
			}
		};
	}

	/**
	 * Specifies whether {@code attributeChange} is a refinement of the given {@code bodyChange}.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check whether it is refined by
	 *            {@code attributeChange}.
	 * @param attributeChange
	 *            The {@link AttributeChange} to check whether it refines {@code bodyChange}.
	 * @return <code>true</code> if {@code attributeChange} is refining {@code bodyChange}, <code>false</code>
	 *         otherwise.
	 */
	private boolean isRefiningAttributeChange(final OpaqueElementBodyChange bodyChange,
			AttributeChange attributeChange) {
		return isChangeOfBodyOrLanguageAttribute(attributeChange) //
				&& isOnSameSide(bodyChange, attributeChange) //
				&& concernsSameObjectAndLanguage(bodyChange, attributeChange) //
				&& isCorrespondingChangeType(bodyChange, attributeChange);
	}

	/**
	 * Specifies whether two differences, {@code diff1} and {@code diff2} have the same difference source.
	 * 
	 * @param diff1
	 *            The first difference to check.
	 * @param diff2
	 *            The second difference to check.
	 * @return <code>true</code> if they have the same difference source, <code>false</code> otherwise.
	 */
	private boolean isOnSameSide(final Diff diff1, Diff diff2) {
		return diff1.getSource().equals(diff2.getSource());
	}

	/**
	 * Specifies whether {@code attributeChange} and {@code bodyChange} affect the same object and the same
	 * language.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check.
	 * @param attributeChange
	 *            The {@link AttributeChange} to check.
	 * @return <code>true</code> if {@code attributeChange} and {@code bodyChange} concern the same object and
	 *         language, <code>false</code> otherwise.
	 */
	private boolean concernsSameObjectAndLanguage(final OpaqueElementBodyChange bodyChange,
			AttributeChange attributeChange) {
		return concernsSameObject(bodyChange, attributeChange)
				&& concernsSameLanguage(bodyChange, attributeChange);
	}

	/**
	 * Specifies whether {@code attributeChange} and {@code bodyChange} affect the same container object.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check.
	 * @param attributeChange
	 *            The {@link AttributeChange} to check.
	 * @return <code>true</code> if {@code attributeChange} and {@code bodyChange} concern the same container
	 *         object, <code>false</code> otherwise.
	 */
	private boolean concernsSameObject(OpaqueElementBodyChange bodyChange, AttributeChange attributeChange) {
		final Diff refiningDiff = bodyChange.getRefinedBy().get(0);
		return refiningDiff.getMatch().equals(attributeChange.getMatch());
	}

	/**
	 * Specifies whether {@code attributeChange} and {@code bodyChange} affect the same language.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check.
	 * @param attributeChange
	 *            The {@link AttributeChange} to check.
	 * @return <code>true</code> if {@code attributeChange} and {@code bodyChange} concern the same language,
	 *         <code>false</code> otherwise.
	 */
	private boolean concernsSameLanguage(OpaqueElementBodyChange bodyChange, AttributeChange attributeChange) {
		return bodyChange.getLanguage().equals(getLanguage(attributeChange));
	}

	/**
	 * Returns the language of the given {@code attributeChange}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the language cannot be determined.
	 * @param attributeChange
	 *            The {@link AttributeChange} to get the language for.
	 * @return The language of the {@code attributeChange}.
	 */
	private String getLanguage(AttributeChange attributeChange) {
		if (isChangeOfBodyAttribute(attributeChange)) {
			return getAffectedLanguage(attributeChange);
		} else if (isChangeOfLanguageAttribute(attributeChange)) {
			return (String)attributeChange.getValue();
		} else {
			throw new IllegalArgumentException("Cannot get language for " + attributeChange); //$NON-NLS-1$
		}
	}

	/**
	 * Specifies whether {@code attributeChange} and {@code bodyChange} have corresponding change types (i.e.,
	 * add, delete, move, change). The change types correspond either if both are moves or if both are
	 * anything else except for moves.
	 * 
	 * @param bodyChange
	 *            The {@link OpaqueElementBodyChange} to check.
	 * @param attributeChange
	 *            The {@link AttributeChange} to check.
	 * @return <code>true</code> if the change types correspond to each other, <code>false</code> otherwise.
	 */
	private boolean isCorrespondingChangeType(OpaqueElementBodyChange bodyChange,
			AttributeChange attributeChange) {
		final boolean isCorrespondingChangeType;
		final Diff refiningDiff = bodyChange.getRefinedBy().get(0);
		if (MOVE.equals(refiningDiff.getKind())) {
			isCorrespondingChangeType = MOVE.equals(attributeChange.getKind());
		} else {
			isCorrespondingChangeType = !MOVE.equals(attributeChange.getKind());
		}
		return isCorrespondingChangeType;
	}

	/**
	 * Specifies whether the given {@code attributeChange} is either a change of the body attribute or a
	 * change of the language attribute of an {@link OpaqueAction}, {@link OpaqueBehavior}, or
	 * {@link OpaqueExpression}.
	 * 
	 * @param attributeChange
	 *            The {@link AttributeChange} to check.
	 * @return <code>true</code> if it is a body change or a language change, <code>false</code> otherwise.
	 */
	protected boolean isChangeOfBodyOrLanguageAttribute(AttributeChange attributeChange) {
		return isChangeOfBodyAttribute(attributeChange) || isChangeOfLanguageAttribute(attributeChange);
	}

	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return OpaqueElementBodyChange.class;
	}

	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createOpaqueElementBodyChange();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected EObject getDiscriminant(Diff input) {
		return find(getDiscriminants(input), or(instanceOf(OpaqueAction.class),
				instanceOf(OpaqueBehavior.class), instanceOf(OpaqueExpression.class)), null);
	}

	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new DiscriminantsGetter() {
			@Override
			public Set<EObject> caseOpaqueAction(OpaqueAction object) {
				return getObjectAsSet(object);
			}

			@Override
			public Set<EObject> caseOpaqueBehavior(OpaqueBehavior object) {
				return getObjectAsSet(object);
			}

			@Override
			public Set<EObject> caseOpaqueExpression(OpaqueExpression object) {
				return getObjectAsSet(object);
			}

			private Set<EObject> getObjectAsSet(EObject object) {
				Set<EObject> result = new HashSet<EObject>();
				result.add(object);
				return result;
			}
		};
	}

}
