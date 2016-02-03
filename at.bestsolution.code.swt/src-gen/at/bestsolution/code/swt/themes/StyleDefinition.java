package at.bestsolution.code.swt.themes;

public interface StyleDefinition extends ThemeBase {
	public String getName();
	public String getTextRefColor();
	public boolean isBold();
	public boolean isItalic();

	public interface Builder {
		public Builder name(String name);
		public Builder textRefColor(String textRefColor);
		public Builder bold(boolean bold);
		public Builder italic(boolean italic);
		public StyleDefinition build();
	}
}
