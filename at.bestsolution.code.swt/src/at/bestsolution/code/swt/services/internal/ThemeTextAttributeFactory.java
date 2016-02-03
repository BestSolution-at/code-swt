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
package at.bestsolution.code.swt.services.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import at.bestsolution.code.swt.services.TextAttributeFactory;
import at.bestsolution.code.swt.themes.Theme;
import at.bestsolution.code.swt.themes.ThemeManager;

@Component
public class ThemeTextAttributeFactory implements TextAttributeFactory, ThemeManager {
	private Theme currentTheme;
	private List<Theme> themeList = new ArrayList<>();

	@Override
	public void setTheme(String themeId) {
		this.currentTheme = themeList.stream().filter( t -> themeId.equals(t.getId())).findFirst().orElse(null);
	}

	@Reference(cardinality=ReferenceCardinality.MULTIPLE,policy=ReferencePolicy.DYNAMIC)
	public void registerTheme(Theme theme) {
		if( this.currentTheme == null ) {
			this.currentTheme = theme;
		}
		themeList.add(theme);
	}

	public void unregisterTheme(Theme theme) {
		themeList.remove(theme);
	}

	@Override
	public TextAttribute getAttribute(String language, String tokenName) {
		if( currentTheme != null ) {
			RGB rgb = currentTheme.getColor(language + "." + tokenName);
			if( rgb != null ) {
				Color c = JFaceResources.getColorRegistry().get(rgb.toString());
				if( c == null ) {
					JFaceResources.getColorRegistry().put(rgb.toString(), rgb);
					c = JFaceResources.getColorRegistry().get(rgb.toString());
				}
				int style = SWT.NORMAL;

				if( currentTheme.boldFont(language + "." + tokenName) ) {
					style |= SWT.BOLD;
				}
				if( currentTheme.italicFont(language + "." + tokenName) ) {
					style |= SWT.ITALIC;
				}

				return new TextAttribute(c, null, style);
			}
		}
		return new TextAttribute(null);
	}


}