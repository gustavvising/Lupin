package me.timeswitcher.lupin.utility;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPlayerPacket.RotationPacket;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {

	private static final float maxYawChange = 15.0F;

	public static float packetYaw = 0.0F;
	public static float packetPitch = 0.0F;

	private static boolean initialized = false;

	public synchronized static void faceEntity(Entity entity) {
		if (entity == null || Lupin.mc.player == null) {
			return;
		}

		if (!initialized) {
			packetYaw = Lupin.mc.player.rotationYaw;
			packetPitch = Lupin.mc.player.rotationPitch;
			initialized = true;
		}

		float[] rotations = getRotation(entity);

		packetYaw = limitAngleChange(packetYaw, rotations[0]);
		packetPitch = MathHelper.clamp(rotations[1], -90.0F, 90.0F);

		Lupin.mc.player.rotationYaw = packetYaw;
		Lupin.mc.player.rotationYawHead = packetYaw;
		Lupin.mc.player.rotationPitch = packetPitch;

		PlayerUtil.sendPacket(
				new RotationPacket(
						packetYaw,
						packetPitch,
						Lupin.mc.player.onGround
				)
		);
	}

	private static float limitAngleChange(float current, float intended) {
		float change = MathHelper.wrapDegrees(intended - current);
		change = MathHelper.clamp(change, -RotationUtil.maxYawChange, RotationUtil.maxYawChange);
		return current + change;
	}

	public static float[] getRotation(Entity entity) {
		if (entity == null || Lupin.mc.player == null) {
			return new float[] {
					packetYaw,
					packetPitch
			};
		}

		double x = PlayerUtil.posX(entity) - PlayerUtil.posX();
		double y = ((entity.getBoundingBox().minY + entity.getBoundingBox().maxY) * 0.5D)
				- (PlayerUtil.posY() + Lupin.mc.player.getEyeHeight());
		double z = PlayerUtil.posZ(entity) - PlayerUtil.posZ();

		double horizontalDistance = Math.sqrt(x * x + z * z);

		if (horizontalDistance < 1.0E-4D) {
			horizontalDistance = 1.0E-4D;
		}

		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(y, horizontalDistance) * 180.0D / Math.PI);

		yaw = Lupin.mc.player.rotationYaw
				+ MathHelper.wrapDegrees(yaw - Lupin.mc.player.rotationYaw);

		pitch = Lupin.mc.player.rotationPitch
				+ MathHelper.wrapDegrees(pitch - Lupin.mc.player.rotationPitch);

		pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

		return new float[] {
				yaw,
				pitch
		};
	}

	public static void reset() {
		packetYaw = 0.0F;
		packetPitch = 0.0F;
		initialized = false;
	}
}