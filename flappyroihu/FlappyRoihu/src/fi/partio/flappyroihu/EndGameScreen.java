package fi.partio.flappyroihu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

public class EndGameScreen {

    Player winner;

    private String splashScreenFontnName = FlappyRoihu.CONFIG.getString("splashscreen.font");
    float fadeToBlackSpeed = 0.0003f;
    float alpha = 0;
    boolean showWinner;
    Rectangle fadeRect;

    public EndGameScreen(Player winner) {
	this.winner = winner;
    }

    public void update(int delta) {

	if (alpha <= 1)
	    alpha += ((delta)) * fadeToBlackSpeed;

	else {
	    showWinner = true;

	}

    }

    public void draw(Graphics g) {
	// Black box that covers all

	g.setColor(new Color(0, 0, 0, alpha));

	g.fillRect(0, 0, FlappyRoihu.WIDTH + 2, FlappyRoihu.HEIGHT + 2);

	if (showWinner) {
	    g.setFont(new TrueTypeFont(
		    new java.awt.Font(
			    splashScreenFontnName,
			    java.awt.Font.BOLD,
			    50),
		    true));
	    g.setColor(Color.white);
	    g.drawString("Winner: " + winner.getName(), 200, 300);
	}

    }

    public boolean isShowWinnerState() {
	return showWinner;
    }

}
