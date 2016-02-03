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
