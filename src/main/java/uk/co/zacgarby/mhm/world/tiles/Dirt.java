package uk.co.zacgarby.mhm.world.tiles;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.TextureRegion;
import uk.co.zacgarby.mhm.world.Level;
import uk.co.zacgarby.mhm.world.Tile;

public class Dirt extends Tile {
	private static TextureRegion[] regions = new TextureRegion[6];
	
	// Random texture index, weighted towards the end (plain/few stones)
	private int texIndex = (int) (Math.pow(Math.random(), 0.6) * regions.length);
	
	public Dirt(int x, int y) {
		setX(x);
		setY(y);
		setPassable(true);
	}
	
	@Override
	public void draw(Batch batch, Level level, int x, int y) {
		batch.draw(regions[texIndex], x, y);
	}
	
	public static void load() {
		for (int i = 0; i < 6; i++) {
			regions[i] = new TextureRegion(i * 10, 0, 10, 10, TILESET);
		}
	}
}
