package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.particles.ParticleTypes;

public class Trail extends Mod {

	private final Time TRAIL_TIMER = new Time();

	public Trail(String name) {
		super(name, Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, "Draw particles behind the player when moving. For fun.");
	}

	@Override
	public void onUpdate() {

		if (!GameUtil.isRenderNull()) {

			if (PlayerUtil.isMovementInputMoving() && TRAIL_TIMER.isDelayComplete(75.0f)) {

				boolean addition;

				if (R.nextDouble() > 0.5d) {

					addition = true;

				} else {

					addition = false;
				}
				double x = PlayerUtil.getHorizontalX(-1.0d);
				double z = PlayerUtil.getHorizontalZ(-1.0d);

				for (int i = 0; i < 30; i++) {

					mc.world.addParticle(ParticleTypes.END_ROD, x, PlayerUtil.posY() + 1.0d, z, addition ? R.nextDouble() : -R.nextDouble(), R.nextDouble(), addition ? R.nextDouble() : -R.nextDouble());
				}
				TRAIL_TIMER.reset();
			}
		}
	}
}