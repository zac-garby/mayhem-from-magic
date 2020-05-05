package uk.co.zacgarby.mhm.graphics;

public class TextureRegion {
	private float x, y, w, h, tx1, ty1, tx2, ty2;
	private Texture tex;
	
	public TextureRegion(float x, float y, float w, float h, Texture texture) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tex = texture;
		
		float ppx = 1.0f / (float) texture.getWidth();
		float ppy = 1.0f / (float) texture.getHeight();
		
		tx1 = x * ppx;
		tx2 = tx1 + w * ppx;
		ty1 = y * ppy;
		ty2 = ty1 + h * ppy;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return w;
	}

	public float getHeight() {
		return h;
	}

	public Texture getTexture() {
		return tex;
	}

	public float getTx1() {
		return tx1;
	}

	public float getTy1() {
		return ty1;
	}

	public float getTx2() {
		return tx2;
	}

	public float getTy2() {
		return ty2;
	}

	@Override
	public String toString() {
		return "TextureRegion [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
}
