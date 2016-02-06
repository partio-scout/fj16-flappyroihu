package fi.partio.flappyroihu.test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;

import fi.partio.flappyroihu.SparkEmitter;

/**
 * A particle test using built in effects
 *
 * @author kevin
 */
public class ParticleTest extends BasicGame {
    /** The particle system running everything */
    private ParticleSystem system;
    /** The particle blending mode */
    private int mode = ParticleSystem.BLEND_COMBINE;

    private float x = 0;
    private float y = 0;

    /**
     * Create a new test of graphics context rendering
     */
    public ParticleTest() {
	super("Particle Test");
    }

    /**
     * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
     */
    @Override
    public void init(GameContainer container) throws SlickException {
	Image image = new Image("testdata/spark.png", true);
	system = new ParticleSystem(image);

	system.addEmitter(new SparkEmitter(400, 300, 45, org.newdawn.slick.Color.blue));
	//system.addEmitter(new FireEmitter(200, 200, 10));
	//	system.addEmitter(new FireEmitter(600, 300, 30));

	//system.setUsePoints(true);
    }

    /**
     * @see org.newdawn.slick.BasicGame#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
     */
    @Override
    public void render(GameContainer container, Graphics g) {
	for (int i = 0; i < 10; i++) {
	    g.translate(1, 1);

	    system.render();
	}
	g.resetTransform();
	g.drawString("Press space to toggle blending mode", 200, 500);
	g.drawString("Particle Count: " + (system.getParticleCount() * 100), 200, 520);
    }

    /**
     * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer, int)
     */
    @Override
    public void update(GameContainer container, int delta) {
	system.update(delta);
	//x += 0.1;
	//y += 0.1;
	system.setPosition(x, y);
    }

    /**
     * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
     */
    @Override
    public void keyPressed(int key, char c) {
	if (key == Input.KEY_ESCAPE) {
	    System.exit(0);
	}
	if (key == Input.KEY_SPACE) {
	    mode = ParticleSystem.BLEND_ADDITIVE == mode ? ParticleSystem.BLEND_COMBINE : ParticleSystem.BLEND_ADDITIVE;
	    system.setBlendingMode(mode);
	}
    }

    /**
     * Entry point to our test
     * 
     * @param argv The arguments passed to the test
     */
    public static void main(String[] argv) {
	try {
	    AppGameContainer container = new AppGameContainer(new ParticleTest());
	    container.setDisplayMode(1200, 800, false);
	    container.start();
	} catch (SlickException e) {
	    e.printStackTrace();
	}
    }
}
