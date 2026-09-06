package me.timeswitcher.lupin.utility;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;

public class GameUtil {

	public static boolean isFullScreen() {
		return Lupin.mc.getMainWindow().isFullscreen();
	}
	
	public static void setFullScreen() {
		if (!isFullScreen()) {
		Lupin.mc.getMainWindow().toggleFullscreen();
		}
	}

	public static boolean isDebugInfo() {
		return Lupin.mc.gameSettings.showDebugInfo;
	}

	public static boolean isGameNull() {
		return Lupin.mc == null || Lupin.mc.world == null || Lupin.mc.player == null || Lupin.mc.player.world == null;
	}

	public static boolean isRenderNull() {
		return Lupin.mc.getTextureManager() == null || Lupin.mc.gameRenderer == null || Lupin.mc.worldRenderer == null;
	}

	public static boolean isServerNull() {
		return Lupin.mc.isSingleplayer() || Lupin.mc.getCurrentServerData() == null;
	}

	public static boolean isChatOpen() {
		return Lupin.mc.ingameGUI.getChatGUI().getChatOpen();
	}

	public static void clearChat() {
		Lupin.mc.ingameGUI.getChatGUI().clearChatMessages(true);
	}

	public static void setKey(KeyBinding key, boolean held) {
		KeyBinding.setKeyBindState(key.getKey(), held);
	}

	public static boolean isKeyDown(KeyBinding key) {
		return key.isKeyDown();
	}

	public static String getKeyName(KeyInputEvent e) {
		int keyCode = e.getKey();
		int scanCode = e.getScanCode();
		return GLFW.glfwGetKeyName(keyCode, scanCode);
	}

	public static void resize() {
		if (LupinUtil.isResize()) {
			Lupin.mc.gameSettings.guiScale = LupinUtil.getGuiScale();
			Lupin.mc.updateWindowSize();
			LupinUtil.setGuiScale(0);
			LupinUtil.setResize(false);
		}
	}
}