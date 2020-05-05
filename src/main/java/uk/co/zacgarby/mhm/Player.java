package uk.co.zacgarby.mhm;

public class Player extends Entity {
	private int money;
	private int mana, maxMana;
	private Stats stats;
	private Title title;
	
	public Player(String name) {
		setName(name);
		setHealth(0);
		setMana(0);
		setX(0);
		setY(0);
		setXP(0);
		setMoney(0);
		setStats(new Stats());
		setTitle(Title.PYROMANCER);
	}
	
	public int getSilver() {
		return money % 100;
	}
	
	public int getGold() {
		return (money - getPlatinum() * 10000) / 100;
	}
	
	public int getPlatinum() {
		return money / 10000;
	}
	
	public void addMoney(int amount) {
		money += amount;
	}
	
	public void setMoney(int amount) {
		money = amount;
	}
	
	public int getMoney() {
		return money;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
}
