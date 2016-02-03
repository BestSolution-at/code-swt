/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
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
