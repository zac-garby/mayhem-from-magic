package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.engine.Batch;
import uk.co.zacgarby.mhm.engine.Game;
import uk.co.zacgarby.mhm.engine.Texture;
import uk.co.zacgarby.mhm.engine.Window;

public class App implements Game {
	private Batch batch;
	private Texture sun, moon;
	private float x = 0;
	
	@Override
	public void setup() {
		sun = new Texture("resources/textures/sun.png");
		moon = new Texture("resources/textures/moon.png");
		
		batch = new Batch(512);
		batch.register(sun);
		batch.register(moon);
		batch.createAtlas();
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
		batch.draw(moon, 125 + (float) (80 * Math.sin(x * 1.643533)), 100 + (float) (50 * Math.sin(x * 1.231235)), 32, 32);
		batch.draw(moon, 125 + (float) (80 * Math.sin(15 + x * -1.243533)), 100 + (float) (50 * Math.sin(5 + x * 1.931235)), 32, 32);
		batch.draw(moon, 125 + (float) (80 * Math.sin(30 + x * 1.743533)), 100 + (float) (50 * Math.sin(10 + x * -2.331235)), 32, 32);
		batch.draw(sun, 125 - 32, 100 - 32, 64, 64);
		batch.flush();
	}
	
	public static void main(String[] args) {
		App app = new App();
		Window win = new Window(app);
		win.run();
	}
}