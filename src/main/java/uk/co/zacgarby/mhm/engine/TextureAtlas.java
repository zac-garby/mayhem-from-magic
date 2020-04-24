package uk.co.zacgarby.mhm.engine;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class TextureAtlas {
	private Texture atlas;
	List<Texture> textures;
	
	int width;
	int height;
	
	public TextureAtlas() {
		textures = new ArrayList<>();
	}
	
	// Registers a texture in the atlas and returns its index.
	public int register(Texture t) {
		textures.add(t);
		
		width += t.getWidth();
		height = Math.max(height, t.getHeight());
		
		return textures.size() - 1;
	}
	
	public void create() {
		try (MemoryStack stack = stackPush()) {
			ByteBuffer ab = stack.malloc(width * height * 4);
			
			int startX = 0;
			for (int i = 0; i < textures.size(); i++) {
				Texture t = textures.get(i);
				t.bind();
				
				ByteBuffer tex = MemoryUtil.memAlloc(t.getWidth() * t.getHeight() * 4);
				glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, tex);
				
				for (int x = 0; x < t.getWidth(); x++) {
					for (int y = 0; y < t.getHeight(); y++) {
						int from = (y * t.getWidth() * 4) + (x * 4);
						int to = (y * width * 4) + ((x + startX) * 4);
						
						for (int c = 0; c < 4; c++) {
							ab.put(to + c, tex.get(from + c));
						}
					}
				}
				
				MemoryUtil.memFree(tex);
				
				startX += t.getWidth();
			}
			
			ab.flip();
			atlas = new Texture(width, height, ab);
		}
	}
	
	public void bind() {
		atlas.bind();
	}
}
