package uk.co.zacgarby.mhm.engine;

public interface Game {
	public void setup();
	public void dispose();
	
	public void update(double dt);
	public void render();
}
