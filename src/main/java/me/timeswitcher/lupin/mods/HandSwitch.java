package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;

public class HandSwitch extends Mod {

	private final Time handswitchTimer = new Time();

	private Hand orginalHand;
	private HandSide orginalHandSide;

	public HandSwitch(String name) {
		super(name, Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN, "Swaps left and right hand.");
	}

	@Override
	public void onEnable() {
		setOrginalHand(mc.player.getActiveHand());
		setOrginalHandSide(mc.player.getPrimaryHand());
	}

	@Override
	public void onDisable() {

		if (!GameUtil.isGameNull()) {

			try {

				mc.player.setPrimaryHand(getOrginalHandSide());

				if (getOrginalHand() != mc.player.getActiveHand()) {

					PlayerUtil.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.SWAP_HELD_ITEMS, BlockPos.ZERO, Direction.DOWN));
				}
			} catch (Exception ignored) {

			}
		}
	}

	@Override
	public void onUpdate() {

		if (PlayerUtil.playerReady()) {

			try {

				if (handswitchTimer.isDelayComplete(20.0F)) {

					HandSide oppositeHandSide;

					if (mc.player.getPrimaryHand() == HandSide.LEFT) {

						oppositeHandSide = HandSide.RIGHT;

					} else {

						oppositeHandSide = HandSide.LEFT;
					}
					mc.player.setPrimaryHand(oppositeHandSide);
					PlayerUtil.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.SWAP_HELD_ITEMS, BlockPos.ZERO, Direction.DOWN));

					handswitchTimer.reset();
				}
			} catch (Exception ignored) {

			}
		}
	}

	private Hand getOrginalHand() {
		return orginalHand;
	}

	private void setOrginalHand(Hand orginalHand) {
		this.orginalHand = orginalHand;
	}

	private HandSide getOrginalHandSide() {
		return orginalHandSide;
	}

	private void setOrginalHandSide(HandSide orginalHandSide) {
		this.orginalHandSide = orginalHandSide;
	}

}