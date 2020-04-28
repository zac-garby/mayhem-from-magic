package uk.co.zacgarby.mhm.engine;

class TextureRegion {
	private float x, y, w, h;
	private Texture tex;
	
	public TextureRegion(float x, float y, float w, float h, Texture texture) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tex = texture;
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

	public Texture getTexture() {
		return tex;
	}

	@Override
	public String toString() {
		return "TextureRegion [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
}
