/**
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes.impl;

import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment;
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment2;
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapNonContainment;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValueReference;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValuedAttribute;
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefManyToMany;
import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToMany;
import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueContainment;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueReference;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class NodesPackageImpl extends EPackageImpl implements NodesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeMultipleContainmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeSingleValueContainmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeSingleValueAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeMultiValuedAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeSingleValueReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeMultiValueReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeOppositeRefOneToOneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeOppositeRefOneToManyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeOppositeRefManyToManyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeFeatureMapContainmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeFeatureMapNonContainmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeFeatureMapContainment2EClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.tests.nodes.NodesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private NodesPackageImpl() {
		super(eNS_URI, NodesFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link NodesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static NodesPackage init() {
		if (isInited) return (NodesPackage)EPackage.Registry.INSTANCE.getEPackage(NodesPackage.eNS_URI);

		// Obtain or create and register package
		NodesPackageImpl theNodesPackage = (NodesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof NodesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new NodesPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theNodesPackage.createPackageContents();

		// Initialize created meta-data
		theNodesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theNodesPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, theNodesPackage);
		return theNodesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNode() {
		return nodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNode_Name() {
		return (EAttribute)nodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNode_ContainmentRef1() {
		return (EReference)nodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeMultipleContainment() {
		return nodeMultipleContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeMultipleContainment_ContainmentRef2() {
		return (EReference)nodeMultipleContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeMultipleContainment_ContainmentRef3() {
		return (EReference)nodeMultipleContainmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeSingleValueContainment() {
		return nodeSingleValueContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeSingleValueContainment_SingleValueContainment() {
		return (EReference)nodeSingleValueContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeSingleValueAttribute() {
		return nodeSingleValueAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNodeSingleValueAttribute_SingleValuedAttribute() {
		return (EAttribute)nodeSingleValueAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeMultiValuedAttribute() {
		return nodeMultiValuedAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNodeMultiValuedAttribute_MultiValuedAttribute() {
		return (EAttribute)nodeMultiValuedAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeSingleValueReference() {
		return nodeSingleValueReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeSingleValueReference_SingleValuedReference() {
		return (EReference)nodeSingleValueReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeMultiValueReference() {
		return nodeMultiValueReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeMultiValueReference_MultiValuedReference() {
		return (EReference)nodeMultiValueReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeOppositeRefOneToOne() {
		return nodeOppositeRefOneToOneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefOneToOne_Source() {
		return (EReference)nodeOppositeRefOneToOneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefOneToOne_Destination() {
		return (EReference)nodeOppositeRefOneToOneEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeOppositeRefOneToMany() {
		return nodeOppositeRefOneToManyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefOneToMany_Source() {
		return (EReference)nodeOppositeRefOneToManyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefOneToMany_Destination() {
		return (EReference)nodeOppositeRefOneToManyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeOppositeRefManyToMany() {
		return nodeOppositeRefManyToManyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefManyToMany_Source() {
		return (EReference)nodeOppositeRefManyToManyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeOppositeRefManyToMany_Destination() {
		return (EReference)nodeOppositeRefManyToManyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeFeatureMapContainment() {
		return nodeFeatureMapContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNodeFeatureMapContainment_Map() {
		return (EAttribute)nodeFeatureMapContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapContainment_FirstKey() {
		return (EReference)nodeFeatureMapContainmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapContainment_SecondKey() {
		return (EReference)nodeFeatureMapContainmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeFeatureMapNonContainment() {
		return nodeFeatureMapNonContainmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNodeFeatureMapNonContainment_MapNC() {
		return (EAttribute)nodeFeatureMapNonContainmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapNonContainment_FirstKeyNC() {
		return (EReference)nodeFeatureMapNonContainmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapNonContainment_SecondKeyNC() {
		return (EReference)nodeFeatureMapNonContainmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeFeatureMapContainment2() {
		return nodeFeatureMapContainment2EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNodeFeatureMapContainment2_Map2() {
		return (EAttribute)nodeFeatureMapContainment2EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapContainment2_Multiple() {
		return (EReference)nodeFeatureMapContainment2EClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNodeFeatureMapContainment2_Single() {
		return (EReference)nodeFeatureMapContainment2EClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodesFactory getNodesFactory() {
		return (NodesFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		nodeEClass = createEClass(NODE);
		createEAttribute(nodeEClass, NODE__NAME);
		createEReference(nodeEClass, NODE__CONTAINMENT_REF1);

		nodeMultipleContainmentEClass = createEClass(NODE_MULTIPLE_CONTAINMENT);
		createEReference(nodeMultipleContainmentEClass, NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2);
		createEReference(nodeMultipleContainmentEClass, NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3);

		nodeSingleValueContainmentEClass = createEClass(NODE_SINGLE_VALUE_CONTAINMENT);
		createEReference(nodeSingleValueContainmentEClass, NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT);

		nodeSingleValueAttributeEClass = createEClass(NODE_SINGLE_VALUE_ATTRIBUTE);
		createEAttribute(nodeSingleValueAttributeEClass, NODE_SINGLE_VALUE_ATTRIBUTE__SINGLE_VALUED_ATTRIBUTE);

		nodeMultiValuedAttributeEClass = createEClass(NODE_MULTI_VALUED_ATTRIBUTE);
		createEAttribute(nodeMultiValuedAttributeEClass, NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE);

		nodeSingleValueReferenceEClass = createEClass(NODE_SINGLE_VALUE_REFERENCE);
		createEReference(nodeSingleValueReferenceEClass, NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE);

		nodeMultiValueReferenceEClass = createEClass(NODE_MULTI_VALUE_REFERENCE);
		createEReference(nodeMultiValueReferenceEClass, NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE);

		nodeOppositeRefOneToOneEClass = createEClass(NODE_OPPOSITE_REF_ONE_TO_ONE);
		createEReference(nodeOppositeRefOneToOneEClass, NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE);
		createEReference(nodeOppositeRefOneToOneEClass, NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION);

		nodeOppositeRefOneToManyEClass = createEClass(NODE_OPPOSITE_REF_ONE_TO_MANY);
		createEReference(nodeOppositeRefOneToManyEClass, NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE);
		createEReference(nodeOppositeRefOneToManyEClass, NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION);

		nodeOppositeRefManyToManyEClass = createEClass(NODE_OPPOSITE_REF_MANY_TO_MANY);
		createEReference(nodeOppositeRefManyToManyEClass, NODE_OPPOSITE_REF_MANY_TO_MANY__SOURCE);
		createEReference(nodeOppositeRefManyToManyEClass, NODE_OPPOSITE_REF_MANY_TO_MANY__DESTINATION);

		nodeFeatureMapContainmentEClass = createEClass(NODE_FEATURE_MAP_CONTAINMENT);
		createEAttribute(nodeFeatureMapContainmentEClass, NODE_FEATURE_MAP_CONTAINMENT__MAP);
		createEReference(nodeFeatureMapContainmentEClass, NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY);
		createEReference(nodeFeatureMapContainmentEClass, NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY);

		nodeFeatureMapNonContainmentEClass = createEClass(NODE_FEATURE_MAP_NON_CONTAINMENT);
		createEAttribute(nodeFeatureMapNonContainmentEClass, NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC);
		createEReference(nodeFeatureMapNonContainmentEClass, NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC);
		createEReference(nodeFeatureMapNonContainmentEClass, NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC);

		nodeFeatureMapContainment2EClass = createEClass(NODE_FEATURE_MAP_CONTAINMENT2);
		createEAttribute(nodeFeatureMapContainment2EClass, NODE_FEATURE_MAP_CONTAINMENT2__MAP2);
		createEReference(nodeFeatureMapContainment2EClass, NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE);
		createEReference(nodeFeatureMapContainment2EClass, NODE_FEATURE_MAP_CONTAINMENT2__SINGLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		nodeMultipleContainmentEClass.getESuperTypes().add(this.getNode());
		nodeSingleValueContainmentEClass.getESuperTypes().add(this.getNode());
		nodeSingleValueAttributeEClass.getESuperTypes().add(this.getNode());
		nodeMultiValuedAttributeEClass.getESuperTypes().add(this.getNode());
		nodeSingleValueReferenceEClass.getESuperTypes().add(this.getNode());
		nodeMultiValueReferenceEClass.getESuperTypes().add(this.getNode());
		nodeOppositeRefOneToOneEClass.getESuperTypes().add(this.getNode());
		nodeOppositeRefOneToManyEClass.getESuperTypes().add(this.getNode());
		nodeOppositeRefManyToManyEClass.getESuperTypes().add(this.getNode());
		nodeFeatureMapContainmentEClass.getESuperTypes().add(this.getNode());
		nodeFeatureMapNonContainmentEClass.getESuperTypes().add(this.getNode());
		nodeFeatureMapContainment2EClass.getESuperTypes().add(this.getNode());

		// Initialize classes and features; add operations and parameters
		initEClass(nodeEClass, Node.class, "Node", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNode_Name(), ecorePackage.getEString(), "name", null, 0, 1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNode_ContainmentRef1(), this.getNode(), null, "containmentRef1", null, 0, -1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeMultipleContainmentEClass, NodeMultipleContainment.class, "NodeMultipleContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeMultipleContainment_ContainmentRef2(), this.getNode(), null, "containmentRef2", null, 0, -1, NodeMultipleContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeMultipleContainment_ContainmentRef3(), this.getNode(), null, "containmentRef3", null, 0, -1, NodeMultipleContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeSingleValueContainmentEClass, NodeSingleValueContainment.class, "NodeSingleValueContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeSingleValueContainment_SingleValueContainment(), this.getNode(), null, "singleValueContainment", null, 0, 1, NodeSingleValueContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeSingleValueAttributeEClass, NodeSingleValueAttribute.class, "NodeSingleValueAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNodeSingleValueAttribute_SingleValuedAttribute(), ecorePackage.getEString(), "singleValuedAttribute", null, 0, 1, NodeSingleValueAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeMultiValuedAttributeEClass, NodeMultiValuedAttribute.class, "NodeMultiValuedAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNodeMultiValuedAttribute_MultiValuedAttribute(), ecorePackage.getEString(), "multiValuedAttribute", null, 0, -1, NodeMultiValuedAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeSingleValueReferenceEClass, NodeSingleValueReference.class, "NodeSingleValueReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeSingleValueReference_SingleValuedReference(), this.getNode(), null, "singleValuedReference", null, 0, 1, NodeSingleValueReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeMultiValueReferenceEClass, NodeMultiValueReference.class, "NodeMultiValueReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeMultiValueReference_MultiValuedReference(), this.getNode(), null, "multiValuedReference", null, 0, -1, NodeMultiValueReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeOppositeRefOneToOneEClass, NodeOppositeRefOneToOne.class, "NodeOppositeRefOneToOne", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeOppositeRefOneToOne_Source(), this.getNodeOppositeRefOneToOne(), this.getNodeOppositeRefOneToOne_Destination(), "source", null, 0, 1, NodeOppositeRefOneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeOppositeRefOneToOne_Destination(), this.getNodeOppositeRefOneToOne(), this.getNodeOppositeRefOneToOne_Source(), "destination", null, 0, 1, NodeOppositeRefOneToOne.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeOppositeRefOneToManyEClass, NodeOppositeRefOneToMany.class, "NodeOppositeRefOneToMany", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeOppositeRefOneToMany_Source(), this.getNodeOppositeRefOneToMany(), this.getNodeOppositeRefOneToMany_Destination(), "source", null, 0, 1, NodeOppositeRefOneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeOppositeRefOneToMany_Destination(), this.getNodeOppositeRefOneToMany(), this.getNodeOppositeRefOneToMany_Source(), "destination", null, 0, -1, NodeOppositeRefOneToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeOppositeRefManyToManyEClass, NodeOppositeRefManyToMany.class, "NodeOppositeRefManyToMany", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNodeOppositeRefManyToMany_Source(), this.getNodeOppositeRefManyToMany(), this.getNodeOppositeRefManyToMany_Destination(), "source", null, 0, -1, NodeOppositeRefManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeOppositeRefManyToMany_Destination(), this.getNodeOppositeRefManyToMany(), this.getNodeOppositeRefManyToMany_Source(), "destination", null, 0, -1, NodeOppositeRefManyToMany.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeFeatureMapContainmentEClass, NodeFeatureMapContainment.class, "NodeFeatureMapContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNodeFeatureMapContainment_Map(), ecorePackage.getEFeatureMapEntry(), "map", null, 0, -1, NodeFeatureMapContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapContainment_FirstKey(), this.getNode(), null, "firstKey", null, 0, -1, NodeFeatureMapContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapContainment_SecondKey(), this.getNode(), null, "secondKey", null, 0, -1, NodeFeatureMapContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeFeatureMapNonContainmentEClass, NodeFeatureMapNonContainment.class, "NodeFeatureMapNonContainment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNodeFeatureMapNonContainment_MapNC(), ecorePackage.getEFeatureMapEntry(), "mapNC", null, 0, -1, NodeFeatureMapNonContainment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapNonContainment_FirstKeyNC(), this.getNode(), null, "firstKeyNC", null, 0, -1, NodeFeatureMapNonContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapNonContainment_SecondKeyNC(), this.getNode(), null, "secondKeyNC", null, 0, -1, NodeFeatureMapNonContainment.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(nodeFeatureMapContainment2EClass, NodeFeatureMapContainment2.class, "NodeFeatureMapContainment2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNodeFeatureMapContainment2_Map2(), ecorePackage.getEFeatureMapEntry(), "map2", null, 0, -1, NodeFeatureMapContainment2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapContainment2_Multiple(), this.getNodeMultipleContainment(), null, "multiple", null, 0, -1, NodeFeatureMapContainment2.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNodeFeatureMapContainment2_Single(), this.getNodeSingleValueContainment(), null, "single", null, 0, -1, NodeFeatureMapContainment2.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$		
		addAnnotation
		  (getNodeFeatureMapContainment_Map(), 
		   source, 
		   new String[] {
			 "kind", "group" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapContainment_FirstKey(), 
		   source, 
		   new String[] {
			 "group", "#map" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapContainment_SecondKey(), 
		   source, 
		   new String[] {
			 "group", "#map" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapNonContainment_MapNC(), 
		   source, 
		   new String[] {
			 "kind", "group" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapNonContainment_FirstKeyNC(), 
		   source, 
		   new String[] {
			 "group", "#mapNC" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapNonContainment_SecondKeyNC(), 
		   source, 
		   new String[] {
			 "group", "#mapNC" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapContainment2_Map2(), 
		   source, 
		   new String[] {
			 "kind", "group" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapContainment2_Multiple(), 
		   source, 
		   new String[] {
			 "group", "#map2" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getNodeFeatureMapContainment2_Single(), 
		   source, 
		   new String[] {
			 "group", "#map2" //$NON-NLS-1$ //$NON-NLS-2$
		   });
	}

} //NodesPackageImpl
