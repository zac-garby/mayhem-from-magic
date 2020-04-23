package uk.co.zacgarby.mhm.engine;

class TextureRegion {
	private float x, y, w, h;
	private TextureAtlas atlas;
	
	public TextureRegion(float x, float y, float w, float h, TextureAtlas atlas) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.atlas = atlas;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public String toString() {
		return "TextureRegion [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
}
