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

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mono Valued Node Feature Map Non Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.MonoValuedNodeFeatureMapNonContainmentImpl#getMonoMap <em>Mono Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.MonoValuedNodeFeatureMapNonContainmentImpl#getFirstMonoKey <em>First Mono Key</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.MonoValuedNodeFeatureMapNonContainmentImpl#getSecondMonoKey <em>Second Mono Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MonoValuedNodeFeatureMapNonContainmentImpl extends NodeImpl implements MonoValuedNodeFeatureMapNonContainment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMonoMap() <em>Mono Map</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMonoMap()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap monoMap;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonoValuedNodeFeatureMapNonContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMonoMap() {
		if (monoMap == null) {
			monoMap = new BasicFeatureMap(this, NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP);
		}
		return monoMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node getFirstMonoKey() {
		Node firstMonoKey = basicGetFirstMonoKey();
		return firstMonoKey != null && firstMonoKey.eIsProxy() ? (Node)eResolveProxy((InternalEObject)firstMonoKey) : firstMonoKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node basicGetFirstMonoKey() {
		return (Node)getMonoMap().get(NodesPackage.Literals.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFirstMonoKey(Node newFirstMonoKey) {
		((FeatureMap.Internal)getMonoMap()).set(NodesPackage.Literals.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY, newFirstMonoKey);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node getSecondMonoKey() {
		Node secondMonoKey = basicGetSecondMonoKey();
		return secondMonoKey != null && secondMonoKey.eIsProxy() ? (Node)eResolveProxy((InternalEObject)secondMonoKey) : secondMonoKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node basicGetSecondMonoKey() {
		return (Node)getMonoMap().get(NodesPackage.Literals.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecondMonoKey(Node newSecondMonoKey) {
		((FeatureMap.Internal)getMonoMap()).set(NodesPackage.Literals.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY, newSecondMonoKey);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP:
				return ((InternalEList<?>)getMonoMap()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP:
				if (coreType) return getMonoMap();
				return ((FeatureMap.Internal)getMonoMap()).getWrapper();
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY:
				if (resolve) return getFirstMonoKey();
				return basicGetFirstMonoKey();
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY:
				if (resolve) return getSecondMonoKey();
				return basicGetSecondMonoKey();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP:
				((FeatureMap.Internal)getMonoMap()).set(newValue);
				return;
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY:
				setFirstMonoKey((Node)newValue);
				return;
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY:
				setSecondMonoKey((Node)newValue);
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
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP:
				getMonoMap().clear();
				return;
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY:
				setFirstMonoKey((Node)null);
				return;
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY:
				setSecondMonoKey((Node)null);
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
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__MONO_MAP:
				return monoMap != null && !monoMap.isEmpty();
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_MONO_KEY:
				return basicGetFirstMonoKey() != null;
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_MONO_KEY:
				return basicGetSecondMonoKey() != null;
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
		result.append(" (monoMap: "); //$NON-NLS-1$
		result.append(monoMap);
		result.append(')');
		return result.toString();
	}

} //MonoValuedNodeFeatureMapNonContainmentImpl
