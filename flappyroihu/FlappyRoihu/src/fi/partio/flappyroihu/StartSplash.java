package fi.partio.flappyroihu;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class StartSplash {

    private static int STATE_SPLASH_FADING_OUT = 1;
    private static int STATE_NAMES_FADING_IN = 2;
    private static int STATE_NAMES_ON_SCREEN = 3;
    private static int STATE_NAMES_FADING_OUT = 4;

    private static int STATE_ALL_DONE = 99;

    private int state = STATE_SPLASH_FADING_OUT;

    private long waitStartTime;

    private String splashScreenFontnName = FlappyRoihu.CONFIG.getString("splashscreen.font");

    float alpha = 1;
    float splashAlphaDecay = 0.01f;
    private boolean keyPressed;
    boolean isReady;
    boolean showPressEnter = FlappyRoihu.CONFIG.getBoolean("splashscreen.showPressEnter");

    List<Object> playerNames = FlappyRoihu.CONFIG.getList("game.players");
    Image[] targetImages;

    Image splashScreen;
    Image splashScreenBg;

    public StartSplash() {
	try {
	    splashScreen = new Image("/assets/splash.jpg");
	    splashScreenBg = new Image("/assets/splash_bg.jpg");

	    targetImages = new Image[playerNames.size()];
	    List<Object> shapeOrder = FlappyRoihu.CONFIG.getList("target.shapeOrder");
	    for (int i = 0; i < targetImages.length; i++) {
		targetImages[i] = new Image("/assets/gates/" + playerNames.get(i).toString().toLowerCase() + "_" + shapeOrder.get(i) + ".png");
	    }

	    keyPressed = false;

	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void keyPressed() {
	keyPressed = true;
    }

    public void draw(Graphics g) {
	if (state == STATE_SPLASH_FADING_OUT) {
	    splashScreenBg.draw(0, 0);
	    splashScreen.draw(0, 0);
	    g.setColor(new Color(1, 1, 1, splashScreen.getAlpha()));
	    g.setFont(new TrueTypeFont(
		    new java.awt.Font(
			    splashScreenFontnName,
			    java.awt.Font.BOLD,
			    20),
		    true));

	    g.drawString("Code: Arttu Tanner", 60, 600);
	    g.drawString("Art: Olli Haataja", 60, 630);
	    if (showPressEnter)
		g.drawString("[Press Enter]", 1100, 630);
	}

	if (state == STATE_NAMES_ON_SCREEN) {

	    splashScreenBg.draw(0, 0);
	    g.setFont(new TrueTypeFont(
		    new java.awt.Font(
			    splashScreenFontnName,
			    java.awt.Font.BOLD,
			    50),
		    true));
	    g.setColor(Color.white);
	    g.drawString("Players:", 50, 40);

	    int y = 40;
	    for (int i = 0; i < playerNames.size(); i++) {

		targetImages[i].draw(300, y);
		g.drawString(playerNames.get(i) + "", 500, y + 20);
		y += 220;
	    }

	    if (showPressEnter) {
		g.setColor(new Color(1, 1, 1, splashScreen.getAlpha()));
		g.setFont(new TrueTypeFont(
			new java.awt.Font(
				splashScreenFontnName,
				java.awt.Font.BOLD,
				20),
			true));
		g.drawString("[Press Enter]", 1100, 630);
	    }

	}

    }

    public void update(int delta) {

	if (!keyPressed)
	    return;

	if (state == STATE_SPLASH_FADING_OUT) {
	    splashScreen.setAlpha(splashScreen.getAlpha() - splashAlphaDecay);
	    if (splashScreen.getAlpha() < 0.01) {
		state = STATE_NAMES_ON_SCREEN;
		waitStartTime = System.currentTimeMillis();

	    }

	}

	if (state == STATE_NAMES_ON_SCREEN) {

	    if (System.currentTimeMillis() - waitStartTime > 1000 && keyPressed) {
		STATE_ALL_DONE = 99;
		isReady = true;

	    }
	    keyPressed = false;
	}

    }

    public boolean isReady() {
	return isReady;
    }

}
