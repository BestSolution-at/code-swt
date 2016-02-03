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
package at.bestsolution.code.swt.themes;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.swt.graphics.RGB;

public abstract class Theme {
	private final String id;
	private final Map<String,RGB> colorDefinitions = new HashMap<>();
	private final Map<String, StyleDefinition> styleDefinitions = new HashMap<>();

	public Theme(String id, URL baseStylesheetUrl) {
		this.id = id;
		loadStylesheet(baseStylesheetUrl);
	}

	private void loadStylesheet(URL url) {
		try( InputStreamReader r = new InputStreamReader(url.openStream()) ) {
			StylesheetDefinition theme = ThemeGModel.create().createObject(r);
			this.colorDefinitions.putAll(theme.getColorList().stream().collect(Collectors.toMap( c -> c.getName(), c -> createFromString(c.getRgb()))));
			this.styleDefinitions.putAll(theme.getElementList().stream().collect(Collectors.toMap( c -> c.getName(), c -> c)));
		} catch (Exception e) {
			LoggerCreator.createLogger(Theme.class).error("Could not load stylesheet '"+url+"'", e);
		}
	}

	private void unloadStylesheet(URL url) {
		try( InputStreamReader r = new InputStreamReader(url.openStream()) ) {
			StylesheetDefinition theme = ThemeGModel.create().createObject(r);
			theme.getColorList().stream().map(ColorDefinition::getName).forEach(this.colorDefinitions::remove);
			theme.getElementList().stream().map(StyleDefinition::getName).forEach(this.styleDefinitions::remove);
		} catch (Exception e) {
			LoggerCreator.createLogger(Theme.class).error("Could not load stylesheet '"+url+"'", e);
		}
	}

	private static RGB createFromString(String colorDef) {
		if( colorDef.startsWith("#") ) {
			String red;
			String green;
			String blue;
			if( colorDef.length() == 4 ) {
				red = colorDef.substring(1,2);
				red += red;

				green = colorDef.substring(2,3);
				green += green;

				blue = colorDef.substring(3);
				blue += green;
			} else {
				red = colorDef.substring(1,3);
				green = colorDef.substring(3,5);
				blue = colorDef.substring(5);
			}
			return new RGB(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue,16));
		} else if( colorDef.startsWith("rgb") ) {
			String[] parts = colorDef.trim().split(",");
			String red = parts[0].substring(4).trim();
			String green = parts[1].trim();
			String blue = parts[2].substring(0, parts[2].length()-1).trim();
			return new RGB(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
		}
		return new RGB(0, 0, 0);
	}

	public String getId() {
		return id;
	}

	public void registerStylesheet(Stylesheet s) {
		s.getUrlList().forEach( this::loadStylesheet );
	}

	public void unregisterStylesheet(Stylesheet s) {
		s.getUrlList().forEach( this::unloadStylesheet );
	}

	public RGB getColor(String stylename) {
		StyleDefinition definition = styleDefinitions.get(stylename);

		if( definition != null ) {
			String color = definition.getTextRefColor();
			if( color != null ) {
				return colorDefinitions.get(color);
			}
		}

		return null;
	}

	public boolean boldFont(String stylename) {
		StyleDefinition definition = styleDefinitions.get(stylename);
		if( definition != null ) {
			return definition.isBold();
		}
		return false;
	}

	public boolean italicFont(String stylename) {
		StyleDefinition definition = styleDefinitions.get(stylename);
		if( definition != null ) {
			return definition.isItalic();
		}
		return false;
	}
}
