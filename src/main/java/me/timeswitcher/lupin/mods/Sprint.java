package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;

public class Sprint extends Mod {

	public Sprint(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Automatically sets the player to sprinting.");
	}

	@Override
	public void onUpdate() {
		if (ModsUtil.canSprint()) {
			if (!(Lupin.instance.getModHandler().getModByName("No Slowdown").isToggled() && PlayerUtil.inWeb())) {
			mc.player.setSprinting(true);
			}
		}
	}

}