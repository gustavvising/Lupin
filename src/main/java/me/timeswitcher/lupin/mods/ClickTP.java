package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import net.minecraft.util.math.RayTraceResult;

public class ClickTP extends Mod {
	
	public ClickTP(String name) {
		super(name, Category.WORLD, GLFW.GLFW_KEY_UNKNOWN, "Teleports you to selected position.");
	}
	
	@Override
	public void onUpdate() {
		
	}
}
