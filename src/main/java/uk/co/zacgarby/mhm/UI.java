package uk.co.zacgarby.mhm;

import uk.co.zacgarby.mhm.graphics.Batch;
import uk.co.zacgarby.mhm.graphics.Font;
import uk.co.zacgarby.mhm.graphics.Texture;

public class UI {
	private Texture background;
	private Player player;
	
	public UI(Player player) {
		this.player = player;
		this.background = new Texture("resources/textures/ui.png");
	}
	
	public void draw(Batch batch) {
		batch.draw(background, 0, 0);
		
		Font.XP.drawRight(batch, String.valueOf(player.getLevel()), 196, 128);
		Font.XP.draw(batch, String.valueOf(player.getLevel() + 1), 239, 128);
		
		Font.NORMAL.draw(batch, String.valueOf(player.getSilver()), 191, 112);
		Font.NORMAL.draw(batch, String.valueOf(player.getGold()), 214, 112);
		Font.NORMAL.draw(batch, String.valueOf(player.getPlatinum()), 237, 112);
		
		Font.NAME.drawCentered(batch, player.getName(), 212, 180);
		
		Font.CLASSES.drawCentered(batch, String.valueOf(player.getTitle().getFontChar()), 212, 160);
	}
}
