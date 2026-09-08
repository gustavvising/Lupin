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

	public static final Mode lines = new Mode("Lines");
	public static final Mode box = new Mode("Box");
	public static final Mode glow = new Mode("Glow");

	public static final CheckBox onlyPlayers = new CheckBox("Only Players", false);

	public static boolean addedGlow;
	public static ArrayList<LivingEntity> glowingEntities = new ArrayList<LivingEntity>();

	public LivingESP(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Render lines around entities.");
		this.setCurrentMode(lines);
		this.getModes().add(lines);
		this.getModes().add(box);
		this.getModes().add(glow);
		this.getCheckBoxes().add(onlyPlayers);
	}

	@Override
	public void onUpdate() {

		if (addedGlow) {

			if (!this.getCurrentMode().equals(glow)) {

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