package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;

public class HighJump extends Mod {

	public static boolean jumped = false;
	public static final Time GROUND_TIMER = new Time();

	public HighJump(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_H, "Makes you jump high.");
	}

	@Override
	public void onDisable() {
		if (jumped && PlayerUtil.motionY() > 0.0d) {
			PlayerUtil.setMotion(PlayerUtil.motionX(), -0.07544406518948656d, PlayerUtil.motionZ());
		}
		ModsUtil.resetTimerSpeed();
	}

	@Override
	public void onUpdate() {
		
		if (!mc.player.onGround) {
			GROUND_TIMER.reset();
		}

		if (!Lupin.instance.getModHandler().getModByName("Fly").isToggled()) {

			if (jumped) {

				if (mc.player.onGround || mc.player.fallDistance > 0.0f || PlayerUtil.isFlying()) {

					ModsUtil.resetTimerSpeed();
				}
			}
		}
	}
}