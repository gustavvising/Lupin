package me.timeswitcher.lupin.mods;

import org.lwjgl.glfw.GLFW;

import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.MathUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;

public class ChestStealer extends Mod {

	private final Time CHEST_TIMER = new Time();
	private int slotId;

	public ChestStealer(String name) {
		super(name, Category.WORLD, GLFW.GLFW_KEY_K, "Automatically loots the chests you open.");
	}

	@Override
	public void onUpdate() {
		if (mc.player.openContainer != null && mc.currentScreen instanceof ChestScreen) {

			ChestContainer c = (ChestContainer) mc.player.openContainer;

			if (c.getLowerChestInventory().isEmpty()) {
				slotId = 0;
				mc.player.closeScreen();
				CHEST_TIMER.reset();
			}
			if (c.getLowerChestInventory().getStackInSlot(slotId).isEmpty()) {
				slotId += 1;
			}
			if (c.getLowerChestInventory().getSizeInventory() > 27) {

				if (!(slotId <= 54)) {
					slotId = 0;
				}
			}
			if (c.getLowerChestInventory().getSizeInventory() <= 27) {

				if (!(slotId <= 27)) {
					slotId = 0;
				}
			}
			if (CHEST_TIMER.isDelayComplete(MathUtil.getRandomInt(64, 68) + R.nextFloat() + R.nextFloat())) {
				mc.playerController.windowClick(c.windowId, slotId, R.nextBoolean() ? 0 : 1, ClickType.QUICK_MOVE, mc.player);
				slotId += 1;
				CHEST_TIMER.reset();
			}
		} else {
			slotId = 0;
			CHEST_TIMER.reset();
		}
	}

}