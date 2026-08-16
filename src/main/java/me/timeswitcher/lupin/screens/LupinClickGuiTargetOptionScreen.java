package me.timeswitcher.lupin.screens;

import java.awt.Color;
import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.EntityUtil;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinClickGuiTargetOptionScreen extends Screen {

	private final Color COMBATCOLOR = new Color(235, 77, 75);
	private final Color COMPONENTBACKGROUNDCOLOR = new Color(8, 34, 53);

	private ArrayList<CheckBox> targetOptions = new ArrayList<CheckBox>();

	public LupinClickGuiTargetOptionScreen() {
		super(new TranslationTextComponent("ClickGuiTargetOption"));
		this.targetOptions.add(EntityUtil.players);
		this.targetOptions.add(EntityUtil.monsters);
		this.targetOptions.add(EntityUtil.animals);
		this.targetOptions.add(EntityUtil.tameables);
		this.targetOptions.add(EntityUtil.villagers);
		this.targetOptions.add(EntityUtil.teams);
		this.targetOptions.add(EntityUtil.friends);
		this.targetOptions.add(EntityUtil.invisibles);
		this.targetOptions.add(EntityUtil.deads);
	}

	@Override
	public void tick() {
		if (!GameUtil.isFullScreen()) {

			if (!LupinUtil.isResize() && Lupin.mc.gameSettings.guiScale != 1) {
				LupinUtil.setGuiScale(Lupin.mc.gameSettings.guiScale);
				Lupin.mc.gameSettings.guiScale = 1;
				Lupin.mc.updateWindowSize();
				LupinUtil.setResize(true);
			}
		} else {

			if (LupinUtil.isResize()) {
				Lupin.mc.gameSettings.guiScale = LupinUtil.getGuiScale();
				Lupin.mc.updateWindowSize();
				LupinUtil.setResize(false);
			}
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {

		if (!GameUtil.isGameNull()) {

			int x = this.width / 2;
			int y = this.height / 2;

			RenderUtil.drawRect(x - 125, y - 125, x + 125, y + 125, LupinUtil.getClickGui().BACKGROUNDCOLORINT);
			RenderUtil.drawRect(x - 125, y - 125 + LupinUtil.getClickGui().OVERLINEADJUSTMENT, x + 125, y - 125, COMBATCOLOR.getRGB());

			RenderSystem.pushMatrix();

			RenderSystem.scalef(1.5F, 1.5F, 1.5F);

			//Lupin.instance.getFontManager().verdana.drawString("Target Options", (int) (x / 1.5F) - Lupin.instance.getFontManager().verdana.getStringWidth("Target Options") / 2, (int) (y / 1.5F) - 69, Color.WHITE.getRGB(), -1);
			Lupin.mc.fontRenderer.drawStringWithShadow("Target Options", (x / 1.5F) - Lupin.mc.fontRenderer.getStringWidth("Target Options") / 2, (y / 1.5F) - 67, Color.WHITE.getRGB());

			RenderSystem.scalef(1.0F, 1.0F, 1.0F);

			RenderSystem.popMatrix();

			if (targetOptions != null) {

				float checkBoxXStart = x - 50;
				float checkBoxYStart = y - 50;

				int drawedBoxes = 0;
				
				for (CheckBox checkBox : targetOptions) {

					if (drawedBoxes == 5) {
						checkBoxXStart += 100;
						checkBoxYStart = y - 50;
					}
					checkBox.setX(checkBoxXStart);
					checkBox.setY(checkBoxYStart);

					String valueText = checkBox.getName() + (checkBox.isChecked() ? " \u00A77on" : " \u00A77off");

					//Lupin.instance.getFontManager().verdana.drawString(valueText, (int) (checkBox.getX() + checkBox.getBoxWidth() / 2) - Lupin.instance.getFontManager().verdana.getStringWidth(valueText) / 2, (int) checkBox.getY() - 15, Color.WHITE.getRGB(), -1);
					Lupin.mc.fontRenderer.drawString(valueText, (checkBox.getX() + checkBox.getBoxWidth() / 2) - Lupin.mc.fontRenderer.getStringWidth(valueText) / 2, checkBox.getY() - 15, Color.WHITE.getRGB());
					RenderUtil.drawRect(checkBox.getX(), checkBox.getY(), checkBox.getX() + checkBox.getBoxWidth(), checkBox.getY() + checkBox.getBoxHeight(), COMPONENTBACKGROUNDCOLOR.getRGB());
					if (checkBox.isChecked()) {
						RenderUtil.drawRect(checkBox.getX() + 1, checkBox.getY() + 1, checkBox.getX() + checkBox.getBoxWidth() - 1, checkBox.getY() + checkBox.getBoxHeight() - 1, COMBATCOLOR.getRGB());
					}
					drawedBoxes += 1;
					checkBoxYStart += 40;
				}
			}
			super.render(mouseX, mouseY, partialTicks);
		}
	}

	public boolean isCheckBoxHovered(CheckBox checkBox, double mouseX, double mouseY) {
		return mouseX >= checkBox.getX() && mouseX <= checkBox.getX() + checkBox.getBoxWidth() && mouseY >= checkBox.getY() && mouseY <= checkBox.getY() + checkBox.getBoxHeight();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

		if (mouseButton == 0 || mouseButton == 1) {

			for (CheckBox checkBox : targetOptions) {

				if (isCheckBoxHovered(checkBox, mouseX, mouseY)) {
					checkBox.setChecked(checkBox.isChecked() ? false : true);
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onClose() {
		GameUtil.resize();
		Lupin.mc.displayGuiScreen(LupinUtil.getClickGui());
	}
}