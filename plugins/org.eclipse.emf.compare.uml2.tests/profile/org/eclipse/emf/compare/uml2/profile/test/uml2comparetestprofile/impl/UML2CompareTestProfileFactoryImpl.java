/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl;

import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfileFactory;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class UML2CompareTestProfileFactoryImpl extends EFactoryImpl implements UML2CompareTestProfileFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static UML2CompareTestProfileFactory init() {
		try {
			UML2CompareTestProfileFactory theUML2CompareTestProfileFactory = (UML2CompareTestProfileFactory)EPackage.Registry.INSTANCE
					.getEFactory(UML2CompareTestProfilePackage.eNS_URI);
			if (theUML2CompareTestProfileFactory != null) {
				return theUML2CompareTestProfileFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UML2CompareTestProfileFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public UML2CompareTestProfileFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UML2CompareTestProfilePackage.ACLICHE:
				return createACliche();
			case UML2CompareTestProfilePackage.ACLICHE2:
				return createACliche2();
			case UML2CompareTestProfilePackage.ACLICHE3:
				return createACliche3();
			default:
				throw new IllegalArgumentException(
						"The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ACliche createACliche() {
		AClicheImpl aCliche = new AClicheImpl();
		return aCliche;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ACliche2 createACliche2() {
		ACliche2Impl aCliche2 = new ACliche2Impl();
		return aCliche2;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ACliche3 createACliche3() {
		ACliche3Impl aCliche3 = new ACliche3Impl();
		return aCliche3;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public UML2CompareTestProfilePackage getUML2CompareTestProfilePackage() {
		return (UML2CompareTestProfilePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UML2CompareTestProfilePackage getPackage() {
		return UML2CompareTestProfilePackage.eINSTANCE;
	}

} // UML2CompareTestProfileFactoryImpl
