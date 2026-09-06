package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import net.minecraft.potion.Effects;

public class AntiBlind extends Mod {

	public AntiBlind(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Removes nausea, blindness & pumpkin overlay.");
	}

	@Override
	public void onUpdate() {
		if (mc.player.isPotionActive(Effects.NAUSEA)) {
			mc.player.removePotionEffect(Effects.NAUSEA);
		}
		if (mc.player.isPotionActive(Effects.BLINDNESS)) {
			mc.player.removePotionEffect(Effects.BLINDNESS);
		}
	}

}