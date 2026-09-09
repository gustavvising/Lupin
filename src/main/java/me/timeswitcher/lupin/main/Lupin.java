package me.timeswitcher.lupin.main;

import me.timeswitcher.lupin.managers.AltManager;
import me.timeswitcher.lupin.managers.EventListener;
import me.timeswitcher.lupin.managers.FontManager;
import me.timeswitcher.lupin.managers.ModHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("lupin")
public class Lupin {

	public static final String NAME = "Lupin";
	public static final String PREFIX = "\u00A7f\u00A7lLupin\u00A78\u00A7k:\u00A7r\u00A77 ";
	public static final String VERSION = "1.2";
	
	public static Lupin instance;

	public static Minecraft mc = Minecraft.getInstance();

	private final EventListener eventListener = new EventListener();
	private final ModHandler modHandler = new ModHandler();
	private final AltManager altManager = new AltManager();
	private final FontManager fontManager = new FontManager();
	
	public Lupin() {
		instance = this;

		MinecraftForge.EVENT_BUS.register(eventListener);
	}

	public EventListener getEventListener() {
		return eventListener;
	}

	public ModHandler getModHandler() {
		return modHandler;
	}

	public AltManager getAltManager() {
		return altManager;
	}

	public FontManager getFontManager() {
		return fontManager;
	}
}