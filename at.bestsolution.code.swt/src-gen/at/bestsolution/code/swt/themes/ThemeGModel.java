package at.bestsolution.code.swt.themes;

public interface ThemeGModel {
	public static ThemeGModel create() {
		return new at.bestsolution.code.swt.themes.gson.GsonElementFactory();
	}

	public <T extends ThemeBase> T createObject(java.io.Reader json);
	public <T extends ThemeBase> java.util.List<T> createList(java.io.Reader json);

	public String toString(ThemeBase o);
	public String toString(java.util.List<ThemeBase> o);

	public StylesheetDefinition.Builder StylesheetDefinitionBuilder();
	public StyleDefinition.Builder StyleDefinitionBuilder();
	public ColorDefinition.Builder ColorDefinitionBuilder();
}
