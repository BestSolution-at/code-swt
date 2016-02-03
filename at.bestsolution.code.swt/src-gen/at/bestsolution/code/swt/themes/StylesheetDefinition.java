package at.bestsolution.code.swt.themes;

public interface StylesheetDefinition extends ThemeBase {
	public java.util.List<ColorDefinition> getColorList();
	public java.util.List<StyleDefinition> getElementList();

	public interface Builder {
		public Builder colorList(java.util.List<ColorDefinition> colorList);
		public Builder appendColorList(ColorDefinition colorList);
		public Builder colorList(java.util.function.Function<ThemeGModel,java.util.List<ColorDefinition>> provider);
		public Builder appendColorList(java.util.function.Function<ColorDefinition.Builder,ColorDefinition> provider);
		public Builder elementList(java.util.List<StyleDefinition> elementList);
		public Builder appendElementList(StyleDefinition elementList);
		public Builder elementList(java.util.function.Function<ThemeGModel,java.util.List<StyleDefinition>> provider);
		public Builder appendElementList(java.util.function.Function<StyleDefinition.Builder,StyleDefinition> provider);
		public StylesheetDefinition build();
	}
}
