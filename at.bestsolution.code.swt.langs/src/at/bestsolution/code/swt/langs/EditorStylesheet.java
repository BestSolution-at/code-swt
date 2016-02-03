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
