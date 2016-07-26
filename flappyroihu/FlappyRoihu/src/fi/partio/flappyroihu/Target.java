package fi.partio.flappyroihu;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;

/**
 * Object responsible of drawing and keeping state of gates i.e. targets.
 * This object also checks if the related player manages to go through this gate.
 * @author Arttu
 *
 */
public class Target {

    private Player player;
    private float height;
    private float size;
    private float xLoc;
    private Shape shape;
    private boolean passed;
    private boolean checkDone;
    public float darkerFactor = 0.4f;
    private Image targetImage;

    private static int X_DELTA = 55;
    private static int Y_DELTA = 100;
    private float alphaDecay = 0.02f;

    List<Object> shapeOrder = FlappyRoihu.CONFIG.getList("target.shapeOrder");

    /**
     * @param player
     * @param height
     * @param xLoc
     */
    public Target(Player player, float height, float xLoc) {
	super();
	this.player = player;
	this.height = height;
	size = FlappyRoihu.HEIGHT / 5;
	checkDone = false;

	try {
	    this.targetImage = new Image("/assets/gates/" + player.getName().toLowerCase() + "_" + shapeOrder.get(player.getNumber() - 1) + ".png");
	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	this.xLoc = xLoc;
	this.passed = false;

	shape = new Ellipse(xLoc + X_DELTA, FlappyRoihu.HEIGHT - height + Y_DELTA, 116 / 2, 200 / 2);
	//shape = new Rectangle(xLoc, FlappyRoihu.HEIGHT - height, FlappyRoihu.WIDTH / 30, getSize());
    }

    /**
     * Draws target
     * @param g Graphics where target is drawn
     */
    public void draw(Graphics g) {

	// If corresponding player is already dead, don't bother to draw
	if (player.isDead())
	    return;

	shape.setCenterX(xLoc + X_DELTA);
	g.setColor(player.getColor().darker(darkerFactor));
	//g.draw(shape);

	targetImage.draw(xLoc, FlappyRoihu.HEIGHT - height);

    }

    /**
     * Updates target position 
     * @param delta Relative time ellapsed between last update
     */
    public void update(int delta) {

	/*
	// If corresponding player is already dead, don't bother to update
	if (player.isDead())
	    return;
	*/

	xLoc -= FlappyRoihu.TARGET_SPEED * delta;

	if (xLoc < player.getXLoc() && !checkDone) {
	    checkDone = true;
	    if (!player.getShape().intersects(shape))
		player.die();
	    else if (!player.isDead())
		passGate();
	}

	if (passed) {
	    targetImage.setAlpha(targetImage.getAlpha() - alphaDecay);
	}

    }

    public void passGate() {
	darkerFactor = -0.4f;
	passed = true;
    }

    /**
     * Checks if this target is already outside of the screen
     * @return True, if target is outside of the screen
     */
    public boolean isOutside() {
	return (xLoc < -FlappyRoihu.WIDTH / 20);
    }

    /**
     * Returns the target size
     * @return
     */
    public float getSize() {
	return size;
    }

    /**
     * Returns the target xLoc
     * @return
     */
    public float getXloc() {
	return xLoc;
    }

    /**
     * Returns the player which is linked to this target
     * @return
     */
    public Player getPlayer() {
	return player;
    }

}
