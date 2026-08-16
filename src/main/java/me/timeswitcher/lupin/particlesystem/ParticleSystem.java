package me.timeswitcher.lupin.particlesystem;

import java.util.ArrayList;

import me.timeswitcher.lupin.utility.RenderUtil;

public class ParticleSystem {

	private final float SPEED = 1.2f;
	private final float MAXDISTANCE = 35f;

	private ArrayList<Particle> particles = new ArrayList<Particle>();

	public void addParticles(int width, int height, int amount) {
		for (int i = 0; i < amount; i++) {
			particles.add(Particle.generateParticles(width, height));
		}
	}

	public void tick(int width, int height) {
		for (Particle particle : particles) {
			particle.tick(width, height, SPEED);
		}
	}

	public void render(int MOUSEX, int MOUSEY) {

		for (Particle particle : particles) {

			RenderUtil.drawRect(particle.getX(), particle.getY(), particle.getX() + (particle.getSize() / 2), particle.getY() + (particle.getSize() / 2), particle.getColor().getRGB());

			float dist = particle.getDistanceTo(MOUSEX, MOUSEY);

			if (dist <= MAXDISTANCE) {

				RenderUtil.drawShape(particle.getX(), particle.getY(), dist, 3, particle.getColor().getRGB());
			}
		}
	}
}