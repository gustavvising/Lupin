package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;

public class FastSneak extends Mod {

	private final Time SPEED_TIMER = new Time();

	private final Mode SLOWER = new Mode("Slower");
	private final Mode FASTER = new Mode("Faster");

	public FastSneak(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Decreases the sneak movement slowdown.");
		this.setCurrentMode(SLOWER);
		this.getModes().add(SLOWER);
		this.getModes().add(FASTER);
	}

	@Override
	public void onUpdate() {
		if (ModsUtil.canFastSneak() && PlayerUtil.isMovementInputMoving()) {

			if (mc.player.isSprinting()) {
				mc.player.setSprinting(false);
			}
			double speedAdd = 0;

			if (SPEED_TIMER.isDelayComplete(200)) {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || (mc.gameSettings.keyBindForward.isKeyDown() && (mc.gameSettings.keyBindLeft.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown()))) && !mc.gameSettings.keyBindBack.isKeyDown()) {
					if (this.getCurrentMode().equals(SLOWER)) {
						speedAdd = 0.11d;
					} else {
						speedAdd = 0.25d;
					}
				} else {
					if (this.getCurrentMode().equals(SLOWER)) {
						speedAdd = 0.11d;
					} else {
						speedAdd = 0.18d;
					}
				}
			}
			double speed = (PlayerUtil.getMoveSpeed() / 0.05F) + speedAdd;
			PlayerUtil.setMoveSpeed(speed);
		} else {
			SPEED_TIMER.reset();
		}
	}
}