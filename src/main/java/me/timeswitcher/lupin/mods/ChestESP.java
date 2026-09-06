package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class ChestESP extends Mod {

	public ChestESP(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Draws a box around chests in the world.");
	}

}