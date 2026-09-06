package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.BlockPos;

public class VClip extends Mod {

	private final int MAX_DISTANCE = 9;

	private boolean jump = false;
	private boolean sneak = false;
	private boolean normalSneak = false;

	private double y = 0;
	private BlockPos tpPos = null;

	public static double up = 0;
	public static double down = 0;

	public VClip(String name) {
		super(name, Category.WORLD, GLFW.GLFW_KEY_U, "Allows you to teleport up and down. Tip, also see .vclip <distance>.");
	}

	@Override
	public void onDisable() {
		jump = false;
		sneak = false;
		normalSneak = false;
		y = 0;
		tpPos = null;
		up = 0;
		down = 0;
	}

	private void clip(double value) {
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(tpPos.getX() + 0.5d, PlayerUtil.posY() + value, tpPos.getZ() + 0.5d, mc.player.onGround));
		PlayerUtil.setPos(tpPos.getX() + 0.5d, PlayerUtil.posY() + value, tpPos.getZ() + 0.5d);
		y = 0;
		tpPos = null;
		up = 0;
		down = 0;
	}

	@SuppressWarnings("deprecation")
	private boolean isSolid(Block block) {
		return block.getMaterial(block.getDefaultState()).isSolid();
	}

	private boolean UP(boolean checkOnly) {

		BlockPos pos = new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() + 2, PlayerUtil.posZ());

		for (int i = 0; i < (MAX_DISTANCE - 1); i++) {

			Block block = mc.world.getBlockState(pos).getBlock();

			if (block == Blocks.AIR) {

				if (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.down()).getBlock() != Blocks.AIR && isSolid( mc.world.getBlockState(pos.down()).getBlock())) {
					if (!checkOnly) {
						y = pos.getY() - PlayerUtil.posY();
						tpPos = pos;
					} else {
						up = pos.getY() - PlayerUtil.posY();
					}
					return true;
				}
			}
			pos = new BlockPos(pos.up());
		}
		return false;
	}

	private boolean DOWN(boolean checkOnly) {

		BlockPos pos = new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() - 2, PlayerUtil.posZ());

		for (int i = 0; i < (MAX_DISTANCE - 1); i++) {

			Block block = mc.world.getBlockState(pos).getBlock();

			if (block == Blocks.AIR) {

				if (mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR && mc.world.getBlockState(pos.up()).getBlock() != Blocks.AIR && isSolid( mc.world.getBlockState(pos.up()).getBlock())) {
					if (!checkOnly) {
						y = (PlayerUtil.posY() + 1) - pos.getY();
						tpPos = pos.down();
					} else {
						down = (PlayerUtil.posY() + 1) - pos.getY();
					}
					return true;
				}
			}
			pos = new BlockPos(pos.down());
		}
		return false;
	}

	@Override
	public void onUpdate() {

		if (ModsUtil.canVClip()) {

			if (!jump && !sneak && mc.player.onGround && mc.world.getBlockState(new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() + 2, PlayerUtil.posZ())).getBlock() != Blocks.AIR) {
				up = UP(true) ? up : 0;
				if (up == 1) {	
					up = 0;
				}
			} else {
				up = 0;
			}
			if (!sneak && !normalSneak && !jump && mc.player.onGround) {
				down = DOWN(true) ? down : 0;
				if (down == 1) {	
					down = 0;
				}
			} else {
				down = 0;
			}

			if (jump && !mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround) {
				jump = false; 
			} 
			if (!jump && !sneak && mc.gameSettings.keyBindJump.isKeyDown()) {

				if (mc.player.onGround && mc.world.getBlockState(new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() + 2, PlayerUtil.posZ())).getBlock() != Blocks.AIR) {

					if (UP(false)) {
						if (y > 1) {
							clip((y));
							jump = true;
						}
					} 
				} 
			}
			if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
				normalSneak = false;
			}
			if (sneak && !mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.onGround) {

				sneak = false;
			}
			if (!sneak && !normalSneak && !jump && mc.gameSettings.keyBindSneak.isKeyDown()) {

				if (mc.player.onGround) {

					if (DOWN(false)) {
						if (y > 1) {
							GameUtil.setKey(mc.gameSettings.keyBindSneak, false);
							mc.player.movementInput.sneaking = false;
							clip(-y);
							sneak = true;
						}
					} else {

						normalSneak = true;
					}
				}
			}
		}
	}

}