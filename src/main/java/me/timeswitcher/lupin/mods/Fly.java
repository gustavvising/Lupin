package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.PlayerUtil;
import net.minecraft.network.play.client.CPlayerPacket;

public class Fly extends Mod {

    private final double speed = 0.5D;

    public Fly(String name) {
        super(name, Category.MOVE, GLFW.GLFW_KEY_G, "Allows the player to fly.");
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }

        mc.player.setMotion(0, 0, 0);
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }

        mc.player.setMotion(
                mc.player.getMotion().x,
                0,
                mc.player.getMotion().z
        );
    }

    @Override
public void onUpdate() {
    if (mc.player == null) {
        return;
    }

    double forward = 0.0D;
    double strafe = 0.0D;

    if (mc.gameSettings.keyBindForward.isKeyDown()) {
        forward++;
    }

    if (mc.gameSettings.keyBindBack.isKeyDown()) {
        forward--;
    }

    if (mc.gameSettings.keyBindLeft.isKeyDown()) {
        strafe++;
    }

    if (mc.gameSettings.keyBindRight.isKeyDown()) {
        strafe--;
    }

    double motionX = 0.0D;
    double motionZ = 0.0D;

    if (forward != 0.0D || strafe != 0.0D) {
        double length = Math.sqrt(
                forward * forward + strafe * strafe
        );

        forward /= length;
        strafe /= length;

        double yaw = Math.toRadians(mc.player.rotationYaw);

        motionX = (
                -Math.sin(yaw) * forward
                + Math.cos(yaw) * strafe
        ) * speed;

        motionZ = (
                Math.cos(yaw) * forward
                + Math.sin(yaw) * strafe
        ) * speed;
    }

    double motionY = 0.0D;

    if (mc.gameSettings.keyBindJump.isKeyDown()) {
        motionY = speed;
    } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
        motionY = -speed;
    }

    mc.player.setMotion(motionX, motionY, motionZ);

    sendPositionPacket();
}


    private void sendPositionPacket() {
        PlayerUtil.sendPacket(
                new CPlayerPacket.PositionPacket(
                        mc.player.getPosX(),
                        mc.player.getPosY(),
                        mc.player.getPosZ(),
                        false
                )
        );
    }
}
