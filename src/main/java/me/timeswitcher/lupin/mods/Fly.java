package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.MathUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ElytraItem;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerPacket.PositionRotationPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Fly extends Mod {

	private final Mode PACKET = new Mode("Packet");
	private final Mode AIRJUMP = new Mode("OldAirJump");

	private final Slider TIMER_SPEED = new Slider("Timer SPEED", 30.0f, 1.0f, 100.0f, 100.0f, false);
	private final Slider TIMER_START = new Slider("Timer Start Delay", 20.0f, 1.0f, 100.0f, 500.0f, true);
	private final Slider TIMER_STOP = new Slider("Timer Stop Delay", 30.0f, 1.0f, 100.0f, 1000.0f, true);
	private final Slider TIMER_RESET = new Slider("Timer Reset Delay", 40.0f, 1000.0f, 100.0f, 5000.0f, true);

	private BlockPos blockPos;
	private final Time FLY_TIMER = new Time();
	private final Time SLOWDOWN_TIMER = new Time();

	private boolean slowDown = false;

	public Fly(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_G, "Makes you fly.");
		this.setCurrentMode(PACKET);
		this.getModes().add(PACKET);
		this.getModes().add(AIRJUMP);
		this.getSliders().add(TIMER_SPEED);
		this.getSliders().add(TIMER_START);
		this.getSliders().add(TIMER_STOP);
		this.getSliders().add(TIMER_RESET);
	}

	private void packet() {

		mc.player.noClip = true;
		
		PlayerUtil.setMotion(PlayerUtil.motionX(), 0, PlayerUtil.motionZ());
		
		mc.player.movementInput.sneaking = false;
		
		//mc.player.onGround = true;
		
		if (SLOWDOWN_TIMER.isDelayComplete(500f)) {
			slowDown = true;
		}
		if (SLOWDOWN_TIMER.isDelayComplete(1100f)) {
			slowDown = false;
			SLOWDOWN_TIMER.reset();
		}

		if (FLY_TIMER.isDelayComplete(50)) {
			
			final float direction = mc.player.rotationYaw + ((mc.player.moveForward < 0.0f) ? 180 : 0) + ((mc.player.moveStrafing > 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f) - ((mc.player.moveStrafing < 0.0f) ? (-90.0f * ((mc.player.moveForward < 0.0f) ? -0.5f : ((mc.player.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
			final double xDir = Math.cos((direction + 90.0f) * Math.PI / 180.0);
			final double zDir = Math.sin((direction + 90.0f) * Math.PI / 180.0);

			double horizontalSpeed = PlayerUtil.isMoveKeys() ? 0.1d : 0;
			double verticalSpeed = 0;

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				verticalSpeed = !PlayerUtil.isMoveKeys() ? 0.1d : 0;
			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				verticalSpeed = !PlayerUtil.isMoveKeys() ? -0.1d : 0;
			}
			if (!slowDown) {
				sendPacketFly(xDir * horizontalSpeed, verticalSpeed, zDir * horizontalSpeed);
			} else {
				sendPacketFly(0, 0, 0);
			}
			FLY_TIMER.reset();
		}
	}

	private void sendPacketFly(double x, double y, double z) {
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX() + x, PlayerUtil.posY(), PlayerUtil.posZ() + z, true));
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX() + x, PlayerUtil.posY() + y, PlayerUtil.posZ() + z, true));
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX(), -0, PlayerUtil.posZ(), true));
		//PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.posX(), 256, PlayerUtil.posZ(), true));
		//PlayerUtil.setPos(PlayerUtil.posX() + x, PlayerUtil.posY() + y, PlayerUtil.posZ() + z);
	}

	private void old() {
		if (blockPos != null) {

			if (World.isValid(blockPos) && mc.world.getBlockState(blockPos).getBlock() == Blocks.BARRIER) {

				mc.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);

				blockPos = null; 
			}
		} 
		if (ModsUtil.canFly() && blockPos == null) {

			BlockPos blockPos = mc.player.getPosition().down();

			if (PlayerUtil.isMoveKeys() && mc.gameSettings.keyBindJump.isKeyDown() && mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {

				mc.world.setBlockState(blockPos, Blocks.BARRIER.getDefaultState(), 2);

				this.blockPos = blockPos; 
			} 
		}
	}

	@Override
	public void onEnable() {
		blockPos = null;
	}

	@Override
	public void onDisable() {
		ModsUtil.resetTimerSpeed();
	}

	@Override
	public void onUpdate() {

		if (this.getCurrentMode().equals(PACKET)) {

			mc.player.noClip = true;

			packet();

		} else if (this.getCurrentMode().equals(AIRJUMP)) {

			old();
		}
	}

	//up
	//Y: 66.41999998688698
	//MotionY: 0.33319999363422365
	//Y: 66.7531999805212
	//MotionY: 0.24813599859094576
	//Y: 67.00133597911214
	//MotionY: 0.16477328182606651
	//Y: 67.16610926093821
	//MotionY: 0.08307781780646721
	//Y: 67.24918707874468
	//MotionY: 0.0030162615090425808
	//Y: 67.25220334025373

	//down
	//MotionY: -0.07544406518948656
	//Y: 67.17675927506424
	//MotionY: -0.15233518685055708
	//FallDist: 0.075444065
	//Y: 67.02442408821369
	//MotionY: -0.22768848754498797
	//FallDist: 0.22777925
	//Y: 66.79673560066871
	//MotionY: -0.30153472366278034
	//FallDist: 0.45546773
	//Y: 66.49520087700593
	//MotionY: -0.3739040364667221
	//FallDist: 0.7570025
	//Y: 66.1212968405392
	//MotionY: -0.4448259643949201
	//FallDist: 1.1309065
}