package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;

public class FancyChat extends Mod {

    public FancyChat(String name) {
		super(name, Category.EXTRA, GLFW.GLFW_KEY_UNKNOWN, "Replaces your chat letters with unicode characters.");
	}
	
	public static String convertStringFancyChat(String input) {
		String output = "";
		for (char c : input.toCharArray())
			output += convertCharFancyChat(c);

		return output;
	}

	private static String convertCharFancyChat(char c) {
		if (c < 0x21 || c > 0x80)
			return "" + c;

        String blacklist = "(){}[]|";
        if (blacklist.contains(Character.toString(c)))
			return "" + c;

		return new String(Character.toChars(c + 0xfee0));
	}

}