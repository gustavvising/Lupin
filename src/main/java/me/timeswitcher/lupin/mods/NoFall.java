package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerPacket.PositionRotationPacket;

public class NoFall extends Mod {

    private final Time NOFALL_TIMER = new Time();

    public NoFall(String name) {
        super(name, Category.PLAYER, GLFW.GLFW_KEY_N, "Prevents fall damage.");
    }

    @Override
    public void onUpdate() {

        if (mc.player.getHealth() < mc.player.getMaxHealth()) {

            PlayerUtil.setMotion(0, 0, 0);
            ModsUtil.setTimerSpeed(100);

            if (NOFALL_TIMER.isDelayComplete(100)) {
                for (int i = 0; i < 15; i++) {
                    PlayerUtil.sendPacket(new CPlayerPacket(true));
                }
                NOFALL_TIMER.reset();
            }
        }
    }

}