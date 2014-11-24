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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Feature Map Attributes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapAttributes#getMapAttr <em>Map Attr</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapAttributes#getMultipleAttr <em>Multiple Attr</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapAttributes#getSingleAttr <em>Single Attr</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapAttributes()
 * @model
 * @generated
 */
public interface NodeFeatureMapAttributes extends Node {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Map Attr</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Attr</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Attr</em>' attribute list.
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapAttributes_MapAttr()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group'"
	 * @generated
	 */
	FeatureMap getMapAttr();

	/**
	 * Returns the value of the '<em><b>Multiple Attr</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Attr</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Attr</em>' reference.
	 * @see #setMultipleAttr(Node)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapAttributes_MultipleAttr()
	 * @model required="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#mapAttr'"
	 * @generated
	 */
	Node getMultipleAttr();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapAttributes#getMultipleAttr <em>Multiple Attr</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiple Attr</em>' reference.
	 * @see #getMultipleAttr()
	 * @generated
	 */
	void setMultipleAttr(Node value);

	/**
	 * Returns the value of the '<em><b>Single Attr</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Attr</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Attr</em>' reference.
	 * @see #setSingleAttr(Node)
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#getNodeFeatureMapAttributes_SingleAttr()
	 * @model required="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="group='#mapAttr'"
	 * @generated
	 */
	Node getSingleAttr();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.tests.nodes.NodeFeatureMapAttributes#getSingleAttr <em>Single Attr</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Attr</em>' reference.
	 * @see #getSingleAttr()
	 * @generated
	 */
	void setSingleAttr(Node value);

} // NodeFeatureMapAttributes
