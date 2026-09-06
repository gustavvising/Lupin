package me.timeswitcher.lupin.utility;

import java.util.Random;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerPacket.RotationPacket;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {

	public static float packetYaw = 1337;
	public static float packetPitch = 1337;
	
	private final static Random R = new Random();

	public synchronized static void faceEntity(Entity entity) {
		float[] rotations = getRotation(entity);
        if (packetYaw == 1337) {
            packetYaw = Lupin.mc.player.rotationYaw;
        }
        if (packetPitch == 1337) {
            packetPitch = Lupin.mc.player.rotationPitch;
        }
        float rotationSpeed = calcAngleDistance(entity);
        packetYaw = limitAngleChange(packetYaw, rotations[0], rotationSpeed / 200);
        packetPitch = rotations[1];

        Lupin.mc.player.rotationYaw = packetYaw;
        Lupin.mc.player.rotationYawHead = packetYaw;
        Lupin.mc.player.rotationPitch = packetPitch;

        PlayerUtil.sendPacket(new RotationPacket(packetYaw, packetPitch, Lupin.mc.player.onGround));
    }

	private final static float limitAngleChange(final float current, final float intended, final float maxChange)
	{
		float change = intended - current;

		if(change > maxChange)
		{
			change = maxChange;
		} else if(change < -maxChange)
		{
			change = -maxChange;
		}
		return current + change;
	}

	public static float[] getRotationsNeeded(Entity entity)
	{
		if (entity == null)
			return null;

		double diffX = PlayerUtil.posX(entity) - PlayerUtil.posX();
		double diffY = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / (1.75D + R.nextFloat()) - (PlayerUtil.posY() + Lupin.mc.player.getEyeHeight());;
		double diffZ = PlayerUtil.posZ(entity) - PlayerUtil.posZ();

		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		return new float[] {Lupin.mc.player.rotationYaw + MathHelper.wrapDegrees((yaw - Lupin.mc.player.rotationYaw)), Lupin.mc.player.rotationPitch + MathHelper.wrapDegrees((pitch - Lupin.mc.player.rotationPitch))};
	}
	
	public static boolean packetLookingAt(Entity entity) {
		float distance = calcAngleDistance(entity);
		System.out.println(distance);
		return !(distance > 20) && !(distance < -20);
	}
	
	private static float calcAngleDistance(Entity entity) {
		return entity.rotationYaw - packetYaw;
	}

	public static void lookAtEntity(Entity e) {
		float yaw = getRotation(e)[0];
		float pitch = getRotation(e)[1];
		PlayerUtil.sendPacket(new RotationPacket(yaw, pitch, Lupin.mc.player.onGround));
	}

	private static float[] getRotation(Entity e) {
		double x = PlayerUtil.posX(e) - PlayerUtil.posX();
		double y = PlayerUtil.posX(e) - PlayerUtil.posY();
		double z = PlayerUtil.posZ(e) - PlayerUtil.posZ();

		double len = Math.sqrt(x * x + y * y + z * z);

		y /= len;

		float f = (float)(Math.atan2(z, x) * 180.0D / Math.PI) + 90.0F;
		float f1 = (float)-(Math.atan2(y, len) * 180.0D / Math.PI);

		return new float[] {Lupin.mc.player.rotationYaw + MathHelper.wrapDegrees((f - Lupin.mc.player.rotationYaw)), Lupin.mc.player.rotationPitch + MathHelper.wrapDegrees((f1 - Lupin.mc.player.rotationPitch))};
	}

}