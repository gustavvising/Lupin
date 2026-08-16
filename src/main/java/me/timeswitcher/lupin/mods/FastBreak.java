package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class FastBreak extends Mod {

	public static float speed = 0.441F;

	public FastBreak(String name) {
		super(name, Category.WORLD, GLFW.GLFW_KEY_UNKNOWN, "Break blocks slightly faster than normal.");
	}

	@Override
	public void onUpdate() {

		if (R.nextDouble() < 0.8d) {

			if (R.nextBoolean()) {

				speed = 0.441f;

			} else {

				speed = 0.445f;
			}

		} else {

			speed = 0.444f;
		}
	}
}