package me.timeswitcher.lupin.utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.main.LupinUser;
import me.timeswitcher.lupin.screens.LupinClickGuiScreen;
import me.timeswitcher.lupin.screens.LupinMainMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.util.ResourceLocation;

public class LupinUtil {

	private static boolean loggedIn = false;
	private final static String URL = "https://pastebin.com/raw/5tk4c3zb";

	private static boolean loginFailed = false;
	
	public static final ResourceLocation LUPINLOGO = new ResourceLocation("modid", "lupinlogo.png");
	
	private static LupinMainMenuScreen mainMenu = null;
	private static LupinClickGuiScreen clickGui = null;
	private static int CLICKGUIKEY = GLFW.GLFW_KEY_RIGHT_SHIFT;

	private static ArrayList<String> friends = new ArrayList<String>();

	private static boolean didSetupRendererLayer = false;

	private static boolean showLogo = true;
	private static boolean showInfoHud = true;
	private static boolean renderModList = true;

	private static boolean resize = false;
	private static int guiScale = 0;
	
	public static boolean forceopSent = false;
	public static boolean blockedOppedMessage = false;

	public static void login(final String UID) {

		setLoginFailed(false);

		HttpURLConnection connection = null;

		try {

			connection = WebUtil.openConnection(URL);
			final String response = WebUtil.buildResponse(connection.getInputStream());

			if (response.contains(UID + ":" + HwidUtil.getHWID())) {
				LupinUser.setUID(UID);
				LupinUtil.setLoggedIn(true);
				Lupin.mc.displayGuiScreen(new MainMenuScreen());

			} else {

				setLoginFailed(true);
			}

		} catch (final IOException e) {

		} finally {
			if (connection != null) {
				connection.disconnect();

			}
		}
	}
	
	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static void setLoggedIn(boolean loggedIn) {
		LupinUtil.loggedIn = loggedIn;
	}

	public static boolean isLoginFailed() {
		return loginFailed;
	}

	public static void setLoginFailed(boolean loginFailed) {
		LupinUtil.loginFailed = loginFailed;
	}
	
	public static LupinMainMenuScreen getMainMenu() {
		return mainMenu;
	}

	public static void setMainMenu(LupinMainMenuScreen mainMenu) {
		LupinUtil.mainMenu = mainMenu;
	}

	public static LupinClickGuiScreen getClickGui() {
		return clickGui;
	}

	public static void setClickGui(LupinClickGuiScreen clickGui) {
		LupinUtil.clickGui = clickGui;
	}

	public static int getCLICKGUIKEY() {
		return CLICKGUIKEY;
	}

	public static void setCLICKGUIKEY(int CLICKGUIKEY) {
		LupinUtil.CLICKGUIKEY = CLICKGUIKEY;
	}

	public static ArrayList<String> getFriends() {
		return friends;
	}

	public static void setFriends(ArrayList<String> friends) {
		LupinUtil.friends = friends;
	}

	public static boolean isDidSetupRendererLayer() {
		return didSetupRendererLayer;
	}

	public static void setDidSetupRendererLayer(boolean didSetupRendererLayer) {
		LupinUtil.didSetupRendererLayer = didSetupRendererLayer;
	}

	public static boolean isShowLogo() {
		return showLogo;
	}

	public static void setShowLogo(boolean showLogo) {
		LupinUtil.showLogo = showLogo;
	}

	public static boolean isShowInfoHud() {
		return showInfoHud;
	}

	public static void setShowInfoHud(boolean showInfoHud) {
		LupinUtil.showInfoHud = showInfoHud;
	}

	public static boolean isRenderModList() {
		return renderModList;
	}

	public static void setRenderModList(boolean renderModList) {
		LupinUtil.renderModList = renderModList;
	}

	public static int getGuiScale() {
		return guiScale;
	}

	public static void setGuiScale(int guiScale) {
		LupinUtil.guiScale = guiScale;
	}

	public static boolean isResize() {
		return resize;
	}

	public static void setResize(boolean resize) {
		LupinUtil.resize = resize;
	}
}