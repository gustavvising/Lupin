package me.timeswitcher.lupin.utility;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.util.text.StringTextComponent;

public class ChatUtil {

	public static void sendClientMessage(String message) {
		Lupin.mc.player.sendMessage(new StringTextComponent(Lupin.PREFIX + message));
	}

	public static void sendClientMessageNoPrefix(String message) {
		Lupin.mc.player.sendMessage(new StringTextComponent(message));
	}

	public static void sendChatMessage(String message) {
		Lupin.mc.player.sendChatMessage(message);
	}
}