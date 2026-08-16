package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;

public class LongJump extends Mod {

	private double runSpeed = 0.5d;
	private boolean jumped = false;
	private final Time LONGJUMP_TIMER = new Time();

	public LongJump(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_L, "Jump long.");
	}

	private boolean canDoLongJump() {
		return mc.player.onGround && LONGJUMP_TIMER.isDelayComplete(1000.0f) && PlayerUtil.isMovementInputForward(0.8f) && mc.gameSettings.keyBindJump.isKeyDown();
	}

	@Override
	public void onUpdate() {

		if (ModsUtil.canLongJump()) {

			if (!jumped) {

				if (canDoLongJump()) {

					PlayerUtil.setMoveSpeed(runSpeed);
					jumped = true;
					LONGJUMP_TIMER.reset();
				}

			} else {

				if (!mc.player.onGround) {

					if (PlayerUtil.isMovementInputForward(0.8f)) {

						final float direction = mc.player.rotationYaw + ((mc.player.moveForward < 0.0f) ? 180 : 0) + ((mc.player.moveStrafing > 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f) - ((mc.player.moveStrafing < 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
						final double xDir = Math.cos((direction + 90.0f) * Math.PI / 180.0);
						final double zDir = Math.sin((direction + 90.0f) * Math.PI / 180.0);

						double SPEED = 0;

						if (mc.player.fallDistance < 0.075444065f) {

							SPEED = 0.9d;
						}
						if (mc.player.fallDistance >= 0.075444065f && mc.player.fallDistance < 0.22777925f) {

							SPEED = 0.8d;
						}
						if (mc.player.fallDistance >= 0.22777925f && mc.player.fallDistance < 0.45546773f) {

							SPEED = 0.7d;
						}
						if (mc.player.fallDistance >= 0.45546773f && mc.player.fallDistance < 0.7570025f) {

							SPEED = 0.6d;
						}
						if (mc.player.fallDistance >= 0.7570025f && mc.player.fallDistance < 1.1309065f) {

							SPEED = 0.5d;
						}
						if (mc.player.fallDistance >= 1.1309065f) {

							SPEED = 0.4d;
						}
						PlayerUtil.setMotion(xDir * SPEED, PlayerUtil.motionY(), zDir * SPEED);

					} else {

						PlayerUtil.setMotion(0.0d, PlayerUtil.motionY(), 0.0d);
					}

				} else {

					if (LONGJUMP_TIMER.isDelayComplete(100.0f)) {

						jumped = false;
					}
				}
			}
		}
	}
}