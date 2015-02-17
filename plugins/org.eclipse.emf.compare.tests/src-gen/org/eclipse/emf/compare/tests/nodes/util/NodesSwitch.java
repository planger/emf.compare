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
package org.eclipse.emf.compare.tests.nodes.util;

import java.util.List;

import org.eclipse.emf.compare.tests.nodes.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage
 * @generated
 */
public class NodesSwitch<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static NodesPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodesSwitch() {
		if (modelPackage == null) {
			modelPackage = NodesPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case NodesPackage.NODE: {
				Node node = (Node)theEObject;
				T result = caseNode(node);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT: {
				NodeMultipleContainment nodeMultipleContainment = (NodeMultipleContainment)theEObject;
				T result = caseNodeMultipleContainment(nodeMultipleContainment);
				if (result == null) result = caseNode(nodeMultipleContainment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT: {
				NodeSingleValueContainment nodeSingleValueContainment = (NodeSingleValueContainment)theEObject;
				T result = caseNodeSingleValueContainment(nodeSingleValueContainment);
				if (result == null) result = caseNode(nodeSingleValueContainment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_SINGLE_VALUE_ATTRIBUTE: {
				NodeSingleValueAttribute nodeSingleValueAttribute = (NodeSingleValueAttribute)theEObject;
				T result = caseNodeSingleValueAttribute(nodeSingleValueAttribute);
				if (result == null) result = caseNode(nodeSingleValueAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_MULTI_VALUED_ATTRIBUTE: {
				NodeMultiValuedAttribute nodeMultiValuedAttribute = (NodeMultiValuedAttribute)theEObject;
				T result = caseNodeMultiValuedAttribute(nodeMultiValuedAttribute);
				if (result == null) result = caseNode(nodeMultiValuedAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE: {
				NodeSingleValueReference nodeSingleValueReference = (NodeSingleValueReference)theEObject;
				T result = caseNodeSingleValueReference(nodeSingleValueReference);
				if (result == null) result = caseNode(nodeSingleValueReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE: {
				NodeMultiValueReference nodeMultiValueReference = (NodeMultiValueReference)theEObject;
				T result = caseNodeMultiValueReference(nodeMultiValueReference);
				if (result == null) result = caseNode(nodeMultiValueReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE: {
				NodeOppositeRefOneToOne nodeOppositeRefOneToOne = (NodeOppositeRefOneToOne)theEObject;
				T result = caseNodeOppositeRefOneToOne(nodeOppositeRefOneToOne);
				if (result == null) result = caseNode(nodeOppositeRefOneToOne);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY: {
				NodeOppositeRefOneToMany nodeOppositeRefOneToMany = (NodeOppositeRefOneToMany)theEObject;
				T result = caseNodeOppositeRefOneToMany(nodeOppositeRefOneToMany);
				if (result == null) result = caseNode(nodeOppositeRefOneToMany);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_OPPOSITE_REF_MANY_TO_MANY: {
				NodeOppositeRefManyToMany nodeOppositeRefManyToMany = (NodeOppositeRefManyToMany)theEObject;
				T result = caseNodeOppositeRefManyToMany(nodeOppositeRefManyToMany);
				if (result == null) result = caseNode(nodeOppositeRefManyToMany);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT: {
				NodeFeatureMapContainment nodeFeatureMapContainment = (NodeFeatureMapContainment)theEObject;
				T result = caseNodeFeatureMapContainment(nodeFeatureMapContainment);
				if (result == null) result = caseNode(nodeFeatureMapContainment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT: {
				NodeFeatureMapNonContainment nodeFeatureMapNonContainment = (NodeFeatureMapNonContainment)theEObject;
				T result = caseNodeFeatureMapNonContainment(nodeFeatureMapNonContainment);
				if (result == null) result = caseNode(nodeFeatureMapNonContainment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2: {
				NodeFeatureMapContainment2 nodeFeatureMapContainment2 = (NodeFeatureMapContainment2)theEObject;
				T result = caseNodeFeatureMapContainment2(nodeFeatureMapContainment2);
				if (result == null) result = caseNode(nodeFeatureMapContainment2);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE: {
				NodeSingleValueEEnumAttribute nodeSingleValueEEnumAttribute = (NodeSingleValueEEnumAttribute)theEObject;
				T result = caseNodeSingleValueEEnumAttribute(nodeSingleValueEEnumAttribute);
				if (result == null) result = caseNode(nodeSingleValueEEnumAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.NODE_MULTI_VALUE_EENUM_ATTRIBUTE: {
				NodeMultiValueEEnumAttribute nodeMultiValueEEnumAttribute = (NodeMultiValueEEnumAttribute)theEObject;
				T result = caseNodeMultiValueEEnumAttribute(nodeMultiValueEEnumAttribute);
				if (result == null) result = caseNode(nodeMultiValueEEnumAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NodesPackage.MONO_VALUED_NODE_FEATURE_MAP_NON_CONTAINMENT: {
				MonoValuedNodeFeatureMapNonContainment monoValuedNodeFeatureMapNonContainment = (MonoValuedNodeFeatureMapNonContainment)theEObject;
				T result = caseMonoValuedNodeFeatureMapNonContainment(monoValuedNodeFeatureMapNonContainment);
				if (result == null) result = caseNode(monoValuedNodeFeatureMapNonContainment);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNode(Node object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Multiple Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Multiple Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeMultipleContainment(NodeMultipleContainment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Single Value Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Single Value Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeSingleValueContainment(NodeSingleValueContainment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Single Value Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Single Value Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeSingleValueAttribute(NodeSingleValueAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Multi Valued Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Multi Valued Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeMultiValuedAttribute(NodeMultiValuedAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Single Value Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Single Value Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeSingleValueReference(NodeSingleValueReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Multi Value Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Multi Value Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeMultiValueReference(NodeMultiValueReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Opposite Ref One To One</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Opposite Ref One To One</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeOppositeRefOneToOne(NodeOppositeRefOneToOne object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Opposite Ref One To Many</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Opposite Ref One To Many</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeOppositeRefOneToMany(NodeOppositeRefOneToMany object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Opposite Ref Many To Many</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Opposite Ref Many To Many</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeOppositeRefManyToMany(NodeOppositeRefManyToMany object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Feature Map Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Feature Map Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeFeatureMapContainment(NodeFeatureMapContainment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Feature Map Non Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Feature Map Non Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeFeatureMapNonContainment(NodeFeatureMapNonContainment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Feature Map Containment2</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Feature Map Containment2</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeFeatureMapContainment2(NodeFeatureMapContainment2 object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Single Value EEnum Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Single Value EEnum Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeSingleValueEEnumAttribute(NodeSingleValueEEnumAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Multi Value EEnum Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Multi Value EEnum Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeMultiValueEEnumAttribute(NodeMultiValueEEnumAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mono Valued Node Feature Map Non Containment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mono Valued Node Feature Map Non Containment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMonoValuedNodeFeatureMapNonContainment(MonoValuedNodeFeatureMapNonContainment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //NodesSwitch
