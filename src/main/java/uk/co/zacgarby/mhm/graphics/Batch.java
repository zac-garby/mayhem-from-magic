package uk.co.zacgarby.mhm.graphics;

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

import org.lwjgl.system.MemoryUtil;

public class Batch {
	private FloatBuffer vertices;
	private IntBuffer triangles;
	private Shader shader;
	private Texture lastTexture;
	private int vbo, ebo, amount, maxSprites;
	
	public Batch(int maxSprites) {
		this.maxSprites = maxSprites;
		
		vertices = MemoryUtil.memAllocFloat(maxSprites * 4 * 7);
		triangles = MemoryUtil.memAllocInt(maxSprites * 2 * 3);
		amount = 0;
		
		vbo = glGenBuffers();		
		ebo = glGenBuffers();
		
		shader = new Shader("resources/shaders/passthrough.frag", "resources/shaders/passthrough.vert");
	}
	
	public void start() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		
		glEnableVertexAttribArray(Shader.POSITION_LOC);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 7 * 4, 0);
		
		glEnableVertexAttribArray(Shader.COLOUR_LOC);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 7 * 4, 2 * 4);
		
		glEnableVertexAttribArray(Shader.TEXCOORD_LOC);
		glVertexAttribPointer(3, 2, GL_FLOAT, false, 7 * 4, 5 * 4);
	}
	
	public void draw(Texture t, float x, float y, float w, float h) {
		if (t != lastTexture) {
			flush();
			lastTexture = t;
		}
		
		if (amount + 1 > maxSprites) {
			flush();
		}
		
		int count = amount * 4;
				
		//       X, Y                   R, G, B                Tx, Ty
		vertices.put(x).put(y)         .put(1).put(1).put(1) .put(0).put(0);
		vertices.put(x + w).put(y)     .put(1).put(1).put(1) .put(1).put(0);
		vertices.put(x + w).put(y + h) .put(1).put(1).put(1) .put(1).put(1);
		vertices.put(x).put(y + h)     .put(1).put(1).put(1) .put(0).put(1);
		
		triangles.put(count).put(count + 1).put(count + 2);
		triangles.put(count + 2).put(count + 3).put(count);
	
		amount++;
	}
	
	public void draw(Texture t, float x, float y) {
		draw(t, x, y, t.getWidth(), t.getHeight());
	}
	
	public void draw(TextureRegion reg, float x, float y, float w, float h) {
		if (reg.getTexture() != lastTexture) {
			flush();
			lastTexture = reg.getTexture();
		}
		
		if (amount + 1 > maxSprites) {
			flush();
		}
		
		int count = amount * 4;
				
		//       X, Y                   R, G, B               Tx, Ty
		vertices.put(x).put(y)         .put(1).put(1).put(1) .put(reg.getTx1()).put(reg.getTy1());
		vertices.put(x + w).put(y)     .put(1).put(1).put(1) .put(reg.getTx2()).put(reg.getTy1());
		vertices.put(x + w).put(y + h) .put(1).put(1).put(1) .put(reg.getTx2()).put(reg.getTy2());
		vertices.put(x).put(y + h)     .put(1).put(1).put(1) .put(reg.getTx1()).put(reg.getTy2());
		
		triangles.put(count).put(count + 1).put(count + 2);
		triangles.put(count + 2).put(count + 3).put(count);
	
		amount++;
	}
	
	public void draw(TextureRegion reg, float x, float y) {
		draw(reg, x, y, reg.getWidth(), reg.getHeight());
	}
	
	public void flush() {
		if (amount == 0) {
			return;
		}
				
		vertices.flip();
		triangles.flip();
		
		shader.use();
		glUniform1i(Shader.ATLAS_LOC, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangles, GL_STATIC_DRAW);
		
		lastTexture.bind();
		glDrawElements(GL_TRIANGLES, 6 * amount, GL_UNSIGNED_INT, 0);
		
		vertices.limit(vertices.capacity());
		triangles.limit(triangles.capacity());
		
		amount = 0;
	}
	
	public void end() {
		flush();
	}
	
	public void uniform1i(int loc, int val) {
		shader.use();
		glUniform1i(loc, val);
	}
	
	public void uniform4f(int loc, float r, float g, float b, float a) {
		shader.use();
		glUniform4f(loc, r, g, b, a);
	}
	
	public void useShader(Shader s) {
		shader = s;
	}
}
