package uk.co.zacgarby.mhm.graphics;

import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.opengl.GL33;

public class Framebuffer {
	private int width, height;
	private int handle;
	private Texture tex;
	
	private Integer prevW = null, prevH = null;
	
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
	
	public void start() {
		if (prevW == null || prevH == null) {
			int[] vp = new int[4];
			GL33.glGetIntegerv(GL33.GL_VIEWPORT, vp);
			prevW = vp[2];
			prevH = vp[3];
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
		glViewport(0, 0, width, height);
	}
	
	public void end() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, prevW, prevH);
	}
}
