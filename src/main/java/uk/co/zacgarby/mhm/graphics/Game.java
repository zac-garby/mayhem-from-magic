package uk.co.zacgarby.mhm.graphics;

public abstract class Game {
	public abstract void setup();
	public abstract void dispose();
	
	public abstract void update(double dt);
	public abstract void render();
	
	public void cursorMoved(int x, int y) {}
	public void keyDown(int key, int scancode, int mods) {}
	public void keyUp(int key, int scancode, int mods) {}
	public void mouseDown(int button, int mods) {}
	public void mouseUp(int button, int mods) {}
}
