package uk.co.zacgarby.mhm.graphics;

public class Colour {
	public static Colour WHITE = new Colour(1, 1, 1, 1);
	public static Colour BLACK = new Colour(0, 0, 0, 1);
	public static Colour TRANSPARENT = new Colour(0, 0, 0, 0);
	public static Colour ORANGE = new Colour(0xdfae26ff);
	public static Colour YELLOW = new Colour(0xeae352ff);
	public static Colour GREEN = new Colour(0x37946eff);
	public static Colour BLUE_GREEN = new Colour(0x306082ff);
	public static Colour LIGHT_RED = new Colour(0xd95763ff);
	public static Colour DARK_RED = new Colour(0xac3232ff);
	public static Colour PURPLE = new Colour(0x912e81ff);
	public static Colour MAGENTA = new Colour(0xa452b3ff);
	public static Colour LIGHT_BLUE = new Colour(0xcbdbfcff);
	
	public float r, g, b, a;
	
	public Colour(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Colour(int hex) {
		this.r = ((float) ((hex & 0xff000000) >> 24)) / 255f;
		this.g = ((float) ((hex & 0x00ff0000) >> 16)) / 255f;
		this.b = ((float) ((hex & 0x0000ff00) >> 8)) / 255f;
		this.a = ((float) ((hex & 0x000000ff) >> 0)) / 255f;
	}
	
	public byte getR() {
		return (byte) (r * 255);
	}
	
	public byte getG() {
		return (byte) (g * 255);
	}
	
	public byte getB() {
		return (byte) (b * 255);
	}
	
	public byte getA() {
		return (byte) (a * 255);
	}
	
	public void into(byte[] dst) {
		dst[0] = getR();
		dst[1] = getG();
		dst[2] = getB();
		dst[3] = getA();
	}
}
