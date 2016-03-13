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

import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.ResourcesPlugin;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.DartServerFactory;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;

public class DartServerConfigAddon {
	@PostConstruct
	void init(DartServerFactory serverFactory) {
		Stream.of(ResourcesPlugin.getWorkspace().getRoot().getProjects()).forEach( p -> {
			DartServer server = serverFactory.getServer(p.getName());
			server.getService(ServiceAnalysis.class).setAnalysisRoots(new String[] { p.getLocation().toFile().toString() }, new String[0], null);
		});
	}
}
