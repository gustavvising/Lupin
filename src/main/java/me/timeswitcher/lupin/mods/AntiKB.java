package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.PlayerUtil;

public class AntiKB extends Mod {

	private boolean jump = false;

	public AntiKB(String name) {
		super(name, Category.COMBAT, GLFW.GLFW_KEY_V, "Prevents damage knockback.");
	}

	@Override
	public void onUpdate() {
		if (!mc.player.onGround && mc.player.movementInput.jump) {
			jump = true;
		}
		if (jump && mc.player.onGround) {
			jump = false;
		}
		if (mc.player.hurtTime > 5) {
			PlayerUtil.setMotion(PlayerUtil.motionX() / 1.4d, jump ? PlayerUtil.motionY() : PlayerUtil.motionY() / 1.8d, PlayerUtil.motionZ() / 1.4d);
		}
	}
	
}