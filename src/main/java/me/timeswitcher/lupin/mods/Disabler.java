package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CKeepAlivePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CSpectatePacket;

public class Disabler extends Mod {

	private final Time PACKET_TIMER = new Time();
	
	public Disabler(String name) {
		super(name, Category.EXTRA, GLFW.GLFW_KEY_UNKNOWN, "Disables spartan anticheat.");
	}

	@Override
	public void onUpdate() {
		if (PACKET_TIMER.isDelayComplete(50)) {
			PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX(), -0, PlayerUtil.posZ(), R.nextBoolean()));
			PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX() + 5, PlayerUtil.posY() + 6d, PlayerUtil.posZ() + 5, R.nextBoolean()));
			PlayerUtil.setPos(PlayerUtil.posX(), PlayerUtil.posY() + 1, PlayerUtil.posZ());
			PACKET_TIMER.reset();
		}
	}
}