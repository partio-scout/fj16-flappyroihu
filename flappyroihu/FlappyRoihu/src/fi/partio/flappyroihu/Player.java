package fi.partio.flappyroihu;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
    private int number;
    private int key;
    private boolean dead; // is the player dead
    private float size;
    private ParticleSystem playerPs;
    private int beenDeadCount; // how many frames the player has been dead
    private static boolean showName = FlappyRoihu.CONFIG.getBoolean("player.displayName");
    private static Font nameFont = new TrueTypeFont(
	    new java.awt.Font(
		    FlappyRoihu.CONFIG.getString("player.displayNameFont"),
		    java.awt.Font.BOLD,
		    FlappyRoihu.CONFIG.getInt("player.displayNameFontSize")),
	    true);

    private static int nameBoxHeight = FlappyRoihu.CONFIG.getInt("player.displayNameBoxHeight");
    private static int nameBoxWidth = FlappyRoihu.CONFIG.getInt("player.displayNameBoxWidth");

    private Color nameFontColor;
    private String playerName;
    private SparkEmitter emitter;

    private Image arrowImage;
    private Image gateImage;

    private FlappyRoihu master;

    List<Object> shapeOrder = FlappyRoihu.CONFIG.getList("target.shapeOrder");

    /**
     * Creates and initializes new player objet
     * @param col Player color
     * @param xLoc Player xLocation (so that players are not overlapping)
     * @param height Starting height (yLoc of the player)
     * @param key Key that is binded to this player
     */
    public Player(String subCamp, int number, float xLoc, float height, int key, FlappyRoihu master) {
	this.playerName = subCamp;
	this.xLoc = xLoc;
	this.height = height;
	this.descending = true;
	this.color = getColorForSubcamp(subCamp, false);
	this.nameFontColor = getColorForSubcamp(subCamp, true);
	this.key = key;
	this.number = number;
	this.master = master;
	beenDeadCount = 0;
	size = 75;
	dead = false;
	initAnimation();
	shape = new Circle(xLoc, FlappyRoihu.HEIGHT, size / 2);

	try {
	    if (nameFontColor.getRed() > 50)
		this.arrowImage = new Image("assets/arrow_light.png");
	    else
		this.arrowImage = new Image("assets/arrow_dark.png");

	    if (number == 2)
		arrowImage.rotate(180);

	    this.gateImage = (new Image("assets/" + shapeOrder.get(getNumber() - 1) + ".png")).getScaledCopy(0.6f);

	} catch (SlickException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private Color getColorForSubcamp(String subCamp, boolean forFont) {
	List<Object> playerNames = FlappyRoihu.CONFIG.getList("player.names");
	List<Object> playerColors;
	if (!forFont)
	    playerColors = FlappyRoihu.CONFIG.getList("player.colors");
	else
	    playerColors = FlappyRoihu.CONFIG.getList("player.nameColors");

	if (playerNames.size() != playerColors.size()) {
	    System.err.println("FATAL! Player colors and player names are of different length. Names: " + playerNames.size() + " Colors:" + playerColors.size());
	    System.exit(1);
	}

	for (int i = 0; i < playerNames.size(); i++) {
	    if (playerNames.get(i) != null && (playerNames.get(i) + "").toLowerCase().equals(subCamp.toLowerCase()))
		return Color.decode("" + playerColors.get(i));
	}

	System.err.println("FATAL! No color found for player" + subCamp + ".");
	System.exit(2);

	return null;

    }

    public String getName() {
	return this.playerName;
    }

    public void draw(Graphics g) {

	// if player has been dead for over 200 frames, he is history!
	if (beenDeadCount > 1000)
	    return;

	if (size > 0)
	    playerPs.render();

	if (size > 0 && DEBUG) {
	    g.setColor(Color.white);
	    g.draw(shape);
	}

	if (showName)
	    drawName(g);

    }

    private void drawName(Graphics g) {
	g.setColor(color);
	int boxX = (int) xLoc - (int) size / 2;
	int boxY = (number == 2 ? FlappyRoihu.HEIGHT - nameBoxHeight : 0);
	g.fillRect(boxX, boxY, nameBoxWidth, nameBoxHeight);

	g.setFont(nameFont);
	g.setColor(nameFontColor);
	g.drawString(playerName, boxX + 60, boxY + 6);
	g.drawImage(arrowImage, boxX + 20, boxY + 4);

	g.drawImage(gateImage, boxX + nameBoxWidth - 65, boxY + 5);

    }

    /**
     * Update player position on field
     * @param delta relative time between previous update
     */
    public void update(int delta) {

	// if player has been dead for over 200 frames, he is history!
	if (beenDeadCount > 1000)
	    return;

	if (dead)
	    beenDeadCount += delta;

	if (descending || dead)
	    height -= FlappyRoihu.PLAYER_SPEED * delta;
	else
	    height += FlappyRoihu.PLAYER_SPEED * delta;

	if (height < 100)
	    height = 100;
	if (height > FlappyRoihu.HEIGHT - 100)
	    height = FlappyRoihu.HEIGHT - 100;

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
     * Sets player dead. Plays relevant animation and then stops updating player after a while
     */
    public void die() {
	dead = true;
	explode();
	master.playerDies(this);
    }

    public void explode() {

	try {
	    playerPs = new ParticleSystem(new Image("assets/spark.png", true), 300);
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

    public int getNumber() {
	return number;
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
