package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.PlayerUtil;

public class Bobbing extends Mod {


	public Bobbing(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Increases the viewbobbing for fun.");
	}

	@Override
	public void onUpdate() {

		float maxYaw = 0.2f;

			if (PlayerUtil.isMoveKeys() && !mc.player.collidedHorizontally && PlayerUtil.isMovementInputMoving() && !mc.player.movementInput.sneaking) {

				if (mc.player.onGround) {

					if (mc.player.cameraYaw < maxYaw) {
						mc.player.cameraYaw += 0.0165F;
					}

				} else {

					if (mc.player.cameraYaw < maxYaw) {
						mc.player.cameraYaw += 0.0099F;
					}
				}
				if (mc.player.cameraYaw > maxYaw) {

					mc.player.cameraYaw = maxYaw;
				}
			} 
	}

}