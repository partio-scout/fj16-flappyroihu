package fi.partio.flappyroihu;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Main class including the game loop and initialization
 * @author Arttu
 *
 */
public class FlappyRoihu extends BasicGame {

    public static Configuration CONFIG = getConfig();

    /**
     * Screen size
     */
    public static int WIDTH = CONFIG.getInt("screen.width");
    public static int HEIGHT = CONFIG.getInt("screen.height");

    /**
     * Relative speed by which player moves up and down. Descending speed is slower than ascending
     */
    public static float PLAYER_SPEED = 0.1f;

    /**
     * Relative speed by which targets (gates) move towards the player.
     */
    public static float TARGET_SPEED = CONFIG.getFloat("target.speed");

    /**
     * Amount by which both player and target speed increase in each update cycle
     */
    public static float TARGET_SPEED_INCREASE = CONFIG.getFloat("target.speedIncrease");
    public static float PLAYER_SPEED_INCREASE = CONFIG.getFloat("player.speedIncrease");

    private boolean started = false;

    private static Random rnd = ThreadLocalRandom.current();

    private float bgrX;
    private float bgrY;

    //private Image background;

    private Vector<Player> players;
    private Vector<Target> targets;
    private Background background;

    /**
     * Constructs the main object. Calls super to give title to the window
     */
    public FlappyRoihu() {
	super("Flappy Roihu");
    }

    /**
     * Opens the default config file (flappyroihu.properties) in default directory
     * and reads the config variables in the CONFIG -object
     * @return
     */
    private static Configuration getConfig() {

	try {
	    Configuration config = new PropertiesConfiguration("flappyroihu.properties");
	    return config;

	} catch (Exception cex) {
	    System.err.println("Error when opening or reading 'flappyroihu.properties'");
	    cex.printStackTrace();
	    System.exit(0);
	    return null;
	}
    }

    @Override
    /**
     * Sets initial variables
     * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
     */
    public void init(GameContainer container) throws SlickException {

	background = new Background("assets/tausta2.jpg");

	//System.out.println("BG-w:" + background.getWidth());
	players = new Vector<>();
	players.add(new Player(Color.red, 50, 200, Input.KEY_LSHIFT));
	players.add(new Player(Color.green, 150, 100, Input.KEY_SPACE));
	players.add(new Player(Color.yellow, 250, 300, Input.KEY_RSHIFT));
	for (Player p : players)
	    p.update(1);
	targets = new Vector<>();
	//targets.add(new Target(players.get(0), 300, 700));
	addTargets(1200);
    }

    @Override
    /**
     * Renders all targets and players and background
     */
    public void render(GameContainer container, Graphics g) {
	//background.draw(bgrX, bgrY, 2000 * 1.4f, 1325 * 1.4f);
	//background.draw(bgrX + 2000 * 1.3f + WIDTH, bgrY, -2000 * 1.3f, 1325 * 1.3f);

	background.draw();

	for (Target t : targets)
	    t.draw(g);
	for (Player p : players)
	    p.draw(g);
	if (!started)
	    g.drawString("PAUSED! PRESS ENTER TO START (P to pause again)", WIDTH / 3, HEIGHT / 2);
    }

    @Override
    /**
     * Updates all targets and players and background
     */
    public void update(GameContainer container, int delta) throws SlickException {

	if (!started)
	    return;
	// TODO Auto-generated method stub
	bgrX -= TARGET_SPEED * 0.2;
	for (Player p : players)
	    p.update(delta);
	for (Target t : targets)
	    t.update(delta);
	if (getLastTargetX() < 600)
	    addTargets(WIDTH + 100);

	background.update(delta);

	TARGET_SPEED += PLAYER_SPEED_INCREASE;
	PLAYER_SPEED += TARGET_SPEED_INCREASE;
    }

    @Override
    /**
    * Process pressed keys (delegates to player objects)
    */
    public void keyPressed(int key, char c) {
	for (Player p : players)
	    p.keyPressed(key);

	if (key == Input.KEY_ENTER)
	    started = true;

	if (key == Input.KEY_ESCAPE)
	    System.exit(0);

	if (key == Input.KEY_P)
	    started = false;

	if (key == Input.KEY_E) {
	    for (Player p : players)
		p.explode();
	}

    }

    @Override
    public void keyReleased(int key, char c) {
	for (Player p : players)
	    p.keyReleased(key);
    }

    /**
     * Main method and starting point
     * @param argv
     */
    public static void main(String[] argv) {

	try {
	    AppGameContainer container = new AppGameContainer(new FlappyRoihu());
	    container.setDisplayMode(WIDTH, HEIGHT, false);
	    container.setFullscreen(CONFIG.getBoolean("screen.fulscreen"));
	    container.setShowFPS(true);
	    container.setVSync(CONFIG.getBoolean("screen.vsync"));
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }

    private void addTargets(float xLoc) {

	int[] order = getShuffledArray(players.size());
	float startH = CONFIG.getFloat("target.height");
	//startH -= rnd.nextInt(HEIGHT / 6);
	for (int i = 0; i < players.size(); i++) {
	    targets.addElement(new Target(players.get(order[i]), startH, xLoc + players.get(order[i]).getXLoc()));
	    startH += targets.get(i).getSize() + 30;
	}
    }

    private float getLastTargetX() {
	float lastTargetX = 0;
	for (Target t : targets)
	    if (t.getXloc() > lastTargetX)
		lastTargetX = t.getXloc();
	return lastTargetX;
    }

    /**
     * Returns array with numbers 0 - size in random order
     * @param size
     * @return
     */
    public static int[] getShuffledArray(int size) {

	int[] ar = new int[size];
	for (int i = 0; i < size; i++)
	    ar[i] = i;
	for (int i = ar.length - 1; i > 0; i--) {
	    int index = rnd.nextInt(i + 1);
	    // Simple swap
	    int a = ar[index];
	    ar[index] = ar[i];
	    ar[i] = a;
	}
	return ar;
    }

}
