package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class FastClimb extends Mod {

	private final Time climbdownTimer = new Time();
	private final Time jumpTimer = new Time();

	public FastClimb(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Climb ladders and vines faster.");
	}

	@Override
	public void onUpdate() {

		if (PlayerUtil.climb()) {

			if (jumpTimer.isDelayComplete(1000f)) {

				boolean up = PlayerUtil.isMovementInputForward(0.8F);

				double speedTPUp = 0.0021d; double speedTPDown = 0.007d;

				if (up) {

					climbdownTimer.reset();

					PlayerUtil.setPos(PlayerUtil.posX(), PlayerUtil.posY() + speedTPUp,PlayerUtil.posZ());

				} else {

					if (climbdownTimer.isDelayComplete(500f)) {

						BlockPos pos = new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() - speedTPDown, PlayerUtil.posZ());

						if (mc.world.getBlockState(new BlockPos(pos)).getBlock() == Blocks.VINE || mc.world.getBlockState(new BlockPos(pos)).getBlock() == Blocks.LADDER)

							PlayerUtil.setPos(PlayerUtil.posX(), PlayerUtil.posY() - speedTPDown, PlayerUtil.posZ()); 
					}
				}
			}
		} else {

			ModsUtil.resetTimerSpeed();
		}
	}

}