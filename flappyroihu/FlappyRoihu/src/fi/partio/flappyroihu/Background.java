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

    private int tileCount = FlappyRoihu.CONFIG.getInt("background.tileCount");;
    private int tileCycleCount = 5; // repeat all tiles 50 times. Should be enough for everyone
    private int tileBufferLength = tileCycleCount * tileCount;

    private BackgroundTile[] tileBuffer;

    private int tileNo;

    private float parlaxFactor = FlappyRoihu.CONFIG.getFloat("background.parlaxFactor");

    private int tileHeight = FlappyRoihu.CONFIG.getInt("background.tileHeight");
    private int tileWidth = FlappyRoihu.CONFIG.getInt("background.tileWidth");

    public Background(String imgName) {

	tileBuffer = new BackgroundTile[tileBufferLength];

	try {
	    int tileInd = 0;
	    for (int cycle = 0; cycle < tileCycleCount; cycle++) {
		// @TODO same tile can repeat two times, if prevoius cycle ends with same tile as the next cycle starts
		int[] tileOrd = FlappyRoihu.getShuffledArray(tileCount);
		for (int tileId : tileOrd) {
		    tileBuffer[tileInd] = new BackgroundTile(tileId + 1, tileInd * tileWidth);
		    tileInd++;
		}
	    }
	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void draw() {
	for (BackgroundTile tile : tileBuffer)
	    tile.draw();
    }

    public void update(int delta) {
	for (BackgroundTile tile : tileBuffer)
	    tile.update(delta);
    }
}
