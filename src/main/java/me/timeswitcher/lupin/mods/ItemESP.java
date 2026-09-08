package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Slider;

public class ItemESP extends Mod {

	public static final Slider nametagSize = new Slider("Nametag size", 25.0f, 0.0f, 100.0f, 10.0f, false);

	public static final CheckBox nametag = new CheckBox("Nametag", true);

	public ItemESP(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Renders a box around items in the world.");
		this.getSliders().add(nametagSize);
		this.getCheckBoxes().add(nametag);
	}

}