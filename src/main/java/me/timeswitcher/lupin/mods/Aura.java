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

	private final Slider REACH = new Slider("Reach", 50.0f, 0.0f, 100.0f, 6.0f, false);
	private final Slider APS = new Slider("APS", 50.0f, 0.0f, 100.0f, 20.0f, true);
	private final Slider TARGETCHANGE_DELAY = new Slider("Targetchange delay", 10.0f, 0.0f, 100.0f, 500.0f, true);

	private final CheckBox COOLDOWN = new CheckBox("1.9+ Cooldown", true);
	private final CheckBox NO_SWING = new CheckBox("No Swing", false);
	private final CheckBox CRITICAL = new CheckBox("Critical", true);
	private final CheckBox IN_GUI = new CheckBox("In Gui", false);

	public static LivingEntity target = null;

	private final Time ATTACK_TIMER = new Time();
	private final Time ROT_TIMER = new Time();
	private final Time TARGETCHANGE_TIMER = new Time();

	public static int noSwings;

	public Aura(String name) {
		super(name, Category.COMBAT, GLFW.GLFW_KEY_R, "Automatically attacks entities around you.");
		this.getSliders().add(REACH);
		this.getSliders().add(APS);
		this.getSliders().add(TARGETCHANGE_DELAY);
		this.getCheckBoxes().add(COOLDOWN);
		this.getCheckBoxes().add(NO_SWING);
		this.getCheckBoxes().add(CRITICAL);
		this.getCheckBoxes().add(IN_GUI);
	}

	@Override
	public void onDisable() {
		target = null;
	}

	@Override
	public void onUpdate() {

		if (ModsUtil.canAura()) {

			if (IN_GUI.isChecked() || mc.currentScreen == null) {

				try  {

					if (target == null) {

						if (TARGETCHANGE_TIMER.isDelayComplete(TARGETCHANGE_DELAY.getReturnValue())) {

							target = EntityUtil.getEntityInRange(REACH.getReturnValue());

							TARGETCHANGE_TIMER.reset();
						}
					}
					if (target != null) {

						if (EntityUtil.isEntityValid(target)) {

							if (ROT_TIMER.isDelayComplete(50.0f)) {
								RotationUtil.faceEntity(target);
								ROT_TIMER.reset();
							}

							if (COOLDOWN.isChecked() ? mc.player.getCooledAttackStrength(0.0F) == 1.0F : ATTACK_TIMER.isDelayComplete(1000 / APS.getReturnValue())) {

								if (mc.player.getDistance(target) <= REACH.getReturnValue()) {

									if (EntityUtil.isEntityValid(target) && target != null) {

										if (RotationUtil.packetLookingAt(target)) {

											if (CRITICAL.isChecked()) {

												if (ModsUtil.canCheatCrit()) {
													PlayerUtil.crit();
												}
											}
											PlayerUtil.hit(target, NO_SWING.isChecked());
											ATTACK_TIMER.reset();
										}

									} else {

										target = null;
									}
								} else {

									target = null;
								}
							}
						} else {

							target = null;
						}
					}
				} catch (Exception ignored) {

				}
			}
		}
	}

}