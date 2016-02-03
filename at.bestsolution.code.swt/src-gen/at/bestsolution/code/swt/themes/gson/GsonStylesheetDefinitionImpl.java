package at.bestsolution.code.swt.themes.gson;

import at.bestsolution.code.swt.themes.*;
import com.google.gson.JsonObject;

public final class GsonStylesheetDefinitionImpl implements GsonBase, StylesheetDefinition {
	public GsonStylesheetDefinitionImpl(JsonObject jsonObject) {
		this.colorList = jsonObject.has("colorList") ? java.util.Collections.unmodifiableList(java.util.stream.StreamSupport.stream( jsonObject.getAsJsonArray("colorList").spliterator(), false )
								.map( e -> GsonElementFactory.createColorDefinition(e.getAsJsonObject())).collect(java.util.stream.Collectors.toList())) : java.util.Collections.emptyList();
		this.elementList = jsonObject.has("elementList") ? java.util.Collections.unmodifiableList(java.util.stream.StreamSupport.stream( jsonObject.getAsJsonArray("elementList").spliterator(), false )
								.map( e -> GsonElementFactory.createStyleDefinition(e.getAsJsonObject())).collect(java.util.stream.Collectors.toList())) : java.util.Collections.emptyList();
	}
	public GsonStylesheetDefinitionImpl(java.util.List<ColorDefinition> colorList, java.util.List<StyleDefinition> elementList) {
		this.colorList = colorList;
		this.elementList = elementList;
	}

	public JsonObject toJSONObject() {
		JsonObject o = new JsonObject();
		o.addProperty( "$gtype", "StylesheetDefinition" );
		o.add( "colorList", GsonBase.toDomainJsonArray(getColorList()) );
		o.add( "elementList", GsonBase.toDomainJsonArray(getElementList()) );
		return o;
	}

	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " { "
					 + "colorList : " + colorList.stream().map( e -> e.getClass().getSimpleName() + "@" + Integer.toHexString(e.hashCode()) ).collect(java.util.stream.Collectors.toList()) + ", "
					 + "elementList : " + elementList.stream().map( e -> e.getClass().getSimpleName() + "@" + Integer.toHexString(e.hashCode()) ).collect(java.util.stream.Collectors.toList())
					+" }";
	}

	private final java.util.List<ColorDefinition> colorList;
	public java.util.List<ColorDefinition> getColorList() {
		return this.colorList;
	}
	

	private final java.util.List<StyleDefinition> elementList;
	public java.util.List<StyleDefinition> getElementList() {
		return this.elementList;
	}
	


	public static class Builder implements StylesheetDefinition.Builder {
		private final ThemeGModel instance;

		public Builder(ThemeGModel instance) {
			this.instance = instance;
		}
		private final java.util.List<ColorDefinition> colorList = new java.util.ArrayList<>();
		public Builder colorList(java.util.List<ColorDefinition> colorList) {
			this.colorList.addAll(colorList);
			return this;
		}
		public Builder appendColorList(ColorDefinition colorList) {
			this.colorList.add(colorList);
			return this;
		}
		public Builder colorList(java.util.function.Function<ThemeGModel,java.util.List<ColorDefinition>> provider) {
			colorList( provider.apply( instance ) );
			return this;
		}

		public Builder appendColorList(java.util.function.Function<ColorDefinition.Builder,ColorDefinition> provider) {
			appendColorList( provider.apply( new GsonColorDefinitionImpl.Builder(instance) ) );
			return this;
		}
		private final java.util.List<StyleDefinition> elementList = new java.util.ArrayList<>();
		public Builder elementList(java.util.List<StyleDefinition> elementList) {
			this.elementList.addAll(elementList);
			return this;
		}
		public Builder appendElementList(StyleDefinition elementList) {
			this.elementList.add(elementList);
			return this;
		}
		public Builder elementList(java.util.function.Function<ThemeGModel,java.util.List<StyleDefinition>> provider) {
			elementList( provider.apply( instance ) );
			return this;
		}

		public Builder appendElementList(java.util.function.Function<StyleDefinition.Builder,StyleDefinition> provider) {
			appendElementList( provider.apply( new GsonStyleDefinitionImpl.Builder(instance) ) );
			return this;
		}

		public StylesheetDefinition build() {
			return new GsonStylesheetDefinitionImpl(colorList, elementList);
		}
	}
}
