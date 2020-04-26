package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.engine.Batch;
import uk.co.zacgarby.mhm.engine.Framebuffer;
import uk.co.zacgarby.mhm.engine.Game;
import uk.co.zacgarby.mhm.engine.Texture;
import uk.co.zacgarby.mhm.engine.Window;

public class App implements Game {
	private Batch batch, lightBatch;
	private Texture sidebar;
	private Framebuffer lightmap;
	
	@Override
	public void setup() {
		sidebar = new Texture("resources/textures/sidebar.png");
		
		batch = new Batch(256);
		batch.register(sidebar);
		batch.createAtlas();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void update(double dt) {
		
	}

	@Override
	public void render() {
//		lightmap.bind();
//		lightBatch.start();
//		lightBatch.draw(sidebar, 0, 0);
//		lightBatch.flush();
//		lightmap.unbind();
		
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