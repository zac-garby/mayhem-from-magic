package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.Font;
import uk.co.zacgarby.mhm.graphics.Framebuffer;
import uk.co.zacgarby.mhm.graphics.Game;
import uk.co.zacgarby.mhm.graphics.Shader;
import uk.co.zacgarby.mhm.graphics.Texture;
import uk.co.zacgarby.mhm.graphics.Window;
import uk.co.zacgarby.mhm.world.Level;
import uk.co.zacgarby.mhm.world.Tile;
import uk.co.zacgarby.mhm.world.tiles.Dirt;

public class App extends Game {	
	private Batch batch, lightBatch;
	private Texture cursor;
	private Framebuffer lightmap;
	private int cx, cy;
	private float camX, camY;
	private UI ui;
	private Player player;
	private Level level;
	
	@Override
	public void setup() {		
		cursor = new Texture("resources/textures/cursor.png");
		
		Font.load();
		Tile.load();
		
		batch = new Batch(256);
		batch.useShader(new Shader("resources/shaders/shader.frag", "resources/shaders/shader.vert"));
		
		lightBatch = new Batch(256);
		lightmap = new Framebuffer(160, 160);
		
		cx = 0;
		cy = 0;
		
		player = new Player("zac");
		ui = new UI(player);
		level = new Level(128, 128);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void update(double dt) {
		camX += 20 * dt;
		camY = (float) (50 + 50 * Math.sin(camX / 32));
	}

	@Override
	public void render() {
		lightmap.start();
		lightBatch.start();
		lightBatch.flush();
		lightmap.end();
		
		lightmap.getTex().bind(1);
		batch.uniform1i(Shader.LIGHTMAP_LOC, 1);
		
		batch.start();
		level.draw(batch, 9, 28, camX, camY);
		ui.draw(batch);
		batch.draw(cursor, cx - 1, cy - cursor.getHeight() + 2);
		batch.end();
	}
	
	@Override
	public void cursorMoved(int x, int y) {
		cx = x;
		cy = y;
	}
	
	public static void main(String[] args) {
		App app = new App();
		Window win = new Window(app);
		win.run();
	}
}