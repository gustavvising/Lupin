package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.EntityUtil;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.BlockPos;

public class TPAura extends Mod {

	private final Slider tpReach = new Slider("TP Reach", 30.0f, 0.0f, 100.0f, 100.0f, false);
	private final Slider hitReach = new Slider("Hit Reach", 50.0f, 0.0f, 100.0f, 6.0f, false);
	private final Slider targetChangeDelay = new Slider("Target change delay", 100.0f, 0.0f, 100.0f, 500.0f, true);
	private final Slider tpDist = new Slider("TP Dist", 50.0f, 0.0f, 100.0f, 5.0f, false);
	private final Slider tpDelay = new Slider("TP Delay", 10.0f, 0.0f, 100.0f, 50.0f, true);
	
	private final CheckBox noSwing = new CheckBox("No Swing", false);
	private final CheckBox critical = new CheckBox("Critical", false);
	private final CheckBox inGui = new CheckBox("In Gui", false);
	
	private final Time targetChangeTimer = new Time();
	private final Time tpTimer = new Time();

	private BlockPos startPos = null;
	public static BlockPos lastTPPos = null;
	private boolean startedTP = false;
	private boolean back = false;

	private double px = 0;
	private double py = 0;
	private double pz = 0;

	public static LivingEntity target = null;

	public TPAura(String name) {
		super(name, Category.COMBAT, GLFW.GLFW_KEY_Y, "Automatically attacks entities around you that are far away.");
		this.getSliders().add(tpReach);
		this.getSliders().add(hitReach);
		this.getSliders().add(targetChangeDelay);
		this.getSliders().add(tpDist);
		this.getSliders().add(tpDelay);
		this.getCheckBoxes().add(noSwing);
		this.getCheckBoxes().add(critical);
		this.getCheckBoxes().add(inGui);
	}

	@Override
	public void onDisable() {
		target = null;
		startPos = null;
		px = 0;
		py = 0;
		pz = 0;
		lastTPPos = null;
		startedTP = false;
		back = false;
	}

