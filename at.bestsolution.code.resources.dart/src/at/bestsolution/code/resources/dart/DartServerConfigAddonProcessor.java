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
