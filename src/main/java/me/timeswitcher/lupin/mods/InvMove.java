package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class InvMove extends Mod {

	public static boolean forward;
	public static boolean backward;
	public static boolean left;
	public static boolean right;
	public static boolean jump;
	public static boolean sneak;
	public static boolean sprint;
	public static boolean perspective;

	public InvMove(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Makes you able to move, sprint, sneak & toggle perspective while in inventory.");
	}

	@Override
	public void onUpdate() {
		if (mc.currentScreen == null) {
			forward = false;
			backward = false;
			left = false;
			right = false;
			jump = false;
			sneak = false;
			sprint = false;
			perspective = false;
		}
	}
}