package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class Brightness extends Mod {

	private double oldGamma;

    public Brightness(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Makes the world brighter.");
	}

	@Override
	public void onEnable() {
		oldGamma = mc.gameSettings.gamma;
        mc.gameSettings.gamma = 16.0d;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gamma = oldGamma;
	}

}