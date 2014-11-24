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
package org.eclipse.emf.compare.tests.nodes;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Map With Simple Keys</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.FeatureMapWithSimpleKeys#getSimpleMap <em>Simple Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.FeatureMapWithSimpleKeys#getFirstAttribute <em>First Attribute</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.FeatureMapWithSimpleKeys#getSecondAttribute <em>Second Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getFeatureMapWithSimpleKeys()
 * @model
 * @generated
 */
public interface FeatureMapWithSimpleKeys extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Simple Map</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Simple Map</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Map</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getFeatureMapWithSimpleKeys_SimpleMap()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getSimpleMap();

	/**
	 * Returns the value of the '<em><b>First Attribute</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>First Attribute</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getFeatureMapWithSimpleKeys_FirstAttribute()
	 * @model extendedMetaData="group='#simpleMap'"
	 * @generated
	 */
	EList<String> getFirstAttribute();

	/**
	 * Returns the value of the '<em><b>Second Attribute</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Second Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Second Attribute</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getFeatureMapWithSimpleKeys_SecondAttribute()
	 * @model extendedMetaData="group='#simpleMap'"
	 * @generated
	 */
	EList<String> getSecondAttribute();

} // FeatureMapWithSimpleKeys
