package uk.co.zacgarby.mhm.world;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.Texture;
import uk.co.zacgarby.mhm.graphics.TextureRegion;
import uk.co.zacgarby.mhm.world.tiles.Dirt;
import uk.co.zacgarby.mhm.world.tiles.DirtWall;

public abstract class Tile {
	public static final int SIZE = 10;
	public static Texture TILESET, LIGHTMAP_SET;
	public static TextureRegion SOLID_LM;
	public static TextureRegion[] WALL_LM = new TextureRegion[16];
	
	private int x, y;
	private boolean isPassable;
	
	public abstract void draw(Batch batch, Level level, int x, int y);
	
	public void initialise(Level level) {}
	
	public void drawLightmap(Batch batch, Level level, int x, int y) {
		if (!isPassable) {
			batch.draw(Tile.SOLID_LM, x, y);
		}
	}
	
	
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
		LIGHTMAP_SET = new Texture("resources/textures/lightmap.png");
		
		SOLID_LM = new TextureRegion(0, 0, 10, 10, LIGHTMAP_SET);
		
		for (int i = 0; i < 16; i++) {
			WALL_LM[i] = new TextureRegion(10 + i * 10, 0, 10, 10, LIGHTMAP_SET);
		}
		
		Dirt.load();
		DirtWall.load();
	}
}
