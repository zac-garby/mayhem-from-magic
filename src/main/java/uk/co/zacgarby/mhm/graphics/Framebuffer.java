package uk.co.zacgarby.mhm.graphics;

import static org.lwjgl.opengl.GL33.*;

public class Framebuffer {
	private int width, height;
	private int handle;
	private Texture tex;
	
	public Framebuffer(int width, int height) {
		this.width = width;
		this.height = height;
		
		handle = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
		
		tex = new Texture(width, height);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex.getHandle(), 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getHandle() {
		return handle;
	}

	public Texture getTex() {
		return tex;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
	}
	
	private void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void start() {
		bind();
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void end() {
		unbind();
	}
}
