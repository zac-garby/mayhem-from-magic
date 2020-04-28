package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.engine.Batch;
import uk.co.zacgarby.mhm.engine.Framebuffer;
import uk.co.zacgarby.mhm.engine.Game;
import uk.co.zacgarby.mhm.engine.Shader;
import uk.co.zacgarby.mhm.engine.Texture;
import uk.co.zacgarby.mhm.engine.Window;

public class App implements Game {
	private Batch batch, lightBatch;
	private Texture ui, cursor;
	private Framebuffer lightmap;
	
	@Override
	public void setup() {
		ui = new Texture("resources/textures/ui.png");
		cursor = new Texture("resources/textures/cursor.png");
		
		batch = new Batch(256);
		batch.useShader(new Shader("resources/shaders/shader.frag", "resources/shaders/shader.vert"));
		
		lightBatch = new Batch(256);
		
		lightmap = new Framebuffer(160, 160);
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void update(double dt) {
		
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
		batch.draw(ui, 0, 0);
		batch.draw(cursor, 80, 100);
		batch.end();
	}
	
	public static void main(String[] args) {
		App app = new App();
		Window win = new Window(app);
		win.run();
	}
}