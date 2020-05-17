package uk.co.zacgarby.mhm.world.tiles;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.TextureRegion;
import uk.co.zacgarby.mhm.world.Level;
import uk.co.zacgarby.mhm.world.Tile;

public class DirtWall extends Tile {
	private static TextureRegion[] regions = new TextureRegion[16];
	private Integer texIndex = null;
	
	public DirtWall(int x, int y) {
		setX(x);
		setY(y);
		setPassable(false);
	}

	@Override
	public void draw(Batch batch, Level level, int x, int y) {
		if (texIndex == null) {
			texIndex = level.neighbourhood(getX(), getY(), DirtWall.class);
		}
		
		batch.draw(regions[texIndex], x, y);
	}
	
	@Override
	public void drawLightmap(Batch batch, Level level, int x, int y) {
		if (texIndex == null) {
			texIndex = level.neighbourhood(getX(), getY(), DirtWall.class);
		}
		
		batch.draw(Tile.WALL_LM[texIndex], x, y);
	}
	
	public static void load() {
		for (int i = 0; i < 16; i++) {
			regions[i] = new TextureRegion((i + 6) * 10, 0, 10, 10, TILESET);
		}
	}
}
