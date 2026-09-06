package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class Respawn extends Mod {

	public Respawn(String name) {
		super(name, Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN, "Automatically respawns the player on death.");
	}

	@Override
	public void onUpdate() {

		if (mc.player.deathTime > 0 && mc.currentScreen != null) {
			mc.player.respawnPlayer();
		}
	}

}