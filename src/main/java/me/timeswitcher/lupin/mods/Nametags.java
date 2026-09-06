package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Slider;

public class Nametags extends Mod {

	public static final Slider SIZE = new Slider("Size", 25.0f, 0.0f, 100.0f, 10.0f, false);

	public static final CheckBox ONLY_PLAYERS = new CheckBox("Only Players", false);
	public static final CheckBox HEALTH = new CheckBox("Health", true);

	public Nametags(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Encanched nametags with health display and custom size.");
		this.getSliders().add(SIZE);
		this.getCheckBoxes().add(ONLY_PLAYERS);
		this.getCheckBoxes().add(HEALTH);
	}

}