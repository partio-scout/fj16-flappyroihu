package fi.partio.flappyroihu;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class ExplodeEmiter implements ParticleEmitter {
    /** The x coordinate of the center of the fire effect */
    private int x;
    /** The y coordinate of the center of the fire effect */
    private int y;

    /** The particle emission rate */
    private int interval = 1;
    /** Time til the next particle */
    private int timer;
    /** The size of the initial particles */
    private float size = 40;

    private Color color;

    private Random rnd;

    private boolean particlesCreated;

    /**
     * Create a default fire effect at 0,0
     */
    public ExplodeEmiter() {
	rnd = new Random();
	particlesCreated = false;
    }

    /**
     * Create a default fire effect at x,y
     * 
     * @param x The x coordinate of the fire effect
     * @param y The y coordinate of the fire effect
     */
    public ExplodeEmiter(int x, int y) {
	this();
	this.x = x;
	this.y = y;
    }

    /**
     * Create a default fire effect at x,y
     * 
     * @param x The x coordinate of the fire effect
     * @param y The y coordinate of the fire effect
     * @param size The size of the particle being pumped out
     */
    public ExplodeEmiter(int x, int y, float size, Color color) {
	this();
	this.x = x;
	this.y = y;
	this.size = size;
	this.color = color;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#update(org.newdawn.slick.particles.ParticleSystem, int)
     */
    @Override
    public void update(ParticleSystem system, int delta) {
	timer -= delta;
	if (!particlesCreated) {
	    for (int i = 0; i < 200; i++) {

		timer = interval;
		Particle p = system.getNewParticle(this, 5000);
		//p.setColor(1, 1, 1, 0.5f);
		p.setColor(color.r, color.g, color.b, color.a);
		p.setPosition(x, y);
		p.setSize(size);
		float angle = rnd.nextFloat() * 2 * (float) Math.PI;
		float vx = (float) Math.sin(angle) * 0.4f;
		float vy = (float) Math.cos(angle) * 0.4f;
		p.setVelocity(vx, vy, 1.1f);
		particlesCreated = true;
	    }
	}
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#updateParticle(org.newdawn.slick.particles.Particle, int)
     */
    @Override
    public void updateParticle(Particle particle, int delta) {
	//if (particle.getSize() > 0)
	//  particle.adjustSize(-0.06f * delta * (size / 40.0f));

	particle.adjustSize(-0.19f * delta);
	float dxadj = (float) (rnd.nextGaussian()) * 0.05f;

	float dyadj = (float) (rnd.nextGaussian()) * 0.05f;
	dyadj += 0.01f; // make them fall a bit
	particle.adjustVelocity(0, dyadj);

	float c = 0.0007f * delta;
	float a = -0.0003f * delta;
	//particle.adjustColor(0, -c / 2, -c * 2, -c / 4);
	particle.adjustColor(c, c, c, a);

    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#isEnabled()
     */
    @Override
    public boolean isEnabled() {
	return true;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#completed()
     */
    @Override
    public boolean completed() {
	return false;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#useAdditive()
     */
    @Override
    public boolean useAdditive() {
	return false;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#getImage()
     */
    @Override
    public Image getImage() {
	return null;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#usePoints(org.newdawn.slick.particles.ParticleSystem)
     */
    @Override
    public boolean usePoints(ParticleSystem system) {
	return false;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#isOriented()
     */
    @Override
    public boolean isOriented() {
	return false;
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#wrapUp()
     */
    @Override
    public void wrapUp() {
    }

    /**
     * @see org.newdawn.slick.particles.ParticleEmitter#resetState()
     */
    @Override
    public void resetState() {
    }

    public void setSize(int size) {
	this.size = size;
    }

}
