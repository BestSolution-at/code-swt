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
package at.bestsolution.code.swt.themes;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service=Theme.class)
public class StandardTheme extends Theme {

	public StandardTheme() {
		super("theme.default", StandardTheme.class.getClassLoader().getResource("/themes/standard.json"));
	}

	@Reference(cardinality=ReferenceCardinality.MULTIPLE)
	@Override
	public void registerStylesheet(Stylesheet s) {
		super.registerStylesheet(s);
	}

	@Override
	public void unregisterStylesheet(Stylesheet s) {
		super.unregisterStylesheet(s);
	}
}
