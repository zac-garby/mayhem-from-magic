package uk.co.zacgarby.mhm.engine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.system.MemoryUtil;

public class Batch {
	private FloatBuffer vertices;
	private IntBuffer triangles;
	private TextureAtlas atlas;
	private HashMap<Texture, TextureRegion> regions;
	private int vbo, ebo, amount;
	
	public Batch(int maxSprites) {
		vertices = MemoryUtil.memAllocFloat(maxSprites * 4 * 7);
		triangles = MemoryUtil.memAllocInt(maxSprites * 2 * 3);
		amount = 0;
		
		vbo = glGenBuffers();		
		ebo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		
		glEnableVertexAttribArray(Window.POSITION);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 7 * 4, 0);
		
		glEnableVertexAttribArray(Window.COLOUR);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 7 * 4, 2 * 4);
		
		glEnableVertexAttribArray(Window.TEXCOORD);
		glVertexAttribPointer(3, 2, GL_FLOAT, false, 7 * 4, 5 * 4);
		
		atlas = new TextureAtlas();
		regions = new HashMap<>();
	}
	
	public void draw(Texture t, float x, float y, float w, float h) {
		if (!regions.containsKey(t)) {
			throw new RuntimeException("Attempted to draw an unregistered texture, or texture atlas hasn't been created.");
		}
		
		TextureRegion reg = regions.get(t);
		
		int count = amount * 4;
		
		//       X, Y                 R, G, B                Tx, Ty
		vertices.put(x).put(y)        .put(1).put(1).put(1)  .put(reg.getX()).put(reg.getY());
		vertices.put(x + w).put(y)    .put(1).put(1).put(1)  .put(reg.getX() + reg.getW()).put(reg.getY());
		vertices.put(x + w).put(y + h).put(1).put(1).put(1)  .put(reg.getX() + reg.getW()).put(reg.getY() + reg.getH());
		vertices.put(x).put(y + h)    .put(1).put(1).put(1)  .put(reg.getX()).put(reg.getY() + reg.getH());
		
		triangles.put(count).put(count + 1).put(count + 2);
		triangles.put(count + 2).put(count + 3).put(count);
	
		amount++;
	}
	
	public void flush() {
		if (amount == 0) {
			return;
		}
		
		vertices.flip();
		triangles.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangles, GL_STATIC_DRAW);
		
		atlas.bind();
		glDrawElements(GL_TRIANGLES, 6 * amount, GL_UNSIGNED_INT, 0);
		
		amount = 0;
	}
	
	public void register(Texture t) {
		atlas.register(t);
	}
	
	public void createAtlas() {
		atlas.create();
		
		float widPerPixel = 1 / (float) atlas.width;
		float x = 0;
		for (Texture t : atlas.textures) {
			float width = widPerPixel * t.getWidth();
			TextureRegion reg = new TextureRegion(x, 0, width, (float) t.getHeight() / (float) atlas.height, atlas);
			regions.put(t, reg);
			x += width;
		}
	}
}
