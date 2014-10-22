/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.match;

import org.eclipse.emf.compare.rcp.internal.match.DefaultRCPMatchEngineFactory;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.AbstractConfigurationUI;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.IConfigurationUIFactory;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.Preferences;

/**
 * IConfiguratorUIFactory for {@link DefaultRCPMatchEngineFactory}
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class DefaultMatchEngineConfiguatorUIFactory implements IConfigurationUIFactory {

	/**
	 * {@inheritDoc}
	 */
	public AbstractConfigurationUI createUI(Composite parent, int style, Preferences pref) {
		DefaultMatchEngineConfiguratorUI composite = new DefaultMatchEngineConfiguratorUI(parent, style, pref);
		composite.createContent();
		return composite;
	}

}
