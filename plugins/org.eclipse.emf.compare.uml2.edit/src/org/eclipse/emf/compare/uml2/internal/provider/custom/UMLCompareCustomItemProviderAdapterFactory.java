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
package org.eclipse.emf.compare.uml2.internal.provider.custom;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.Disposable;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers. The adapters
 * generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged
 * fireNotifyChanged}. The adapters also support Eclipse property sheets. Note that most of the adapters are
 * shared among multiple instances.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareCustomItemProviderAdapterFactory extends UMLCompareAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {

	/**
	 * This keeps track of all the item providers created, so that they can be {@link #dispose disposed}.
	 */
	protected Disposable disposable = new Disposable();

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 */
	private ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 */
	private IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 */
	private Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 */
	public UMLCompareCustomItemProviderAdapterFactory() {
		supportedTypes.add(IItemStyledLabelProvider.class);
		supportedTypes.add(IItemDescriptionProvider.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory#createStereotypeApplicationChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeApplicationChangeAdapter() {
		return new StereotypeApplicationChangeCustomItemProvider(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory#createStereotypeAttributeChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeAttributeChangeAdapter() {
		return new StereotypeAttributeChangeCustomItemProvider(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory#createStereotypeReferenceChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		return new StereotypeReferenceChangeCustomItemProvider(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory#createUMLDiffAdapter()
	 */
	@Override
	public Adapter createUMLDiffAdapter() {
		return new UMLDiffCustomItemProvider(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ComposeableAdapterFactory#getRootAdapterFactory()
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		if (parentAdapterFactory == null) {
			return this;
		} else {
			return parentAdapterFactory.getRootAdapterFactory();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ComposeableAdapterFactory#setParentAdapterFactory(org.eclipse.emf.edit.provider.ComposedAdapterFactory)
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.util.UMLCompareAdapterFactory#isFactoryForType(java.lang.Object)
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterFactoryImpl#adapt(org.eclipse.emf.common.notify.Notifier,
	 *      java.lang.Object)
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterFactoryImpl#adapt(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#addListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#removeListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#fireNotifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterFactoryImpl#associate(Adapter, Notifier)
	 */
	@Override
	protected void associate(Adapter adapter, Notifier target) {
		super.associate(adapter, target);
		if (adapter != null) {
			disposable.add(adapter);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
	 */
	public void dispose() {
		disposable.dispose();
	}

}
