package uk.co.zacgarby.mhm.engine;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Texture {
	private int handle;
	private int width;
	private int height;
	
	public Texture() {
		handle = glGenTextures();
	}
	
	public Texture(int width, int height, ByteBuffer data) {
		this.width = width;
		this.height = height;
		
		handle = glGenTextures();
		
		bind();
		param(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		param(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
	}
	
	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		
		handle = glGenTextures();
		
		bind();
		param(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		param(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
	}
	
	public Texture(String path) {
		handle = glGenTextures();
		
		bind();
		param(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		param(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		param(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		try (MemoryStack stack = stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			stbi_set_flip_vertically_on_load(true);
			ByteBuffer image = stbi_load(path, w, h, comp, 4);
			if (image == null) {
				throw new RuntimeException("Failed to load a texture file!\n" + stbi_failure_reason());
			}
			
			width = w.get();
			height = h.get();
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		}
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void bind(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, handle);
		glActiveTexture(GL_TEXTURE0);
	}
	
	public void param(int p, int v) {
		glTexParameteri(GL_TEXTURE_2D, p, v);
	}

	public int getHandle() {
		return handle;
	}
	
	int getWidth() {
		return width;
	}

	int getHeight() {
		return height;
	}

	public void dispose() {
		glDeleteTextures(handle);
	}
}
