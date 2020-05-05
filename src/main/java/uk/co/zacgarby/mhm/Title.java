package uk.co.zacgarby.mhm;

public class Title {
	public static final Title WIZARD = new Title("Wizard", 'w');
	public static final Title PYROMANCER = new Title("Pyromancer", 'p');
	public static final Title VOIDCASTER = new Title("Voidcaster", 'v');
	public static final Title ALCHEMIST = new Title("Alchemist", 'a');
	public static final Title DRUID = new Title("Druid", 'd');
	public static final Title ORACLE = new Title("Oracle", 'o');
	public static final Title WEATHERMAN = new Title("Weatherman", 'W');
	public static final Title TRICKSTER = new Title("Trickster", 't');
	public static final Title PUPPETEER = new Title("Puppeteer", 'P');
	
	private String title;
	private char fontChar;
	
	public Title(String title, char fontChar) {
		this.title = title;
		this.fontChar = fontChar;
	}

	public String getTitle() {
		return title;
	}

	public char getFontChar() {
		return fontChar;
	}
}
