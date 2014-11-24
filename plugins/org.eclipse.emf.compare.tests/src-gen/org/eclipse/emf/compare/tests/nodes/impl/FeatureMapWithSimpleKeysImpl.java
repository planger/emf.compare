/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.nodes.FeatureMapWithSimpleKeys;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Map With Simple Keys</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.FeatureMapWithSimpleKeysImpl#getSimpleMap <em>Simple Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.FeatureMapWithSimpleKeysImpl#getFirstAttribute <em>First Attribute</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.FeatureMapWithSimpleKeysImpl#getSecondAttribute <em>Second Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureMapWithSimpleKeysImpl extends NodeImpl implements FeatureMapWithSimpleKeys {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSimpleMap() <em>Simple Map</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleMap()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap simpleMap;

	/**
	 * The cached value of the '{@link #getFirstAttribute() <em>First Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFirstAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<String> firstAttribute;

	/**
	 * The cached value of the '{@link #getSecondAttribute() <em>Second Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecondAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<String> secondAttribute;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureMapWithSimpleKeysImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.FEATURE_MAP_WITH_SIMPLE_KEYS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getSimpleMap() {
		if (simpleMap == null) {
			simpleMap = new BasicFeatureMap(this, NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP);
		}
		return simpleMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getFirstAttribute() {
		if (firstAttribute == null) {
			firstAttribute = new EDataTypeUniqueEList<String>(String.class, this, NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__FIRST_ATTRIBUTE);
		}
		return firstAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getSecondAttribute() {
		if (secondAttribute == null) {
			secondAttribute = new EDataTypeUniqueEList<String>(String.class, this, NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SECOND_ATTRIBUTE);
		}
		return secondAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP:
				return ((InternalEList<?>)getSimpleMap()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP:
				if (coreType) return getSimpleMap();
				return ((FeatureMap.Internal)getSimpleMap()).getWrapper();
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__FIRST_ATTRIBUTE:
				return getFirstAttribute();
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SECOND_ATTRIBUTE:
				return getSecondAttribute();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP:
				((FeatureMap.Internal)getSimpleMap()).set(newValue);
				return;
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__FIRST_ATTRIBUTE:
				getFirstAttribute().clear();
				getFirstAttribute().addAll((Collection<? extends String>)newValue);
				return;
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SECOND_ATTRIBUTE:
				getSecondAttribute().clear();
				getSecondAttribute().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP:
				getSimpleMap().clear();
				return;
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__FIRST_ATTRIBUTE:
				getFirstAttribute().clear();
				return;
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SECOND_ATTRIBUTE:
				getSecondAttribute().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SIMPLE_MAP:
				return simpleMap != null && !simpleMap.isEmpty();
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__FIRST_ATTRIBUTE:
				return firstAttribute != null && !firstAttribute.isEmpty();
			case NodesPackage.FEATURE_MAP_WITH_SIMPLE_KEYS__SECOND_ATTRIBUTE:
				return secondAttribute != null && !secondAttribute.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (simpleMap: "); //$NON-NLS-1$
		result.append(simpleMap);
		result.append(", firstAttribute: "); //$NON-NLS-1$
		result.append(firstAttribute);
		result.append(", secondAttribute: "); //$NON-NLS-1$
		result.append(secondAttribute);
		result.append(')');
		return result.toString();
	}

} //FeatureMapWithSimpleKeysImpl
