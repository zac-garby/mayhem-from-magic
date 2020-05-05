package uk.co.zacgarby.mhm.world;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.world.tiles.Dirt;
import uk.co.zacgarby.mhm.world.tiles.DirtWall;

public class Level {
	public static final int VIEWPORT_W = 16;
	public static final int VIEWPORT_H = 16;
	
	private Tile[][] tiles;
	private int width, height;
	
	public Level(int w, int h) {
		width = w;
		height = h;
		tiles = new Tile[h][w];
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (Math.random() < 0.75) {
					tiles[j][i] = new Dirt(i, j);
				} else {
					tiles[j][i] = new DirtWall(i, j);
				}
			}
		}
	}
	
	public Tile get(int x, int y) {
		return tiles[y][x];
	}
	
	public int neighbourhood(int x, int y, Class<?> c) {
		int n = 0;
				
		if (y+1 < height && get(x, y+1).getClass() == c) n |= 0b0001;
		if (x+1 < width && get(x+1, y).getClass() == c) n |= 0b0010;
		if (y-1 >= 0 && get(x, y-1).getClass() == c) n |= 0b0100;
		if (x-1 >= 0 && get(x-1, y).getClass() == c) n |= 0b1000;
		
		return n;
	}
	
	public void draw(Batch batch, float x, float y, float cx, float cy) {
		int sx = (int) (cx / 10);
		int sy = (int) (cy / 10);
		
		for (int i = sx; i < sx + VIEWPORT_W + 2; i++) {
			for (int j = sy; j < sy + VIEWPORT_H + 2; j++) {
				if (i < 0 || j < 0 || i >= width || j >= height) continue;
				
				get(i, j).draw(batch, this,
						(int) (x + i * Tile.SIZE - cx),
						(int) (y + j * Tile.SIZE - cy));
			}
		}
	}
}
