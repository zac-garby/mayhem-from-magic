package uk.co.zacgarby.mhm.world;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.Texture;
import uk.co.zacgarby.mhm.world.tiles.Dirt;
import uk.co.zacgarby.mhm.world.tiles.DirtWall;

public abstract class Tile {
	public static final int SIZE = 10;
	public static Texture TILESET;
	
	private int x, y;
	private boolean isPassable;
	
	public abstract void draw(Batch batch, Level level, int x, int y);
	public void initialise(Level level) {}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isPassable() {
		return isPassable;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}
	
	public static void load() {
		TILESET = new Texture("resources/textures/tiles.png");
		
		Dirt.load();
		DirtWall.load();
	}
}
