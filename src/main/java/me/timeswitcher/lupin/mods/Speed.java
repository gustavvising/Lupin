package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.ModsUtil;
import me.timeswitcher.lupin.utility.PlayerUtil;
import me.timeswitcher.lupin.utility.Time;
import me.timeswitcher.lupin.utility.MathUtil;
import net.minecraft.network.play.client.CPlayerPacket;

public class Speed extends Mod {

	private final Mode tp = new Mode("TP");
	private final Mode customTimer = new Mode("Custom Time");

	private final Slider timerStart = new Slider("Custom time start delay", 20.0f, 1.0f, 100.0f, 500.0f, true);
	private final Slider timerStop = new Slider("Custom time stop delay", 30.0f, 1.0f, 100.0f, 1000.0f, true);
	private final Slider timerReset = new Slider("Custom time reset delay", 40.0f, 1.0f, 100.0f, 5000.0f, true);

	private Time tpTimer = new Time();
	private Time timeTimer = new Time();

	public Speed(String name) {
		super(name, Category.MOVE, GLFW.GLFW_KEY_M, "Move fast.");
		this.setCurrentMode(tp);
		this.getModes().add(tp);
		this.getModes().add(customTimer);
		this.getSliders().add(timerStart);
		this.getSliders().add(timerStop);
		this.getSliders().add(timerReset);
	}

	private void tp() {

		if (ModsUtil.canSpeed() && mc.gameSettings.keyBindForward.isKeyDown()) {

			ModsUtil.setTimerSpeed(30);

			if (tpTimer.isDelayComplete(25)) {
				teleport(R.nextBoolean() ? 0.284d : 0.3d);
				tpTimer.reset();
			}
		} else {

			ModsUtil.resetTimerSpeed();
		}
	}

	private void teleport(double distanceIn) {
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.getHorizontalX(distanceIn), PlayerUtil.posY(), PlayerUtil.getHorizontalZ(distanceIn), true));
		PlayerUtil.sendPacket(new CPlayerPacket.PositionPacket(PlayerUtil.getHorizontalX(distanceIn), -0, PlayerUtil.getHorizontalZ(distanceIn), true));
	}

	private void customTimer() {
		if (PlayerUtil.isMoveKeys()) {

			if (timeTimer.isDelayComplete(timerStart.getReturnValue()) && !timeTimer.isDelayComplete(timerStop.getReturnValue())) {
				ModsUtil.setTimerSpeed(MathUtil.getRandomInt(3, 55));
			}
			if (timeTimer.isDelayComplete(timerStop.getReturnValue())) {
				ModsUtil.resetTimerSpeed();
			}
			if (timeTimer.isDelayComplete(timerReset.getReturnValue())) {
				timeTimer.reset();
			}

		} else {

			ModsUtil.resetTimerSpeed();
		}
	}

	@SuppressWarnings("unused")
	private void megaTimer() {
		if (ModsUtil.canSpeed()) {

			ModsUtil.setTimerSpeed(16.56913021619f);

			if (mc.player.ticksExisted % 5 == 0) {
				ModsUtil.setTimerSpeed(21.39151712887f);
			}
			if (mc.player.ticksExisted % 50 == 0) {
				ModsUtil.setTimerSpeed(150f);
			}
			if (!mc.player.onGround) {
				ModsUtil.resetTimerSpeed();
			}
		} else {
			ModsUtil.resetTimerSpeed();
		}
	}

	@Override
	public void onDisable() {
		ModsUtil.resetTimerSpeed();
	}

	@Override
	public void onUpdate() {

		if (this.getCurrentMode().equals(tp)) {

			tp();

		} else if (this.getCurrentMode().equals(customTimer)) {

			customTimer();
		}
	}
}