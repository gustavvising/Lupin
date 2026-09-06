package me.timeswitcher.lupin.utility;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.network.play.client.CPlayerPacket.PositionPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PlayerUtil {

	public static boolean climb() {
		BlockPos bp = new BlockPos(posX(), posY(), posZ());

		return (Lupin.mc.world.getBlockState(bp).getBlock() == Blocks.VINE
				|| Lupin.mc.world.getBlockState(bp).getBlock() == Blocks.LADDER || Lupin.mc.player.isOnLadder()) && Lupin.mc.currentScreen == null && !Lupin.mc.player.isCrouching();
	}

	public static boolean isCreative() {
		return Lupin.mc.player.isCreative();
	}

	public static boolean isRiding() {
		return Lupin.mc.player.getRidingEntity() != null ? true : false;
	}

	public static boolean inLiquid() {
		BlockPos pos = new BlockPos(posX(), posY() - 0.1d, posZ());

		return Lupin.mc.player.isInWater() || Lupin.mc.player.isInLava() || Lupin.mc.world.getBlockState(pos).getBlock() == Blocks.WATER
				|| Lupin.mc.world.getBlockState(pos).getBlock() == Blocks.LAVA;
	}

	public static boolean inBlock() {
		return Lupin.mc.player.isEntityInsideOpaqueBlock();
	}

	public static boolean inWeb() {
		BlockPos blockPos = Lupin.mc.player.getPosition();
		return Lupin.mc.world.getBlockState(blockPos).getBlock() == Blocks.COBWEB || Lupin.mc.world.getBlockState(blockPos.up()).getBlock() == Blocks.COBWEB;
	}

	public static boolean isFlying() {
		return Lupin.mc.player.isElytraFlying() || Lupin.mc.player.abilities.isFlying;
	}

	public static boolean playerReady() {
		return !Lupin.mc.player.isSleeping();
	}

	public static int heigtOverGround() {
		int height = 0;

		while (Lupin.mc.player.getPosition().getY() - height > 0) {

			if (Lupin.mc.world.isAirBlock(new BlockPos(posX(), posY() - height, posZ()))) {

				height += 1;

			} else {

				return height;
			}
		}
		return 0;
	}

	public static boolean isUsingItem() {
		boolean using = false;

		if (Lupin.mc.player.isHandActive() && !Lupin.mc.player.getActiveItemStack().isEmpty()) {

			Item item = Lupin.mc.player.getActiveItemStack().getItem();

			if (item.getUseAction(Lupin.mc.player.getActiveItemStack()) != UseAction.NONE) {

				using = true;

			} else {

				using = false;
			}
		}
		return using;
	}

	public static boolean isBlocking() {
		if (Lupin.mc.player.isHandActive() && !Lupin.mc.player.getActiveItemStack().isEmpty()) {
			Item item = Lupin.mc.player.getActiveItemStack().getItem();
			if (item.getUseAction(Lupin.mc.player.getActiveItemStack()) != UseAction.BLOCK) {
				return false;
			} else {
				return item.getUseDuration(Lupin.mc.player.getActiveItemStack())
						- Lupin.mc.player.getActiveItemStack().getCount() >= 1;
			}
		} else {
			return false;
		}
	}

	public static boolean touchesGround(double heightUnderPlayer) {
		BlockPos pos = new BlockPos(posX(), posY() - heightUnderPlayer, posZ());

		return Lupin.mc.world.getBlockState(pos).getBlock() != Blocks.AIR;
	}

	public static void crit() {
		sendPacket(new PositionPacket(posX(), posY() + .1212968405392, posZ(), false));
		sendPacket(new PositionPacket(posX(), posY(), posZ(), false));
	}

	public static void critFromPos(double x, double y, double z) {
		sendPacket(new PositionPacket(x, y + .1212968405392, z, false));
		sendPacket(new PositionPacket(x, y, z, false));
	}

	public static void hit(Entity target, boolean noSwing) {
		sendPacket(new CUseEntityPacket(target));
		Lupin.mc.player.resetCooldown();
		if (!noSwing) {
			Lupin.mc.player.swingArm(Hand.MAIN_HAND);
		}
		RotationUtil.packetYaw = 1337;
		RotationUtil.packetPitch = 1337;
	}

	public static void sendPacket(IPacket<?> packetIn) {
		Lupin.mc.player.connection.sendPacket(packetIn);
	}

	public static boolean isMovementInputForward(float forward) {
		return Lupin.mc.player.movementInput.moveForward >= forward;
	}

	public static boolean isMovementInputStrafe(float strafe) {
		return Lupin.mc.player.movementInput.moveStrafe >= strafe;
	}

	public static boolean isMoveKeys() {
		return Lupin.mc.gameSettings.keyBindForward.isKeyDown() || Lupin.mc.gameSettings.keyBindBack.isKeyDown()
				|| Lupin.mc.gameSettings.keyBindLeft.isKeyDown() || Lupin.mc.gameSettings.keyBindRight.isKeyDown();
	}

	public static boolean isMovementInputMoving() {
		return Lupin.mc.player.moveForward != 0.0f || Lupin.mc.player.moveStrafing != 0.0f;
	}

	public static double posX() {
		return Lupin.mc.player.getPosX();
	}

	public static double posY() {
		return Lupin.mc.player.getPosY();
	}

	public static double posZ() {
		return Lupin.mc.player.getPosZ();
	}

	public static double posX(Entity e) {
		return e.getPosX();
	}

	public static double posY(Entity e) {
		return e.getPosY();
	}

	public static double posZ(Entity e) {
		return e.getPosZ();
	}

	public static void setPos(double x, double y, double z) {
		Lupin.mc.player.setPosition(x, y, z);
	}

	public static double getHorizontalX(double value) {
		return posX() + -(Math.sin(Math.toRadians(Lupin.mc.player.getRotationYawHead())) * value);
	}

	public static double getHorizontalZ(double value) {
		return posZ() + (Math.cos(Math.toRadians(Lupin.mc.player.getRotationYawHead())) * value);
	}

	public static double getHorizontalX(double x, double value) {
		return x + -(Math.sin(Math.toRadians(Lupin.mc.player.getRotationYawHead())) * value);
	}

	public static double getHorizontalZ(double z, double value) {
		return z + (Math.cos(Math.toRadians(Lupin.mc.player.getRotationYawHead())) * value);
	}

	public static double motionX() {
		return Lupin.mc.player.getMotion().getX();
	}

	public static double motionY() {
		return Lupin.mc.player.getMotion().getY();
	}

	public static double motionZ() {
		return Lupin.mc.player.getMotion().getZ();
	}

	public static void setMotion(double motionX, double motionY, double motionZ) {
		Lupin.mc.player.setMotion(motionX, motionY, motionZ);
	}

	public static void setMotionXZ(double motionX, double motionZ) {
		Lupin.mc.player.setMotion(motionX, motionY(), motionZ);
	}

	public static void addMotion(double motionX, double motionY, double motionZ) {
		Lupin.mc.player.setMotion(motionX() + motionX, motionY() + motionY, motionZ() + motionZ);
	}

	public static void addMotionY(double motionY) {
		Lupin.mc.player.setMotion(motionX(), motionY() + motionY, motionZ());
	}

	public static void addMotionXZ(double motionX, double motionZ) {
		Lupin.mc.player.setMotion(motionX() + motionX, motionY(), motionZ() + motionZ);
	}

	public static double getMoveSpeed() {
		final double distTraveledLastTickX = posX() - Lupin.mc.player.prevPosX;
		final double distTraveledLastTickZ = posZ() - Lupin.mc.player.prevPosZ;
		return MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
	}

	public static String getMoveSpeedString() {
		return String.format("\u00A7fSpeed: \u00A77%.2f \u00A7fm/sec", getMoveSpeed() / 0.05F);
	}

	public static void setMoveSpeed(final double d) {

		if (isMovementInputMoving()) {

			setMotion(-(MathHelper.sin(getDir()) * d), motionY(), MathHelper.cos(getDir()) * d);

		} else {

			setMotion(0.0d, motionY(), 0.0d);
		}
	}

	public static float getDir() {
		double forward = Lupin.mc.player.moveForward;
		final double strafe = Lupin.mc.player.moveStrafing;
		double yaw = Lupin.mc.player.rotationYaw;
		final double strafeYaw = 45.0;
		if (strafe != 0.0 && forward == 0.0) {
			yaw += 360.0;
			if (strafe > 0.0) {
				yaw -= 90.0;
			} else if (strafe < 0.0) {
				yaw += 90.0;
			}
			forward = 0.0;
		} else if (forward > 0.0) {
			if (strafe > 0.0) {
				yaw -= strafeYaw;
			}
			if (strafe < 0.0) {
				yaw += strafeYaw;
			}
		} else {
			if (strafe > 0.0) {
				yaw += strafeYaw;
			}
			if (strafe < 0.0) {
				yaw -= strafeYaw;
			}
		}
		if (forward < 0.0) {
			yaw -= 180.0D;
		}
		yaw *= 0.0174653292;
		return (float) yaw;
	}

}