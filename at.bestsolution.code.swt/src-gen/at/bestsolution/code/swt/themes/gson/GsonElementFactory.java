package at.bestsolution.code.swt.themes.gson;

import at.bestsolution.code.swt.themes.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public final class GsonElementFactory implements ThemeGModel {
	public static StylesheetDefinition createStylesheetDefinition(JsonObject o) {
		if( o.has("$gtype") ) {
			switch( o.get("$gtype").getAsString() ) {
				case "StylesheetDefinition":
					return new GsonStylesheetDefinitionImpl(o);
				default:
					throw new IllegalStateException();
			}
		} else {
			return new GsonStylesheetDefinitionImpl(o);
		}
	}
	public StylesheetDefinition.Builder StylesheetDefinitionBuilder() {
		return new GsonStylesheetDefinitionImpl.Builder(this);
	}

	public static StyleDefinition createStyleDefinition(JsonObject o) {
		if( o.has("$gtype") ) {
			switch( o.get("$gtype").getAsString() ) {
				case "StyleDefinition":
					return new GsonStyleDefinitionImpl(o);
				default:
					throw new IllegalStateException();
			}
		} else {
			return new GsonStyleDefinitionImpl(o);
		}
	}
	public StyleDefinition.Builder StyleDefinitionBuilder() {
		return new GsonStyleDefinitionImpl.Builder(this);
	}

	public static ColorDefinition createColorDefinition(JsonObject o) {
		if( o.has("$gtype") ) {
			switch( o.get("$gtype").getAsString() ) {
				case "ColorDefinition":
					return new GsonColorDefinitionImpl(o);
				default:
					throw new IllegalStateException();
			}
		} else {
			return new GsonColorDefinitionImpl(o);
		}
	}
	public ColorDefinition.Builder ColorDefinitionBuilder() {
		return new GsonColorDefinitionImpl.Builder(this);
	}



	public <T extends ThemeBase> T createObject(java.io.Reader json) {
		JsonObject o = new com.google.gson.Gson().fromJson( json, JsonObject.class);
		return _createObject(o);
	}

	@SuppressWarnings("unchecked")
	private static <T extends ThemeBase> T _createObject(JsonObject o) {
		if( o.has("$gtype") ) {
			switch( o.get("$gtype").getAsString() ) {
				case "StylesheetDefinition":
					return (T) createStylesheetDefinition(o);
				case "StyleDefinition":
					return (T) createStyleDefinition(o);
				case "ColorDefinition":
					return (T) createColorDefinition(o);
			}
		}
		return (T) createStylesheetDefinition(o);
	}

	public <T extends ThemeBase> java.util.List<T> createList(java.io.Reader json) {
		JsonArray ar = new com.google.gson.Gson().fromJson( json, JsonArray.class);
		return java.util.stream.StreamSupport.stream(ar.spliterator(), false)
			.map( e -> (JsonObject)e)
			.map( GsonElementFactory::<T>_createObject)
			.collect(java.util.stream.Collectors.toList());
	}

	public String toString(ThemeBase o) {
		return new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson( ((GsonBase)o).toJSONObject() );
	}

	public String toString(java.util.List<ThemeBase> o) {
		return new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson( GsonBase.toDomainJsonArray(o) );
	}
}
