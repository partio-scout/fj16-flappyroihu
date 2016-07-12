package fi.partio.flappyroihu;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BackgroundTile {
    private float xLoc;

    private Image tileImage;
    private float parlaxFactor = FlappyRoihu.CONFIG.getFloat("background.parlaxFactor");
    private static int tileHeight = FlappyRoihu.CONFIG.getInt("background.tileHeight");
    private static int tileWidth = FlappyRoihu.CONFIG.getInt("background.tileWidth");
    private static int screenWidth = FlappyRoihu.CONFIG.getInt("screen.width");
    private String bgTileset = FlappyRoihu.CONFIG.getString("background.tileset");

    public BackgroundTile(int tileId, int xLoc) throws SlickException {
	this.tileImage = new Image("assets/" + bgTileset + "/tile_" + (tileId < 10 ? "0" : "") + tileId + ".png").getScaledCopy(tileWidth, tileHeight);
	this.xLoc = xLoc;
    }

    public void update(int delta) {
	xLoc -= FlappyRoihu.TARGET_SPEED * parlaxFactor * delta;
    }

    public boolean isVisible() {
	if (xLoc < -tileWidth) {
	    return false; // Already past
	}

	if (xLoc > screenWidth)
	    return false; // Not yet visible
	return true;

    }

    public void draw() {

	if (!isVisible())
	    return; // draw only visible tiles

	// Coordinates for image are the center of the image, not top left corner
	// so some calculation is needed...
	tileImage.draw((int) Math.floor(xLoc), 0);
    }

}
