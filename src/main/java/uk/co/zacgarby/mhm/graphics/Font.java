package uk.co.zacgarby.mhm.graphics;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.util.HashMap;
import java.util.Map;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

public class Font {
	public static Font NORMAL, NORMAL_GRAD, XP, NAME, CLASSES;
	
	private Map<Character, TextureRegion> map = new HashMap<>();
	private Texture source;
	private int space;
	private boolean caseSensitive;
	
	public Font(Texture source, String chars,
			int[] widths, int spaceWidth, int gap, boolean caseSensitive, Colour c1, Colour c2) {
		this.source = source;
		this.space = spaceWidth;
		this.caseSensitive = caseSensitive;
		
		int x = 0, i = 0;
		
		for (char c : chars.toCharArray()) {
			if (!caseSensitive) {
				c = Character.toLowerCase(c);
			}
			
			TextureRegion reg = new TextureRegion(x, 0, widths[i], source.getHeight(), source);
			x += widths[i++] + gap;
			
			map.put(c, reg);
		}
		
		if (!(c1 == null || c2 == null)) {
			try (MemoryStack stack = stackPush()) {
				ByteBuffer pixels = stack.malloc(4 * source.getWidth() * source.getHeight());
				ByteBuffer repl = stack.malloc(4 * source.getWidth() * source.getHeight());

				source.bind();
				glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

				byte[] rgba = new byte[4];

				for (i = 0; i < pixels.capacity(); i += 4) {
					pixels.get(rgba, 0, 4);

					if (rgba[0] == -1 && rgba[1] == -1 && rgba[2] == -1 && rgba[3] == -1) {
						c1.into(rgba);
					} else if (rgba[0] == 0 && rgba[1] == 0 && rgba[2] == 0 && rgba[3] == -1) {
						c2.into(rgba);
					}

					repl.put(rgba);
				}

				repl.flip();

				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, source.getWidth(), source.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, repl);
			}
		}
	}
	
	public void draw(Batch batch, String text, int x, int y) {
		if (!caseSensitive) {
			text = text.toLowerCase();
		}
		
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				x += space;
				continue;
			}
			
			if (!caseSensitive) {
				c = Character.toLowerCase(c);
			}
			
			TextureRegion reg = map.get(c);
			if (reg == null) {
				throw new RuntimeException("Tried to draw unknown character: " + c);
			}
			
			batch.draw(reg, x, y);
			
			x += reg.getWidth() + 1;
		}
	}
	
	public void drawCentered(Batch batch, String text, int x, int y) {
		int width = measure(text);
		
		draw(batch, text, x - width/2, y);
	}
	
	public void drawRight(Batch batch, String text, int x, int y) {
		int width = measure(text);
		
		draw(batch, text, x - width, y);
	}
	
	public int measure(String text) {
		if (!caseSensitive) {
			text = text.toLowerCase();
		}
		
		int width = 0;
		
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				width += space;
				continue;
			}
			
			TextureRegion reg = map.get(c);
			if (reg == null) {
				throw new RuntimeException("Tried to draw unknown character: " + c);
			}
			
			width += reg.getWidth() + 1;
		}
		
		return width - 1;
	}

	public Texture getSource() {
		return source;
	}
	
	public static void load() {
		Font.NORMAL = new Font(new Texture("resources/fonts/font.png"),
				"abcdefghijklmnopqrstuvwxyz0123456789!.,:;()[]-+*÷/",
				new int[] {4, 4, 4, 4, 3, 3, 4, 4, 3, 4, 4, 3, 5, 5, 4, 4, 5, 4, 4, 3, 4, 5, 5, 4, 5, 6,
						4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3},
				3, 1, false, Colour.WHITE, Colour.WHITE);
		
		Font.NORMAL_GRAD = new Font(new Texture("resources/fonts/font.png"),
				"abcdefghijklmnopqrstuvwxyz0123456789!.,:;()[]-+*÷/",
				new int[] {4, 4, 4, 4, 3, 3, 4, 4, 3, 4, 4, 3, 5, 5, 4, 4, 5, 4, 4, 3, 4, 5, 5, 4, 5, 6,
						4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3},
				3, 1, false, Colour.WHITE, Colour.LIGHT_BLUE);
		
		Font.XP = new Font(new Texture("resources/fonts/font.png"),
				"abcdefghijklmnopqrstuvwxyz0123456789!.,:;()[]-+*÷/",
				new int[] {4, 4, 4, 4, 3, 3, 4, 4, 3, 4, 4, 3, 5, 5, 4, 4, 5, 4, 4, 3, 4, 5, 5, 4, 5, 6,
						4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3},
				3, 1, false, Colour.ORANGE, Colour.YELLOW);
		
		Font.NAME = new Font(new Texture("resources/fonts/name-font.png"),
				 "abcdefghijklmnopqrstuvwxyzàáâãäåçèéêëìíîïñòóôõöøùúûüýÿ",
				 new int[] {9, 7, 7, 9, 8, 6, 7, 10, 4, 7, 8, 8, 13, 11, 11, 7, 12, 8, 8, 9, 11, 11, 14,
						 9, 8, 9, 9, 9, 9, 9, 9, 9, 7, 8, 8, 8, 8, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11,
						 11, 11, 11, 11, 8, 8},
				 3, 1, false, Colour.LIGHT_BLUE, Colour.WHITE);
		
		Font.CLASSES = new Font(new Texture("resources/fonts/classes.png"),
				"wpvadoWtP",
				new int[] {72, 72, 72, 72, 72, 72, 72, 72, 72},
				0, 0, true, null, null);
	}
}
