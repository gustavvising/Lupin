package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.network.play.client.CPlayerPacket.PositionRotationPacket;
import net.minecraft.util.math.BlockPos;

public class Step extends Mod {

	private final CheckBox ALLOW_GREATERTHAN_2B = new CheckBox("Allow height > 2 blocks", true);

	public static final Time STEP_TIMER = new Time();
	private static final Time GROUND_TIMER = new Time();
	private static final Time SLOWDOWN_TIMER = new Time();

	private boolean slowDown = false;

	public Step(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Increases the step height.");
		this.getCheckBoxes().add(ALLOW_GREATERTHAN_2B);
	}

	@SuppressWarnings("deprecation")
	private boolean isSolid(Block block) {
		return block.getMaterial(block.getDefaultState()).isSolid();
	}

	@Override
	public void onUpdate() {

		if (!slowDown) {

			if (ModsUtil.canStep()) {

				if (!mc.player.collidedVertically) {
					GROUND_TIMER.reset();
				}

				if (STEP_TIMER.isDelayComplete(100f)) {

					double y = PlayerUtil.posY();

					if (!isSolid(mc.world.getBlockState(mc.player.getPosition().up(2)).getBlock())) {

						float delay;

						if (!isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.4d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 0.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

							delay = 0;

							if (STEP_TIMER.isDelayComplete(delay)) {

								//1 block step
								PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
								PlayerUtil.setPos(PlayerUtil.posX(), y + 1, PlayerUtil.posZ());

								STEP_TIMER.reset();
							}

						} else {

							if ((mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.4d, PlayerUtil.getHorizontalZ(0.5d))).getBlock() instanceof SlabBlock || (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.4d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && mc.world.getBlockState(new BlockPos(PlayerUtil.posX(), y - 0.1d, PlayerUtil.posZ())).getBlock() instanceof SlabBlock)) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 2, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

								if (GROUND_TIMER.isDelayComplete(150f)) {

									delay = mc.player.isSprinting() ? 75 : 50;

									if (STEP_TIMER.isDelayComplete(delay)) {

										//1.5 block step
										PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
										PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
										PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
										PlayerUtil.setPos(PlayerUtil.posX(), y + 1.5d, PlayerUtil.posZ());

										STEP_TIMER.reset();
									}
								}

							} else {

								if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 2.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

									if (GROUND_TIMER.isDelayComplete(500f)) {

										delay = 1000;

										if (STEP_TIMER.isDelayComplete(delay)) {

											//2 block step
											PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
											PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
											PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
											PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

											PlayerUtil.setPos(PlayerUtil.posX(), y + 2, PlayerUtil.posZ());

											STEP_TIMER.reset();
										}
									}
								} else {

									if (ALLOW_GREATERTHAN_2B.isChecked()) {

										if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 3.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//3 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 3, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										} else if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 4.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//4 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 4, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										} else if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 5.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//5 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 5, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										} else if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 6.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//6 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 6, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										} else if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 7.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//7 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 7, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										} else if (isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 1.9d, PlayerUtil.getHorizontalZ(0.5d))).getBlock()) && !isSolid(mc.world.getBlockState(new BlockPos(PlayerUtil.getHorizontalX(0.5d), y + 8.9, PlayerUtil.getHorizontalZ(0.5d))).getBlock())) {

											if (GROUND_TIMER.isDelayComplete(1000f)) {

												delay = 1000;

												if (STEP_TIMER.isDelayComplete(delay)) {

													//8 block step
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + .41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 1.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 2.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 3.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 4.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 5.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 6.41999998688698d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 7.00133597911214d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));
													PlayerUtil.sendPacket(new PositionRotationPacket(PlayerUtil.posX(), y + 7.16610926093821d, PlayerUtil.posZ(), mc.player.rotationYaw, mc.player.rotationPitch, false));

													PlayerUtil.setPos(PlayerUtil.posX(), y + 8, PlayerUtil.posZ());

													STEP_TIMER.reset();

													ModsUtil.setTimerSpeed(150f);
													slowDown = true;
													SLOWDOWN_TIMER.reset();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else {

			if (SLOWDOWN_TIMER.isDelayComplete(1000f)) {
				ModsUtil.resetTimerSpeed();
				slowDown = false;
			}
		}
	}

}