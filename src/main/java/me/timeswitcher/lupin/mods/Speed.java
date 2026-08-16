package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.network.play.client.CPlayerPacket;

public class Speed extends Mod {

	private final Time SPEED_TIMER = new Time();

	public Speed(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_M, "Move fast.");
	}

	@Override
	public void onDisable() {
		ModsUtil.resetTimerSpeed();
	}

	@Override
	public void onUpdate() {
		
		if (ModsUtil.canSpeed()) {

			if (!mc.player.onGround) {
			
			final float direction = mc.player.rotationYaw + ((mc.player.moveForward < 0.0f) ? 180 : 0) + ((mc.player.moveStrafing > 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f) - ((mc.player.moveStrafing < 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
			final double xDir = Math.cos((direction + 90.0f) * Math.PI / 180.0);
			final double zDir = Math.sin((direction + 90.0f) * Math.PI / 180.0);
			
			double speed = 0;
			
			if ((mc.gameSettings.keyBindForward.isKeyDown() || (mc.gameSettings.keyBindForward.isKeyDown() && (mc.gameSettings.keyBindLeft.isKeyDown()
					|| mc.gameSettings.keyBindRight.isKeyDown()))) && !mc.gameSettings.keyBindBack.isKeyDown()) {
				
				speed = 0.27d;
				}
			
			PlayerUtil.setMotion(xDir * speed, PlayerUtil.motionY(), zDir * speed);
			}
			
		} else {

			ModsUtil.resetTimerSpeed();
		}
	}
}