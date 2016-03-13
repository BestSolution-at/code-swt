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
package at.bestsolution.code.swt.langs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import at.bestsolution.code.swt.themes.Stylesheet;

@Component
public class EditorStylesheet implements Stylesheet {

	@Override
	public List<URL> getUrlList() {
		return Arrays.asList(
				createURL("adoc")
				, createURL("ceylon")
				, createURL("dart")
				, createURL("go")
				, createURL("groovy")
				, createURL("java")
				, createURL("js")
				, createURL("kotlin")
				, createURL("lua")
				, createURL("php")
				, createURL("py")
				, createURL("rust")
				, createURL("swift")
				, createURL("ts")
				, createURL("xml")
				);
	}

	private static URL createURL(String language) {
		try {
			return new URL("platform:/plugin/org.eclipse.fx.code.editor.langs/org/eclipse/fx/code/editor/ldef/langs/"+language+"-swt-style.json");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
