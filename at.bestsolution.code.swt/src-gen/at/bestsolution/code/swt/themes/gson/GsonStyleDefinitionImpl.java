package at.bestsolution.code.swt.themes.gson;

import at.bestsolution.code.swt.themes.*;
import com.google.gson.JsonObject;

public final class GsonStyleDefinitionImpl implements GsonBase, StyleDefinition {
	public GsonStyleDefinitionImpl(JsonObject jsonObject) {
		this.bold = jsonObject.has("bold") ? jsonObject.get("bold").getAsBoolean() : false;
		this.italic = jsonObject.has("italic") ? jsonObject.get("italic").getAsBoolean() : false;
		this.name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
		this.textRefColor = jsonObject.has("textRefColor") ? jsonObject.get("textRefColor").getAsString() : null;
	}
	public GsonStyleDefinitionImpl(boolean bold, boolean italic, String name, String textRefColor) {
		this.bold = bold;
		this.italic = italic;
		this.name = name;
		this.textRefColor = textRefColor;
	}

	public JsonObject toJSONObject() {
		JsonObject o = new JsonObject();
		o.addProperty( "$gtype", "StyleDefinition" );
		o.addProperty( "bold", isBold() );
		o.addProperty( "italic", isItalic() );
		o.addProperty( "name", getName() );
		o.addProperty( "textRefColor", getTextRefColor() );
		return o;
	}

	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " { "
					 + "bold : " + bold + ", "
					 + "italic : " + italic + ", "
					 + "name : " + name + ", "
					 + "textRefColor : " + textRefColor
					+" }";
	}

	private final boolean bold;
	public boolean isBold() {
		return this.bold;
	}
	

	private final boolean italic;
	public boolean isItalic() {
		return this.italic;
	}
	

	private final String name;
	public String getName() {
		return this.name;
	}
	

	private final String textRefColor;
	public String getTextRefColor() {
		return this.textRefColor;
	}
	


	public static class Builder implements StyleDefinition.Builder {
		private final ThemeGModel instance;

		public Builder(ThemeGModel instance) {
			this.instance = instance;
		}
		private boolean bold;
		public Builder bold(boolean bold) {
			this.bold = bold;
			return this;
		}
		private boolean italic;
		public Builder italic(boolean italic) {
			this.italic = italic;
			return this;
		}
		private String name;
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		private String textRefColor;
		public Builder textRefColor(String textRefColor) {
			this.textRefColor = textRefColor;
			return this;
		}

		public StyleDefinition build() {
			return new GsonStyleDefinitionImpl(bold, italic, name, textRefColor);
		}
	}
}