	@Override
	public void onUpdate() {

		if (ModsUtil.canAura()) {

			try  {

				if (target == null && !back && !startedTP && lastTPPos == null) {

					startPos = null;
					px = 0;
					py = 0;
					pz = 0;
					lastTPPos = null;
					startedTP = false;
					back = false;

					if (targetChangeTimer.isDelayComplete(targetChangeDelay.getReturnValue())) {

						target = EntityUtil.getEntityInRange(tpReach.getReturnValue());

						targetChangeTimer.reset();

						target = EntityUtil.isEntityValid(target) && mc.player.getDistance(target) <= tpReach.getReturnValue() ? target : null;

						double yDist = PlayerUtil.posY(target) - PlayerUtil.posY();

						if (yDist > 2 || yDist < -2) {
							target = null;
						}
					}
				} else {

					if (!back && !startedTP) {

						double yDist = PlayerUtil.posY(target) - PlayerUtil.posY();

						if (yDist > 4 || yDist < -4) {
							target = null;
							startPos = null;
							px = 0;
							py = 0;
							pz = 0;
							lastTPPos = null;
							back = false;
						}
					}
				}
				if (back) {

					boolean kek = true;
					
					if (kek) {
					
					PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(startPos.getX(), startPos.getY(), startPos.getZ(), true));
					PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(startPos.getX(), -0, startPos.getZ(), true));
					
					target = null;
					startPos = null;
					px = 0;
					py = 0;
					pz = 0;
					lastTPPos = null;
					startedTP = false;
					back = false;
					return;
					}
					
					if (new BlockPos(px, py, pz) != startPos && !(EntityUtil.getDistance(px, py, pz, startPos.getX(), startPos.getY(), startPos.getZ()) <= 3)) {

						if (tpTimer.isDelayComplete(tpDelay.getReturnValue())) {

							double distance = startPos.getX() - px;

							if ((distance >= 0 && distance <= 1) || (distance >= -1	&& distance <= 0)) {

								double k = startPos.getZ() - pz;
								double z = k / tpDist.getReturnValue() + pz;

								if (ClientWorld.isValid(new BlockPos(px, py, z))) {
									//PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px, py, z, true));
									//PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px, -0, z, true));
									PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px, py, z, mc.player.onGround));
									//UTIL.setPos(px, py, z);

									lastTPPos = new BlockPos(px, py, z);

									//px = px;
									//py = py;
									pz = z;
								}

							} else {

								double k = (startPos.getZ() - pz) / (Math.abs(startPos.getX() - px));
								double z = k * tpDist.getReturnValue() + pz;

								if (ClientWorld.isValid(new BlockPos(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z))) {
									//PlayerUtil.sendPacket(new CPlayerPacket.PositionRotationPacket(px + (distance < 0 ? -TP_DIST.getReturnValue() : TP_DIST.getReturnValue()), py, z, mc.player.rotationYaw + mc.player.cameraYaw, mc.player.rotationPitch, true));
									//PlayerUtil.sendPacket(new CPlayerPacket.PositionRotationPacket(px + (distance < 0 ? -TP_DIST.getReturnValue() : TP_DIST.getReturnValue()), -0, z, mc.player.rotationYaw + mc.player.cameraYaw, mc.player.rotationPitch, true));
									PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z, mc.player.onGround));
									//UTIL.setPos(px + (distance < 0 ? -tpDist : tpDist), py, z);

									lastTPPos = new BlockPos(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z);

									px = px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue());
									//py = py;
									pz = z;
								}
							}
							tpTimer.reset();
						}

					} else {
						target = null;
						startPos = null;
						px = 0;
						py = 0;
						pz = 0;
						lastTPPos = null;
						startedTP = false;
						back = false;
					}

				} else if (target != null) {

					if (inGui.isChecked() || mc.currentScreen == null) {

						double yDist = PlayerUtil.posY(target) - PlayerUtil.posY();

						if (!EntityUtil.isEntityValid(target) || !(mc.player.getDistance(target) <= tpReach.getReturnValue()) || yDist > 2 || yDist < -2) {
							target = null;
						}
						if (EntityUtil.isEntityValid(target) && target != null) {

							if (tpTimer.isDelayComplete(tpDelay.getReturnValue()) && (lastTPPos == null || !(EntityUtil.getDistance(lastTPPos.getX(), lastTPPos.getY(), lastTPPos.getZ(), PlayerUtil.posX(target), PlayerUtil.posY(target), PlayerUtil.posZ(target)) <= hitReach.getReturnValue()))) {

								if (px == 0 || py == 0 || pz == 0) {
									px = PlayerUtil.posX();
									py = PlayerUtil.posY();
									pz = PlayerUtil.posZ();
								}
								double distance = PlayerUtil.posX(target) - px;

								if ((distance >= 0 && distance <= 1) || (distance >= -1	&& distance <= 0)) {

									double k = PlayerUtil.posZ(target) - pz;
									double z = k / tpDist.getReturnValue() + pz;

									if (startPos == null) {
										startPos = mc.player.getPosition();									
									}
									if (ClientWorld.isValid(new BlockPos(px, py, z))) {
										PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px, py, z, true));
										PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px, -0, z, true));

										lastTPPos = new BlockPos(px, py, z);

										pz = z;

										startedTP = true;
									}

								} else {

									double k = (PlayerUtil.posZ(target) - pz) / (Math.abs(PlayerUtil.posX(target) - px));
									double z = k * tpDist.getReturnValue() + pz;

									if (startPos == null) {
										startPos = mc.player.getPosition();									
									}
									if (ClientWorld.isValid(new BlockPos(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z))) {
										PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z, true));
										PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), -0, z, true));

										lastTPPos = new BlockPos(px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue()), py, z);

										px = px + (distance < 0 ? -tpDist.getReturnValue() : tpDist.getReturnValue());
										pz = z;

										startedTP = true;
									}
								}
								tpTimer.reset();
							}
							if (lastTPPos != null) {

								if (EntityUtil.getDistance(lastTPPos.getX(), lastTPPos.getY(), lastTPPos.getZ(), PlayerUtil.posX(target), PlayerUtil.posY(target), PlayerUtil.posZ(target)) <= hitReach.getReturnValue()) {

									if (mc.player.getCooledAttackStrength(0.0F) == 1.0F) {

										if (critical.isChecked()) {

											if (ModsUtil.canCheatCrit()) {
												PlayerUtil.critFromPos(lastTPPos.getX(), lastTPPos.getY(), lastTPPos.getZ());
											}
										}
										PlayerUtil.hit(target, noSwing.isChecked());
										lastTPPos = null;
										back = true;
									}
								}
							}

						} else {
							if (startedTP) {
								back = true;
							}
							target = null;
						}
					}
				}
			} catch (Exception ignored) {

			}
		}
	}

}