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
package at.bestsolution.code.resources;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.InputTypeProvider;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("restriction")
@Component(property="service.ranking:Integer=2")
public class IFileSourceInputTypeProvider implements InputTypeProvider {

	@Override
	public Class<? extends Input<?>> getType(String s) {
		return IFileSourceInput.class;
	}

	@Override
	public boolean test(String t) {
		return true;
	}

}
