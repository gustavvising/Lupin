package me.timeswitcher.lupin.screens;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.CheckBox;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.mod.Mode;
import me.timeswitcher.lupin.mod.Slider;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinClickGuiModOptionScreen extends Screen {

	private Mod mod;
	private int color;

	private final Color COMPONENTBACKGROUNDCOLOR = new Color(8, 34, 53);

	public LupinClickGuiModOptionScreen(Mod mod, int color) {
		super(new TranslationTextComponent("ClickGuiModOption"));
		this.mod = mod;
		this.color = color;
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

			RenderUtil.drawRect(mod.getModes().size() > 3 ? x - 150 : x - 125, y - 125, mod.getModes().size() > 3 ? x + 150 : x + 125, y + 125, LupinUtil.getClickGui().BACKGROUNDCOLORINT);
			RenderUtil.drawRect(mod.getModes().size() > 3 ? x - 150 : x - 125, y - 125 + LupinUtil.getClickGui().OVERLINEADJUSTMENT, mod.getModes().size() > 3 ? x + 150 : x + 125, y - 125, color);

			RenderSystem.pushMatrix();

			RenderSystem.scalef(1.5F, 1.5F, 1.5F);

			Lupin.mc.fontRenderer.drawStringWithShadow(mod.getName(), (x / 1.5F) - Lupin.mc.fontRenderer.getStringWidth(mod.getName()) / 2, (y / 1.5F) - 67, mod.isToggled() ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB());

			RenderSystem.scalef(1.0F, 1.0F, 1.0F);

			RenderSystem.popMatrix();

			if (!mod.getModes().isEmpty()) {

				float modeXStart = mod.getModes().size() > 3 ? x - 135: x - 110;
				float modeYStart = y + 90;

				for (Mode mode : mod.getModes()) {

					mode.setX(modeXStart);
					mode.setY(modeYStart);

					RenderUtil.drawRect(mode.getX(), mode.getY(), mode.getX() + mode.getWidth(), mode.getY() + mode.getHeight(), COMPONENTBACKGROUNDCOLOR.getRGB());
					int modeTextColor = mod.getCurrentMode() == null ? Color.LIGHT_GRAY.getRGB() : mod.getCurrentMode().equals(mode) ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB(); 
					Lupin.mc.fontRenderer.drawString(mode.getName(), mode.getX() + (mode.getWidth() / 2) - Lupin.mc.fontRenderer.getStringWidth(mode.getName()) / 2, mode.getY() + 7, modeTextColor);
					modeXStart += 70;
				}
			}

			if (mod.getSliders() != null && !mod.getSliders().isEmpty()) {

				float sliderXStart = x;
				float sliderYStart = y - 50;

				for (Slider slider : mod.getSliders()) {

					if (slider.isDragged() && slider.getDragX() != 0) {

						float mouseValue = mouseX + slider.getDragX();

						if (!(mouseValue < slider.getMin() || mouseValue > slider.getMax())) {
							slider.setSliderValue(mouseValue);
						}
					}
					slider.setIconX(slider.getSliderValue());
					slider.setIconY(sliderYStart);

					String valueText = slider.getName() + " \u00A77" + slider.getReturnValue();

					Lupin.mc.fontRenderer.drawString(valueText, (sliderXStart + slider.getSliderWidth() / 2) - Lupin.mc.fontRenderer.getStringWidth(valueText) / 2, sliderYStart - 15, Color.WHITE.getRGB());
					RenderUtil.drawRect(sliderXStart, sliderYStart, sliderXStart + slider.getSliderWidth(), sliderYStart + slider.getSliderHeight(), COMPONENTBACKGROUNDCOLOR.getRGB());
					RenderUtil.drawRect(sliderXStart + slider.getIconX(), sliderYStart, sliderXStart + slider.getIconX() + slider.getIconWidth(), sliderYStart + slider.getIconHeight(), color);

					sliderYStart += 40;
				}
			}

			if (mod.getCheckBoxes() != null && !mod.getCheckBoxes().isEmpty()) {

				float checkBoxXStart = x - 50;
				float checkBoxYStart = y - 50;

				for (CheckBox checkBox : mod.getCheckBoxes()) {

					checkBox.setX(checkBoxXStart);
					checkBox.setY(checkBoxYStart);

					String valueText = checkBox.getName() + (checkBox.isChecked() ? " \u00A77on" : " \u00A77off");

					Lupin.mc.fontRenderer.drawString(valueText, (checkBox.getX() + checkBox.getBoxWidth() / 2) - Lupin.mc.fontRenderer.getStringWidth(valueText) / 2, checkBox.getY() - 15, Color.WHITE.getRGB());
					RenderUtil.drawRect(checkBox.getX(), checkBox.getY(), checkBox.getX() + checkBox.getBoxWidth(), checkBox.getY() + checkBox.getBoxHeight(), COMPONENTBACKGROUNDCOLOR.getRGB());
					if (checkBox.isChecked()) {
						RenderUtil.drawRect(checkBox.getX() + 1, checkBox.getY() + 1, checkBox.getX() + checkBox.getBoxWidth() - 1, checkBox.getY() + checkBox.getBoxHeight() - 1, color);
					}
					checkBoxYStart += 40;
				}
			}
			super.render(mouseX, mouseY, partialTicks);
		}
	}

	private boolean isModeHovered(Mode mode, double mouseX, double mouseY) {
		return mouseX >= mode.getX() && mouseX <= mode.getX() + mode.getWidth() && mouseY >= mode.getY() && mouseY <= mode.getY() + mode.getHeight();
	}

	private boolean isCheckBoxHovered(CheckBox checkBox, double mouseX, double mouseY) {
		return mouseX >= checkBox.getX() && mouseX <= checkBox.getX() + checkBox.getBoxWidth() && mouseY >= checkBox.getY() && mouseY <= checkBox.getY() + checkBox.getBoxHeight();
	}

	private boolean isSliderIconHovered(Slider slider, double mouseX, double mouseY) {
		int x = this.width / 2;
		return mouseX >= x + slider.getIconX() && mouseX <= x + slider.getIconX() + slider.getIconWidth() && mouseY >= slider.getIconY() && mouseY <= slider.getIconY() + slider.getIconHeight();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

		if (mouseButton == 0 || mouseButton == 1) {

			if (!mod.getModes().isEmpty()) {

				for (Mode mode : mod.getModes()) {
					if (isModeHovered(mode, mouseX, mouseY)) {
						mod.setCurrentMode(mode);
						mod.setAnimation(0);
					}
				}
			}
			for (Slider slider : mod.getSliders()) {

				if (isSliderIconHovered(slider, mouseX, mouseY)) {
					slider.setDragged(true);
					slider.setDragX((float) (slider.getIconX() - mouseX));
				}
			}
			for (CheckBox checkBox : mod.getCheckBoxes()) {

				if (isCheckBoxHovered(checkBox, mouseX, mouseY)) {
					checkBox.setChecked(checkBox.isChecked() ? false : true);
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		for (Slider slider : mod.getSliders()) {
			slider.setDragX(0);
			slider.setDragged(false);
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onClose() {
		GameUtil.resize();
		Lupin.mc.displayGuiScreen(LupinUtil.getClickGui());
	}

}