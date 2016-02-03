package at.bestsolution.code.swt.themes;

public interface ColorDefinition extends ThemeBase {
	public String getName();
	public String getRgb();

	public interface Builder {
		public Builder name(String name);
		public Builder rgb(String rgb);
		public ColorDefinition build();
	}
}
