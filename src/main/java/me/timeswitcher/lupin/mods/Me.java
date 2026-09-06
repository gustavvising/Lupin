package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class Me extends Mod {

	public Me(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Renders a little version of the player on the screen.");
	}

}