package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.engine.Batch;
import uk.co.zacgarby.mhm.engine.Framebuffer;
import uk.co.zacgarby.mhm.engine.Game;
import uk.co.zacgarby.mhm.engine.Texture;
import uk.co.zacgarby.mhm.engine.Window;

public class App implements Game {
	private Batch batch, lightBatch;
	private Texture sidebar;
	private float x = 0;
	private Framebuffer lightmap;
	
	@Override
	public void setup() {
		sidebar = new Texture("resources/textures/sidebar.png");
		
		batch = new Batch(256);
		batch.register(sidebar);
		batch.createAtlas();
		
		lightBatch = new Batch(256);
		lightBatch.createAtlas();
		
		lightmap = new Framebuffer(250, 200);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void update(double dt) {
		x += 0.2 * dt;
	}

	@Override
	public void render() {
		lightmap.bind();
		lightBatch.start();
		lightBatch.flush();
		lightmap.unbind();
		
		batch.start();
		batch.draw(sidebar, 170, 0);
		batch.flush();
	}
	
	public static void main(String[] args) {
		App app = new App();
		Window win = new Window(app);
		win.run();
	}
}