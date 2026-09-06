package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.MathUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.client.CPlayerPacket.PositionRotationPacket;
import net.minecraft.util.math.BlockPos;

public class FastClimb extends Mod {

	private final Time CLIMBDOWN_TIMER = new Time();
	private final Time JUMP_TIMER = new Time();

	public FastClimb(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Climb ladders and vines faster.");
	}

	@Override
	public void onUpdate() {

		if (PlayerUtil.climb()) {

			if (JUMP_TIMER.isDelayComplete(1000f)) {

				boolean up = PlayerUtil.isMovementInputForward(0.8F) ? true : false;

				double speedTPUp = 0.0021d; double speedTPDown = 0.007d;

				if (up) {

					CLIMBDOWN_TIMER.reset();

					PlayerUtil.setPos(PlayerUtil.posX(), PlayerUtil.posY() + speedTPUp,PlayerUtil.posZ());

				} else {

					if (CLIMBDOWN_TIMER.isDelayComplete(500f)) {

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