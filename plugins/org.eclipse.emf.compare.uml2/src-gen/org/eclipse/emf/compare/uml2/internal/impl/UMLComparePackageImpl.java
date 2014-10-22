/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.impl;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2.internal.ExtendChange;
import org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.internal.MessageChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.internal.StereotypedElementChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLComparePackage;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class UMLComparePackageImpl extends EPackageImpl implements UMLComparePackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass associationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extendChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generalizationSetChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass executionSpecificationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass intervalConstraintChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass messageChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereotypeAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereotypeApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereotypeReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass directedRelationshipChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDiffEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereotypedElementChangeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UMLComparePackageImpl() {
		super(eNS_URI, UMLCompareFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link UMLComparePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UMLComparePackage init() {
		if (isInited) return (UMLComparePackage)EPackage.Registry.INSTANCE.getEPackage(UMLComparePackage.eNS_URI);

		// Obtain or create and register package
		UMLComparePackageImpl theUMLComparePackage = (UMLComparePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UMLComparePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UMLComparePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ComparePackage.eINSTANCE.eClass();
		UMLPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theUMLComparePackage.createPackageContents();

		// Initialize created meta-data
		theUMLComparePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUMLComparePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UMLComparePackage.eNS_URI, theUMLComparePackage);
		return theUMLComparePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssociationChange() {
		return associationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExtendChange() {
		return extendChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneralizationSetChange() {
		return generalizationSetChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExecutionSpecificationChange() {
		return executionSpecificationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIntervalConstraintChange() {
		return intervalConstraintChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMessageChange() {
		return messageChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereotypeAttributeChange() {
		return stereotypeAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereotypeApplicationChange() {
		return stereotypeApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStereotypeApplicationChange_Stereotype() {
		return (EReference)stereotypeApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereotypeReferenceChange() {
		return stereotypeReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfileApplicationChange() {
		return profileApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfileApplicationChange_Profile() {
		return (EReference)profileApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDirectedRelationshipChange() {
		return directedRelationshipChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDiff() {
		return umlDiffEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLDiff_Discriminant() {
		return (EReference)umlDiffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLDiff_EReference() {
		return (EReference)umlDiffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereotypedElementChange() {
		return stereotypedElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLCompareFactory getUMLCompareFactory() {
		return (UMLCompareFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		associationChangeEClass = createEClass(ASSOCIATION_CHANGE);

		extendChangeEClass = createEClass(EXTEND_CHANGE);

		generalizationSetChangeEClass = createEClass(GENERALIZATION_SET_CHANGE);

		executionSpecificationChangeEClass = createEClass(EXECUTION_SPECIFICATION_CHANGE);

		intervalConstraintChangeEClass = createEClass(INTERVAL_CONSTRAINT_CHANGE);

		messageChangeEClass = createEClass(MESSAGE_CHANGE);

		stereotypeAttributeChangeEClass = createEClass(STEREOTYPE_ATTRIBUTE_CHANGE);

		stereotypeApplicationChangeEClass = createEClass(STEREOTYPE_APPLICATION_CHANGE);
		createEReference(stereotypeApplicationChangeEClass, STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE);

		stereotypeReferenceChangeEClass = createEClass(STEREOTYPE_REFERENCE_CHANGE);

		profileApplicationChangeEClass = createEClass(PROFILE_APPLICATION_CHANGE);
		createEReference(profileApplicationChangeEClass, PROFILE_APPLICATION_CHANGE__PROFILE);

		directedRelationshipChangeEClass = createEClass(DIRECTED_RELATIONSHIP_CHANGE);

		umlDiffEClass = createEClass(UML_DIFF);
		createEReference(umlDiffEClass, UML_DIFF__DISCRIMINANT);
		createEReference(umlDiffEClass, UML_DIFF__EREFERENCE);

		stereotypedElementChangeEClass = createEClass(STEREOTYPED_ELEMENT_CHANGE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		UMLPackage theUMLPackage = (UMLPackage)EPackage.Registry.INSTANCE.getEPackage(UMLPackage.eNS_URI);
		ComparePackage theComparePackage = (ComparePackage)EPackage.Registry.INSTANCE.getEPackage(ComparePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		associationChangeEClass.getESuperTypes().add(this.getUMLDiff());
		extendChangeEClass.getESuperTypes().add(this.getUMLDiff());
		generalizationSetChangeEClass.getESuperTypes().add(this.getUMLDiff());
		executionSpecificationChangeEClass.getESuperTypes().add(this.getUMLDiff());
		intervalConstraintChangeEClass.getESuperTypes().add(this.getUMLDiff());
		messageChangeEClass.getESuperTypes().add(this.getUMLDiff());
		stereotypeAttributeChangeEClass.getESuperTypes().add(this.getUMLDiff());
		stereotypeApplicationChangeEClass.getESuperTypes().add(this.getUMLDiff());
		stereotypeReferenceChangeEClass.getESuperTypes().add(this.getUMLDiff());
		profileApplicationChangeEClass.getESuperTypes().add(this.getUMLDiff());
		directedRelationshipChangeEClass.getESuperTypes().add(this.getUMLDiff());
		umlDiffEClass.getESuperTypes().add(theComparePackage.getDiff());
		stereotypedElementChangeEClass.getESuperTypes().add(this.getUMLDiff());

		// Initialize classes and features; add operations and parameters
		initEClass(associationChangeEClass, AssociationChange.class, "AssociationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(extendChangeEClass, ExtendChange.class, "ExtendChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(generalizationSetChangeEClass, GeneralizationSetChange.class, "GeneralizationSetChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(executionSpecificationChangeEClass, ExecutionSpecificationChange.class, "ExecutionSpecificationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(intervalConstraintChangeEClass, IntervalConstraintChange.class, "IntervalConstraintChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(messageChangeEClass, MessageChange.class, "MessageChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(stereotypeAttributeChangeEClass, StereotypeAttributeChange.class, "StereotypeAttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(stereotypeApplicationChangeEClass, StereotypeApplicationChange.class, "StereotypeApplicationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getStereotypeApplicationChange_Stereotype(), theUMLPackage.getStereotype(), null, "stereotype", null, 0, 1, StereotypeApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(stereotypeReferenceChangeEClass, StereotypeReferenceChange.class, "StereotypeReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(profileApplicationChangeEClass, ProfileApplicationChange.class, "ProfileApplicationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getProfileApplicationChange_Profile(), theUMLPackage.getProfile(), null, "profile", null, 0, 1, ProfileApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(directedRelationshipChangeEClass, DirectedRelationshipChange.class, "DirectedRelationshipChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDiffEClass, UMLDiff.class, "UMLDiff", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLDiff_Discriminant(), theEcorePackage.getEObject(), null, "discriminant", null, 0, 1, UMLDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getUMLDiff_EReference(), theEcorePackage.getEReference(), null, "eReference", null, 0, 1, UMLDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(stereotypedElementChangeEClass, StereotypedElementChange.class, "StereotypedElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // UMLComparePackageImpl
