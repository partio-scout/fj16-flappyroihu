package fi.partio.flappyroihu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Object responsible of drawing and keeping the state of one "spark" i.e. player.
 * @author Arttu
 *
 */
public class Player {
    private static final boolean DEBUG = false;
    private Color color;
    private float xLoc;
    private float height;
    private boolean descending;
    private Shape shape;
    private int key;
    private boolean dead;
    private float size;
    private ParticleSystem playerPs;

    private SparkEmitter emitter;

    /**
     * Creates and initializes new player objet
     * @param col Player color
     * @param xLoc Player xLocation (so that players are not overlapping)
     * @param height Starting height (yLoc of the player)
     * @param key Key that is binded to this player
     */
    public Player(Color col, float xLoc, float height, int key) {
	this.xLoc = xLoc;
	this.height = height;
	this.descending = false;
	this.color = col;
	this.key = key;
	size = 75;
	dead = false;
	initAnimation();
	shape = new Circle(xLoc, FlappyRoihu.HEIGHT, size / 2);
    }

    public void draw(Graphics g) {
	if (size > 0)
	    playerPs.render();

	if (size > 0 && DEBUG) {
	    g.setColor(Color.white);
	    g.draw(shape);
	}
    }

    /**
     * Update player position on field
     * @param delta relative time between previous update
     */
    public void update(int delta) {
	if (descending || dead)
	    height -= FlappyRoihu.PLAYER_SPEED * delta;
	else
	    height += FlappyRoihu.PLAYER_SPEED * delta;

	if (height < -size)
	    height = -size;
	if (height > FlappyRoihu.HEIGHT)
	    height = FlappyRoihu.HEIGHT;

	playerPs.update(delta);
	playerPs.setPosition(xLoc, FlappyRoihu.HEIGHT - height);
	emitter.setSize((int) size);
	((Circle) shape).setRadius(size / 2);
	shape.setCenterY(FlappyRoihu.HEIGHT - height);

	if (dead) {
	    size -= FlappyRoihu.PLAYER_SPEED * 0.001 * delta;

	}
    }

    /**
     * Sets if the player is descending (true) or ascending
     * @param desc
     */
    public void setDescending(boolean desc) {
	this.descending = desc;
    }

    /**
     * Processed pressed key. If this object detects that the key binded to it
     * is pressed, it sets descending to false (i.e. player is ascending)
     * @param pressedKey
     */
    public void keyPressed(int pressedKey) {
	if (key == pressedKey)
	    descending = false;
    }

    /**
     * Processed released key. If this object detects that the key binded to it
     * is released, player resumes descending
     * @param pressedKey
     */
    public void keyReleased(int pressedKey) {
	if (key == pressedKey)
	    descending = true;
    }

    /**
     * Get the pleyer color
     * @return color
     */
    public Color getColor() {
	return color;
    }

    /**
     * Sets player dead. Plays relevant animation and then stops updating player
     */
    public void die() {
	dead = true;
	explode();
    }

    public void explode() {

	try {
	    playerPs = new ParticleSystem(new Image("assets/spark.png", true));
	    ExplodeEmiter exEmitter = new ExplodeEmiter(0, 0, size, color);
	    playerPs.removeAllEmitters();
	    playerPs.addEmitter(exEmitter);
	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Returs xLocation
     * @return xLoc
     */
    public float getXLoc() {
	return xLoc;
    }

    /**
     * Returns the shape, which is used for collision detection
     * @return Shape drawn around the player
     */
    public Shape getShape() {
	return shape;
    }

    /**
     * Checks if player is dead
     * @return true if dead
     */
    public boolean isDead() {
	return dead;
    }

    /**
     * Initilizes particle animation for this player
     */
    private void initAnimation() {

	try {
	    playerPs = new ParticleSystem(new Image("assets/spark.png", true));
	    emitter = new SparkEmitter(0, 0, size, color);
	    playerPs.addEmitter(emitter);
	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
