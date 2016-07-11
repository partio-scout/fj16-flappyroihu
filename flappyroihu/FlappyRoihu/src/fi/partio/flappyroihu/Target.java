package fi.partio.flappyroihu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
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
    private boolean checkDone;
    public float darkerFactor = 0.4f;

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

	this.xLoc = xLoc;
	shape = new Rectangle(xLoc, FlappyRoihu.HEIGHT - height, FlappyRoihu.WIDTH / 30, getSize());
    }

    /**
     * Draws target
     * @param g Graphics where target is drawn
     */
    public void draw(Graphics g) {

	/*
	// If corresponding player is already dead, don't bother to draw
	if (player.isDead())
	    return;
	*/

	shape.setCenterX(xLoc);
	g.setColor(player.getColor().darker(darkerFactor));
	g.fill(shape);
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
	    else
		darkerFactor = -0.4f;
	}
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
