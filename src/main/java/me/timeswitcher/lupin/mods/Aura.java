package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.EntityUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.RotationUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.entity.LivingEntity;

public class Aura extends Mod {

	private final Slider reach = new Slider(
			"Reach",
			50.0f,
			0.0f,
			100.0f,
			6.0f,
			false
	);

	private final Slider aps = new Slider(
			"APS",
			50.0f,
			0.0f,
			100.0f,
			20.0f,
			true
	);

	private final Slider targetChangeDelay = new Slider(
			"Target change delay",
			10.0f,
			0.0f,
			100.0f,
			500.0f,
			true
	);

	private final CheckBox cooldown = new CheckBox(
			"1.9+ Cooldown",
			true
	);

	private final CheckBox noSwing = new CheckBox(
			"No Swing",
			false
	);

	private final CheckBox critical = new CheckBox(
			"Critical",
			true
	);

	private final CheckBox inGui = new CheckBox(
			"In Gui",
			false
	);

	public static LivingEntity target = null;

	private final Time attackTimer = new Time();
	private final Time targetChangeTimer = new Time();

	public Aura(String name) {
		super(
				name,
				Category.COMBAT,
				GLFW.GLFW_KEY_R,
				"Automatically attacks entities around you."
		);

		getSliders().add(reach);
		getSliders().add(aps);
		getSliders().add(targetChangeDelay);

		getCheckBoxes().add(cooldown);
		getCheckBoxes().add(noSwing);
		getCheckBoxes().add(critical);
		getCheckBoxes().add(inGui);
	}

	@Override
	public void onEnable() {
		target = null;

		attackTimer.reset();
		targetChangeTimer.reset();
	}

	@Override
	public void onDisable() {
		target = null;

		attackTimer.reset();
		targetChangeTimer.reset();
	}

	@Override
	public void onUpdate() {

		if (!ModsUtil.canAura()) {
			return;
		}

		if (!inGui.isChecked() && mc.currentScreen != null) {
			return;
		}

		if (target == null || !EntityUtil.isEntityValid(target)) {
			findTarget();
		}

		if (target == null) {
			return;
		}

		if (!EntityUtil.isEntityValid(target)) {
			target = null;
			return;
		}

		if (mc.player.getDistance(target) > reach.getReturnValue()) {
			target = null;
			return;
		}

		faceTarget();

		if (!isFacingTarget()) {
			return;
		}

		if (!canAttack()) {
			return;
		}

		attack();
	}

	private void findTarget() {

		if (!targetChangeTimer.isDelayComplete(
				targetChangeDelay.getReturnValue())) {
			return;
		}

		target = EntityUtil.getEntityInRange(reach.getReturnValue());

		targetChangeTimer.reset();
	}

	private void faceTarget() {

		float[] rotations = RotationUtil.getRotation(target);
		float yawDifference = rotations[0] - mc.player.rotationYaw;

		if (yawDifference < -10.0F || yawDifference > 10.0F) {
			RotationUtil.faceEntity(target);
		}
	}

	private boolean isFacingTarget() {

		float[] rotations = RotationUtil.getRotation(target);
		float yawDifference = rotations[0] - mc.player.rotationYaw;

		return yawDifference >= -10.0F && yawDifference <= 10.0F;
	}

	private boolean canAttack() {

		if (cooldown.isChecked()) {
			return mc.player.getCooledAttackStrength(0.0F) >= 1.0F;
		}

		float attacksPerSecond = aps.getReturnValue();

		if (attacksPerSecond <= 0.0F) {
			return false;
		}

		float delay = 1000.0F / attacksPerSecond + 20.0F;

		return attackTimer.isDelayComplete(delay);
	}

	private void attack() {

		if (target == null) {
			return;
		}

		if (!EntityUtil.isEntityValid(target)) {
			target = null;
			return;
		}

		if (mc.player.getDistance(target) > reach.getReturnValue()) {
			target = null;
			return;
		}

		if (critical.isChecked() && ModsUtil.canCheatCrit()) {
			PlayerUtil.crit();
		}

		PlayerUtil.hit(target, noSwing.isChecked());

		attackTimer.reset();
	}
}