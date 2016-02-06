package fi.partio.flappyroihu;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Class responsible of drawing and moving the background image
 * @author Arttu
 * 
 */

// @TODO What happens when image ends? Should it repeat?  

public class Background {

    private Image bgImage;

    private float xLoc;

    private float bgWidth;

    public Background(String imgName) {
	Image orig;
	try {
	    orig = new Image(imgName);
	    bgImage = orig.getScaledCopy(FlappyRoihu.HEIGHT / FlappyRoihu.BACKGROUND_HEIGHT);
	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void draw() {
	bgImage.draw(xLoc, 0);
	//if (xLoc > bgWidth - FlappyRoihu.WIDTH)
	//bgImage.draw(xLoc - bgWidth, 0);
    }

    public void update(int delta) {
	xLoc -= FlappyRoihu.TARGET_SPEED * FlappyRoihu.BACKGROUND_PARLAX_FACTOR * delta;
    }
}
