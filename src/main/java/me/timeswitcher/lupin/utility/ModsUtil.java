package me.timeswitcher.lupin.utility;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

public class ModsUtil {

	public static boolean canFly() {
		return !PlayerUtil.inLiquid() && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.climb() && !Lupin.mc.player.isCrouching() && !PlayerUtil.isRiding();
	}

	public static boolean canSpeed() {
		return PlayerUtil.isMoveKeys() && !PlayerUtil.inLiquid() && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.climb() && !PlayerUtil.isUsingItem() && !Lupin.mc.player.isCrouching() && !PlayerUtil.isRiding();
	}

	public static boolean canLongJump() {
		return !Lupin.instance.getModHandler().getModByName("Fly").isToggled() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && PlayerUtil.playerReady() && !PlayerUtil.climb() && !Lupin.mc.player.isCrouching() && !PlayerUtil.isRiding();
	}

	public static boolean canAura() {
		return PlayerUtil.playerReady() && Lupin.mc.player.getHealth() > 0.0f;
	}

	public static boolean canInvMove() {
		return Lupin.mc.currentScreen != null && !(Lupin.mc.currentScreen instanceof ChatScreen);
	}

	public static boolean canTwerk() {
		return Lupin.mc.player.onGround && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && !PlayerUtil.inBlock() && !PlayerUtil.inWeb() && !PlayerUtil.climb() && !PlayerUtil.isRiding();
	}

	public static boolean canFastSneak() {
		return Lupin.mc.player.isCrouching() && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && !PlayerUtil.inWeb() && !PlayerUtil.climb() && !PlayerUtil.isRiding();
	}

	public static boolean canStep() {
		return Lupin.mc.currentScreen == null && Lupin.mc.player.collidedHorizontally && Lupin.mc.player.collidedVertically && !PlayerUtil.inLiquid() && PlayerUtil.playerReady() && !PlayerUtil.inWeb() && !PlayerUtil.climb() && !PlayerUtil.isRiding();
	}

	public static boolean canVClip() {
		return PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && !PlayerUtil.climb() && !PlayerUtil.isRiding();
	}

	public static boolean canNoFall(float minHeight) {
		return !PlayerUtil.inLiquid() && !PlayerUtil.inBlock() && PlayerUtil.playerReady() && !PlayerUtil.climb() && !PlayerUtil.isRiding() && !PlayerUtil.isCreative()
				&& Lupin.mc.player.fallDistance >= minHeight && !Lupin.mc.player.onGround;
	}

	public static boolean canNoWeb() {
		return PlayerUtil.inWeb() && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && !PlayerUtil.isRiding();
	}

	public static boolean canNoSoulsand() {
		BlockPos pos = new BlockPos(PlayerUtil.posX(), PlayerUtil.posY() - 0.1d, PlayerUtil.posZ());

		return Lupin.mc.world.getBlockState(pos).getBlock() == Blocks.SOUL_SAND && Lupin.mc.player.onGround
				&& !Lupin.mc.player.isCrouching() && PlayerUtil.playerReady() && Lupin.mc.currentScreen == null && !PlayerUtil.inLiquid() && !PlayerUtil.isRiding();
	}

	public static boolean canSprint() {
		return !Lupin.mc.player.isSprinting() && !PlayerUtil.inLiquid() && (Lupin.mc.player.canSwim() ? Lupin.mc.player.movementInput.func_223135_b() : Lupin.mc.player.movementInput.moveForward >= 0.8f) && !Lupin.mc.player.collidedHorizontally && (Lupin.mc.player.getFoodStats().getFoodLevel() > 6.0f || Lupin.mc.player.abilities.allowFlying) && !Lupin.mc.player.isHandActive() && !Lupin.mc.player.isPotionActive(Effects.BLINDNESS);
	}

	public static boolean canNoSlow() {
		return PlayerUtil.isUsingItem();
	}
	
	public static boolean canCheatCrit() {
		return Lupin.mc.player.onGround && !PlayerUtil.inLiquid();
	}
	
	public static void setTimerSpeed(float speed) {

		if (Lupin.instance.getAccessManager().isSRG()) {

			Lupin.instance.getAccessManager().applyTimerSpeed(speed);

		} else {

			Lupin.instance.getAccessManager().applyTimerSpeedSRG(speed);
		}
	}

	public static void resetTimerSpeed() {

		float speed = 50.0F;

		if (Lupin.instance.getAccessManager().isSRG()) {

			Lupin.instance.getAccessManager().applyTimerSpeed(speed);

		} else {

			Lupin.instance.getAccessManager().applyTimerSpeedSRG(speed);
		}
	}

}