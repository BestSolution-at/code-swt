package at.bestsolution.code.swt.themes.gson;

import at.bestsolution.code.swt.themes.*;
import com.google.gson.JsonObject;

public final class GsonColorDefinitionImpl implements GsonBase, ColorDefinition {
	public GsonColorDefinitionImpl(JsonObject jsonObject) {
		this.name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
		this.rgb = jsonObject.has("rgb") ? jsonObject.get("rgb").getAsString() : null;
	}
	public GsonColorDefinitionImpl(String name, String rgb) {
		this.name = name;
		this.rgb = rgb;
	}

	public JsonObject toJSONObject() {
		JsonObject o = new JsonObject();
		o.addProperty( "$gtype", "ColorDefinition" );
		o.addProperty( "name", getName() );
		o.addProperty( "rgb", getRgb() );
		return o;
	}

	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " { "
					 + "name : " + name + ", "
					 + "rgb : " + rgb
					+" }";
	}

	private final String name;
	public String getName() {
		return this.name;
	}
	

	private final String rgb;
	public String getRgb() {
		return this.rgb;
	}
	


	public static class Builder implements ColorDefinition.Builder {
		private final ThemeGModel instance;

		public Builder(ThemeGModel instance) {
			this.instance = instance;
		}
		private String name;
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		private String rgb;
		public Builder rgb(String rgb) {
			this.rgb = rgb;
			return this;
		}

		public ColorDefinition build() {
			return new GsonColorDefinitionImpl(name, rgb);
		}
	}
}
