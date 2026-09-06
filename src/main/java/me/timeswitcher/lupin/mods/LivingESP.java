package me.timeswitcher.lupin.mods;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.utility.GameUtil;
import net.minecraft.entity.LivingEntity;

public class LivingESP extends Mod {

	public static final Mode LINES = new Mode("Lines");
	public static final Mode BOX = new Mode("Box");
	public static final Mode GLOW = new Mode("Glow");

	public static final CheckBox ONLY_PLAYERS = new CheckBox("Only Players", false);

	public static boolean addedGlow;
	public static ArrayList<LivingEntity> glowingEntities = new ArrayList<LivingEntity>();

	public LivingESP(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Render lines around entities.");
		this.setCurrentMode(LINES);
		this.getModes().add(LINES);
		this.getModes().add(BOX);
		this.getModes().add(GLOW);
		this.getCheckBoxes().add(ONLY_PLAYERS);
	}

	@Override
	public void onUpdate() {

		if (addedGlow) {

			if (!this.getCurrentMode().equals(GLOW)) {

				if (!glowingEntities.isEmpty()) {

					for (LivingEntity entity : glowingEntities) {

						if (entity != null) {

                            if (entity.isGlowing()) {
                                entity.setGlowing(false);
                            }
                        }
					}
					addedGlow = false;
					glowingEntities.clear();
				}
			}
		}
	}

	@Override
	public void onDisable() {

		addedGlow = false;

		if (!GameUtil.isGameNull()) {

			try {

				if (mc.world.getAllEntities() != null) {

					for (LivingEntity entity : glowingEntities) {

						if (entity != null) {

                            if (entity.isGlowing()) {
                                entity.setGlowing(false);
                            }
                        }
					}
				}

			} catch (Exception ignored) {

			}
		}
	}

}