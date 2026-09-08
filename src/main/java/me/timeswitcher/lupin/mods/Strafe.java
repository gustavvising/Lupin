package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.PlayerUtil;

public class Strafe extends Mod {

    public Strafe(String name) {
        super(name, Category.MOVE, GLFW.GLFW_KEY_UNKNOWN, "Move around freely in air.");
    }

    @Override
    public void onUpdate() {

        if (PlayerUtil.isMoveKeys() && !mc.player.onGround) {

            double yaw = 0;
            double speed = 0;

            if (!(mc.player.isSprinting() && mc.gameSettings.keyBindForward.isKeyDown())) {
                yaw = getDirection();
                speed = R.nextDouble() > 0.8d ? 0.3d : 0.27d;
            } else {
                yaw = getDirection();
                speed = R.nextDouble() > 0.8d ? 0.3d : 0.25d;
            }
            PlayerUtil.setMotion(-Math.sin(yaw) * speed, PlayerUtil.motionY(), Math.cos(yaw) * speed);
        }
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.player.moveForward < 0F)
            forward = -0.5F;
        else if (mc.player.moveForward > 0F)
            forward = 0.5F;

        if (mc.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
}