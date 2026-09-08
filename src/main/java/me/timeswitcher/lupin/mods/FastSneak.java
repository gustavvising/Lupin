package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;

public class FastSneak extends Mod {

	private final Time speedTimer = new Time();

	private final Mode slower = new Mode("Slower");
	private final Mode faster = new Mode("Faster");

	public FastSneak(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Decreases the sneak movement slowdown.");
		this.setCurrentMode(slower);
		this.getModes().add(slower);
		this.getModes().add(faster);
	}

	@Override
	public void onUpdate() {
		if (ModsUtil.canFastSneak() && PlayerUtil.isMovementInputMoving()) {

			if (mc.player.isSprinting()) {
				mc.player.setSprinting(false);
			}
			double speedAdd = 0;

			if (speedTimer.isDelayComplete(200)) {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || (mc.gameSettings.keyBindForward.isKeyDown() && (mc.gameSettings.keyBindLeft.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown()))) && !mc.gameSettings.keyBindBack.isKeyDown()) {
					if (this.getCurrentMode().equals(slower)) {
						speedAdd = 0.11d;
					} else {
						speedAdd = 0.25d;
					}
				} else {
					if (this.getCurrentMode().equals(slower)) {
						speedAdd = 0.11d;
					} else {
						speedAdd = 0.18d;
					}
				}
			}
			double speed = (PlayerUtil.getMoveSpeed() / 0.05F) + speedAdd;
			PlayerUtil.setMoveSpeed(speed);
		} else {
			speedTimer.reset();
		}
	}

}