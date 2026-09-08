package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Blocks;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Fly extends Mod {

	private final Mode packet = new Mode("Packet");
	private final Mode airJump = new Mode("Air Jump");

	private BlockPos blockPos;
	private final Time flyTimer = new Time();

	public Fly(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_G, "Makes you fly.");
		this.setCurrentMode(packet);
		this.getModes().add(packet);
		this.getModes().add(airJump);
	}

	private void packetFly() {

		ModsUtil.setTimerSpeed(500);

		if (flyTimer.isDelayComplete(50)) {

			PlayerUtil.setMotion(0, 0, 0);

			sendPacketFlyFast(PlayerUtil.motionX(), 0.00000001d, PlayerUtil.motionZ());

			flyTimer.reset();
		}
	}

	private void sendPacketFlyFast(double x, double y, double z) {
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX() + x, PlayerUtil.posY() + y, PlayerUtil.posZ() + z, true));
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX() + x, PlayerUtil.posY() + 112, PlayerUtil.posZ() + z, true));
	}

	private void airJump() {

		if (blockPos != null) {

			if (World.isValid(blockPos) && mc.world.getBlockState(blockPos).getBlock() ==
					Blocks.BARRIER) {

				mc.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);

				blockPos = null; } } if (ModsUtil.canFly() && blockPos == null) {

			BlockPos blockPos = new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() - 1,
					PlayerUtil.posZ());

			if (PlayerUtil.isMoveKeys() && mc.gameSettings.keyBindJump.isKeyDown() &&
					mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {

				mc.world.setBlockState(blockPos, Blocks.BARRIER.getDefaultState(), 2);

				this.blockPos = blockPos; } }

	}

	@Override
	public void onEnable() {
		blockPos = null;
	}

	@Override
	public void onDisable() {
		if (!mc.player.onGround) {
			PlayerUtil.setMotion(PlayerUtil.motionX(), -0.07544406518948656d, PlayerUtil.motionZ());
		}
		ModsUtil.resetTimerSpeed();
	}

	@Override
	public void onUpdate() {

		if (this.getCurrentMode().equals(packet)) {

			packetFly();

		} else if (this.getCurrentMode().equals(airJump)) {

			airJump();
		}
		}
	}