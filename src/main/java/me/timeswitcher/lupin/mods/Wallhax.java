package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;

public class Wallhax extends Mod {

	public static final CheckBox onlyPlayers = new CheckBox("Only Players", false);

	public Wallhax(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Renders entities through blocks. (buggy)");
		this.getCheckBoxes().add(onlyPlayers);
	}

}