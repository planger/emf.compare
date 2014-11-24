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

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mono Valued Node Feature Map Non Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment#getMonoMap <em>Mono Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment#getFirstMonoKey <em>First Mono Key</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment#getSecondMonoKey <em>Second Mono Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getMonoValuedNodeFeatureMapNonContainment()
 * @model
 * @generated
 */
public interface MonoValuedNodeFeatureMapNonContainment extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Mono Map</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mono Map</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mono Map</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getMonoValuedNodeFeatureMapNonContainment_MonoMap()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getMonoMap();

	/**
	 * Returns the value of the '<em><b>First Mono Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Mono Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>First Mono Key</em>' reference.
	 * @see #setFirstMonoKey(Node)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getMonoValuedNodeFeatureMapNonContainment_FirstMonoKey()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#monoMap'"
	 * @generated
	 */
	Node getFirstMonoKey();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment#getFirstMonoKey <em>First Mono Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>First Mono Key</em>' reference.
	 * @see #getFirstMonoKey()
	 * @generated
	 */
	void setFirstMonoKey(Node value);

	/**
	 * Returns the value of the '<em><b>Second Mono Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Second Mono Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Second Mono Key</em>' reference.
	 * @see #setSecondMonoKey(Node)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getMonoValuedNodeFeatureMapNonContainment_SecondMonoKey()
	 * @model transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#monoMap'"
	 * @generated
	 */
	Node getSecondMonoKey();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.MonoValuedNodeFeatureMapNonContainment#getSecondMonoKey <em>Second Mono Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Second Mono Key</em>' reference.
	 * @see #getSecondMonoKey()
	 * @generated
	 */
	void setSecondMonoKey(Node value);

} // MonoValuedNodeFeatureMapNonContainment
