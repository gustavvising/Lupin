package me.timeswitcher.lupin.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.timeswitcher.lupin.mods.Aura;
import me.timeswitcher.lupin.mods.Bobbing;
import me.timeswitcher.lupin.mods.ChestStealer;
import me.timeswitcher.lupin.mods.ClickTP;
import me.timeswitcher.lupin.mods.Crasher;
import me.timeswitcher.lupin.mods.Disabler;
import me.timeswitcher.lupin.mods.LivingESP;
import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mods.AntiBlind;
import me.timeswitcher.lupin.mods.FancyChat;
import me.timeswitcher.lupin.mods.FastBreak;
import me.timeswitcher.lupin.mods.FastClimb;
import me.timeswitcher.lupin.mods.FastSneak;
import me.timeswitcher.lupin.mods.Fly;
import me.timeswitcher.lupin.mods.Brightness;
import me.timeswitcher.lupin.mods.Wallhax;
import me.timeswitcher.lupin.mods.HandSwitch;
import me.timeswitcher.lupin.mods.HighJump;
import me.timeswitcher.lupin.mods.InvMove;
import me.timeswitcher.lupin.mods.ItemESP;
import me.timeswitcher.lupin.mods.LongJump;
import me.timeswitcher.lupin.mods.MassMSG;
import me.timeswitcher.lupin.mods.NoFall;
import me.timeswitcher.lupin.mods.NoScoreboard;
import me.timeswitcher.lupin.mods.NoSlowdown;
import me.timeswitcher.lupin.mods.Respawn;
import me.timeswitcher.lupin.mods.Speed;
import me.timeswitcher.lupin.mods.Sprint;
import me.timeswitcher.lupin.mods.Step;
import me.timeswitcher.lupin.mods.ChestESP;
import me.timeswitcher.lupin.mods.TPAura;
import me.timeswitcher.lupin.mods.Nametags;
import me.timeswitcher.lupin.mods.Trail;
import me.timeswitcher.lupin.mods.Twerk;
import me.timeswitcher.lupin.mods.Me;
import me.timeswitcher.lupin.mods.VClip;
import me.timeswitcher.lupin.mods.AntiKB;

public class ModHandler {

	private HashMap<String, Mod> modMap = new HashMap<String, Mod>();

	public ModHandler() {
		initializeModList();
	}

	private void initializeModList() {
		addModToModList(new Me("Me"));
		addModToModList(new Fly("Fly"));
		addModToModList(new Aura("Aura"));
		addModToModList(new Step("Step"));
		addModToModList(new VClip("VClip"));
		addModToModList(new Twerk("Twerk"));
		addModToModList(new Trail("Trail"));
		addModToModList(new Speed("Speed"));
		addModToModList(new Sprint("Sprint"));
		addModToModList(new NoFall("No Fall"));
		addModToModList(new TPAura("TP Aura"));
		addModToModList(new AntiKB("Anti KB"));
		addModToModList(new Wallhax("Wallhax"));
		addModToModList(new Crasher("Crasher"));
		addModToModList(new Bobbing("Bobbing"));
		addModToModList(new Respawn("Respawn"));
		addModToModList(new ClickTP("Click TP"));
		addModToModList(new InvMove("Inv Move"));
		addModToModList(new ItemESP("Item ESP"));
		addModToModList(new MassMSG("Mass MSG"));
		addModToModList(new Nametags("Nametags"));
		addModToModList(new Disabler("Disabler"));
		addModToModList(new HighJump("High Jump"));
		addModToModList(new LongJump("Long Jump"));
		addModToModList(new ChestESP("Chest ESP"));
		addModToModList(new FastSneak("Fast Sneak"));
		addModToModList(new FastBreak("Fast Break"));
		addModToModList(new FastClimb("Fast Climb"));
		addModToModList(new AntiBlind("Anti Blind"));
		addModToModList(new FancyChat("Fancy Chat"));
		addModToModList(new LivingESP("Living ESP"));
		addModToModList(new Brightness("Brightness"));
		addModToModList(new NoSlowdown("No Slowdown"));
		addModToModList(new HandSwitch("Hand Switch"));
		addModToModList(new NoScoreboard("No Scoreboard"));
		addModToModList(new ChestStealer("Chest Stealer"));
	}

	private void addModToModList(Mod theMod) {
		if (!modMap.containsKey(theMod.getName())) {
			modMap.put(theMod.getName(), theMod);
		}
	}

	public Mod getModByName(String name) {
		return modMap.get(name);
	}

	public HashMap<String, Mod> getModMap() {
		return modMap;
	}

	public ArrayList<Mod> getEnabledMods() {
		ArrayList<Mod> activeMods = new ArrayList<>();

		for (Iterator<Mod> iterator = getModMap().values().iterator(); iterator.hasNext();) {

			Mod mod = (Mod)iterator.next();

			if (mod.isToggled() && !activeMods.contains(mod)) {

				activeMods.add(mod);
			}
		}
		activeMods.sort((Mod c1, Mod c2) -> (-1 * ((Lupin.mc.fontRenderer.getStringWidth(c1.getNameAndMode()) - Lupin.mc.fontRenderer.getStringWidth(c2.getNameAndMode())))));
		return activeMods;
	}

	public ArrayList<Mod> getAllMods() {
		ArrayList<Mod> ourMods = new ArrayList<Mod>(modMap.values());
		ourMods.sort((Mod c1, Mod c2) -> (-1 * ((Lupin.mc.fontRenderer.getStringWidth(c1.getName()) - Lupin.mc.fontRenderer.getStringWidth(c2.getName())))));
		return ourMods;
	}

	public ArrayList<Mod> getAllModsByCategory(Category category) {
		ArrayList<Mod> modsByCategory = new ArrayList<Mod>();

		for (Iterator<Mod> iterator = getModMap().values().iterator(); iterator.hasNext();) {

			Mod mod = (Mod)iterator.next();

			if (mod.getCategory() == category) {

				modsByCategory.add(mod);
			}
		}
		modsByCategory.sort((Mod c1, Mod c2) -> (-1 * ((Lupin.mc.fontRenderer.getStringWidth(c1.getName()) - Lupin.mc.fontRenderer.getStringWidth(c2.getName())))));
		return modsByCategory;
	}

	public void runEnabledMods() {
		for (Iterator<Mod> iterator = getEnabledMods().iterator(); iterator.hasNext();) {

			Mod mod = (Mod)iterator.next();

			if (mod.isToggled()) {

				mod.onUpdate();
			}
		}
	}
}