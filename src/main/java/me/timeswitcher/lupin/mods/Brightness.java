package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class Brightness extends Mod {

	private double oldGamma;
	private final double INCREASEDGAMMA = 16.0d;

	public Brightness(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Makes the world brighter.");
	}

	@Override
	public void onEnable() {
		setOldGamma(getGamma());
		setGamma(getINCREASEDGAMMA());
	}

	@Override
	public void onDisable() {
		resetGamma();
	}

	public double getGamma() {
		return mc.gameSettings.gamma;
	}

	public void setGamma(double gamma) {
		mc.gameSettings.gamma = gamma;
	}

	public void resetGamma() {
		setGamma(getOldGamma());
	}

	public double getOldGamma() {
		return oldGamma;
	}

	public void setOldGamma(double oldGamma) {
		this.oldGamma = oldGamma;
	}

	public double getINCREASEDGAMMA() {
		return INCREASEDGAMMA;
	}

}