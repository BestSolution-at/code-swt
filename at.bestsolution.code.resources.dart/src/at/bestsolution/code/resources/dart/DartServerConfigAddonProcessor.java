/*******************************************************************************
* Copyright (c) 2016 BestSolution.at and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* 	Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
*******************************************************************************/
package at.bestsolution.code.resources.dart;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class DartServerConfigAddonProcessor {
	private static String ADDON_URI = "bundleclass://at.bestsolution.code.resources.dart/at.bestsolution.code.resources.dart.DartServerConfigAddon";
	@Execute
	public void addAddon(MApplication application, EModelService modelService) {
		if( !application.getAddons().stream().filter( a -> ADDON_URI.equals(a.getContributionURI()) ).findFirst().isPresent() ) {
			MAddon addon = modelService.createModelElement(MAddon.class);
			addon.setContributionURI(ADDON_URI);
			application.getAddons().add(addon);
		}
	}
}
