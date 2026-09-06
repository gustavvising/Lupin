package me.timeswitcher.lupin.particlesystem;

import java.awt.Color;
import java.util.Random;

public class Particle {

	private Vector2f velocity;
	private float x;
	private float y;
	private float size;
	private Color color;

	private static final Random R = new Random();

	public Particle(Vector2f velocity, float x, float y, float size) {
		this.setVelocity(velocity);
		this.setX(x);
		this.setY(y);
		this.setSize(size);
		this.setColor(Color.DARK_GRAY);
	}

	public static Particle generateParticles(int width, int height) {
		Vector2f velocity = new Vector2f((float) (Math.random() * 2.0f - 1.0f), (float) (Math.random() * 2.0f - 1.0f));
		float x = R.nextInt(width);
		float y = R.nextInt(height);
		float size = (float) (Math.random() * 4.0f) + 1.0f;
		return new Particle(velocity, x, y, size);
	}

	public void tick(int width, int height, float speed) {
		x += velocity.getX() * speed;
		y += velocity.getY() * speed;

		if (x > width) x = 0;
		if (x < 0) x = width;

		if (y > height) y = 0;
		if (y < 0) y = height;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getDistanceTo(Particle particle1) {
		return getDistanceTo(particle1.getX(), particle1.getY());
	}

	public float getDistanceTo(float x, float y) {
		return (float) distance(getX(), getY(), x, y);
	}

	public static double distance(float x, float y, float x1, float y1) {
		return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
	}

}