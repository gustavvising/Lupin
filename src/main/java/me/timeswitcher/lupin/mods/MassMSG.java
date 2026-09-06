package me.timeswitcher.lupin.mods;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.MathUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.client.network.play.NetworkPlayerInfo;

public class MassMSG extends Mod {

	public static ArrayList<String> recipients = new ArrayList<String>();

	public static String message = "Default MassMSG, change it with .massmsg <message>";

	private final Time MESSAGE_TIMER = new Time();

	public MassMSG(String name) {
		super(name, Category.EXTRA, GLFW.GLFW_KEY_UNKNOWN, "Sends a /msg <message> to players on the server.");
	}

	@Override
	public void onUpdate() {

		if (!GameUtil.isGameNull()) {

			if (!mc.getConnection().getPlayerInfoMap().isEmpty()) {

				Collection<NetworkPlayerInfo>players = mc.getConnection().getPlayerInfoMap();
				
				players.forEach((player) -> {

					String name = player.getGameProfile().getName();

					if (!mc.player.getName().getFormattedText().equalsIgnoreCase(name) && !recipients.contains(name) && MESSAGE_TIMER.isDelayComplete(MathUtil.getRandomInt(3024, 5024) + R.nextFloat() + R.nextFloat()) && !name.isEmpty() && !name.contains(" ") && name.length() <= 16F && name.length() >= 3F) {

						mc.player.sendChatMessage("/msg " + name + " " + message);

						recipients.add(name);

						MESSAGE_TIMER.reset();
					}
				});
			}
		}
	}

}