package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.Time;

public class Twerk extends Mod {

	private final Time TWERK_TIMER = new Time();

	private final Slider DELAY = new Slider("Twerk Delay", 50.0f, 1.0f, 100.0f, 500.0f, true);

	public Twerk(String name) {
		super(name, Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN, "Sneaks and unsneaks. Twerking in minecraft.");
		this.getSliders().add(DELAY);
	}

	@Override
	public void onDisable() {
		GameUtil.setKey(mc.gameSettings.keyBindSneak, false);
	}

	@Override
	public void onUpdate() {

		if (ModsUtil.canTwerk()) {

			if (TWERK_TIMER.isDelayComplete(DELAY.getReturnValue())) {
				GameUtil.setKey(mc.gameSettings.keyBindSneak, mc.player.movementInput.sneaking ? false : true);
				TWERK_TIMER.reset();
			}
		}
	}
}