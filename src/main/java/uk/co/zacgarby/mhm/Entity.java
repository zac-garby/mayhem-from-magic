package uk.co.zacgarby.mhm;

public class Entity {
	private String name;
	private int health;
	private int x, y;
	private int xp;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int hp) {
		health = hp;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean harm(int hp) {
		if (hp > getHealth()) {
			return false;
		}
		
		setHealth(getHealth() - hp);
		return true;
	}
	
	public void setPos(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void move(int dx, int dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	}
	
	public int getXP() {
		return xp;
	}
	
	public void setXP(int to) {
		xp = to;
	}
	
	public void addXP(int xp) {
		this.xp += xp;
	}
	
	public int getLevel() {
		return Math.max((int) Math.floor(5 * Math.log10((double)xp / 10.0 + 1)), 1);
	}
}
