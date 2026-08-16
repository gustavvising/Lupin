package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;

public class NoSlowdown extends Mod {

	public NoSlowdown(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Removes the speed slowdown from using items, moving in cobweb & walking on soulsand.");
	}

	@Override
	public void onUpdate() {
		
		if (ModsUtil.canNoWeb()) {
			
			if (PlayerUtil.isMovementInputMoving()) {

				GameUtil.setKey(mc.gameSettings.keyBindSprint, false);
				
				if (mc.player.isSprinting()) {
					mc.player.setSprinting(false);
				}
				double speedAdd = 0.3d;
				double speed = (PlayerUtil.getMoveSpeed() / 0.05F) + speedAdd;

				PlayerUtil.setMoveSpeed(speed);
			}
		}
		if (ModsUtil.canNoSoulsand()) {
			PlayerUtil.setMoveSpeed(0.07d);
		}
	}
	
	public static void noSlowdown() {

		final float SPEED = 1.0F;
		final float SNEAKSPEED = 0.3F;

		if (GameUtil.isKeyDown(mc.gameSettings.keyBindForward)) {

			if (mc.player.isCrouching() || mc.player.movementInput.sneaking) {

				mc.player.movementInput.moveForward = SNEAKSPEED;

			} else {

				mc.player.movementInput.moveForward = SPEED;
			}
		}
		if (GameUtil.isKeyDown(mc.gameSettings.keyBindBack)) {

			if (mc.player.isCrouching() || mc.player.movementInput.sneaking) {

				mc.player.movementInput.moveForward = -SNEAKSPEED;

			} else {

				mc.player.movementInput.moveForward = -SPEED;
			}
		}
		if (GameUtil.isKeyDown(mc.gameSettings.keyBindLeft)) {

			if (mc.player.isCrouching() || mc.player.movementInput.sneaking) {

				mc.player.movementInput.moveStrafe = SNEAKSPEED;

			} else {

				mc.player.movementInput.moveStrafe = SPEED;
			}
		}
		if (GameUtil.isKeyDown(mc.gameSettings.keyBindRight)) {

			if (mc.player.isCrouching() || mc.player.movementInput.sneaking) {

				mc.player.movementInput.moveStrafe = -SNEAKSPEED;

			} else {

				mc.player.movementInput.moveStrafe = -SPEED;
			}
		}
	}
}