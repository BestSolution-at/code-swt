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
package at.bestsolution.code.swt.services.internal;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.osgi.service.component.annotations.Component;

import at.bestsolution.code.swt.services.SourceViewerConfigurationTypeProvider;

@SuppressWarnings("restriction")
@Component
public class DefaultSourceViewerConfigurationTypeProvider implements SourceViewerConfigurationTypeProvider {

	@Override
	public boolean test(Input<?> input) {
		return true;
	}

	@Override
	public Class<? extends SourceViewerConfiguration> getType(Input<?> input) {
		return DefaultSourceViewerConfiguration.class;
	}

}
