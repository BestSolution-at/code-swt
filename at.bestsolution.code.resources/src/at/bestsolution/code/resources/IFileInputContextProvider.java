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
package at.bestsolution.code.resources;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.InputContext;
import org.eclipse.fx.code.editor.services.InputContextProvider;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("restriction")
@Component
public class IFileInputContextProvider implements InputContextProvider {

	@Override
	public boolean test(Input<?> t) {
		return t instanceof IFileSourceInput;
	}

	@Override
	public InputContext getContext(Input<?> input) {
		IFileSourceInput i = (IFileSourceInput) input;
		String id = i.getFile().getProject().getName();
		return new InputContext() {

			@Override
			public String getId() {
				return id;
			}
		};
	}

}
