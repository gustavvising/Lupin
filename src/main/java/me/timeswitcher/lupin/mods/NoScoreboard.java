package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import net.minecraft.scoreboard.ScoreObjective;

public class NoScoreboard extends Mod {

	public static ScoreObjective lastScoreObjective = null;

	public NoScoreboard(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Removes the scoreboard from the screen.");
	}

	@Override
	public void onDisable() {
		if (!GameUtil.isGameNull() && !GameUtil.isRenderNull()) {

			try {

				if (lastScoreObjective != null) {
					Lupin.mc.world.getScoreboard().setObjectiveInDisplaySlot(1, lastScoreObjective);
					lastScoreObjective = null;
				}
			} catch (Exception e) {

			}
		}
	}
}