package me.timeswitcher.lupin.screens;

import java.awt.Color;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.renderings.InsertTextBox;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.HwidUtil;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinLoginScreen extends Screen {

	private InsertTextBox uidField;
	private InsertTextBox hwidField;
	private static String status;

	public LupinLoginScreen() {
		super(new TranslationTextComponent("Lupin Login"));
		GameUtil.setFullScreen();
	}

	@Override
	public void tick() {	
		if (!this.hwidField.getText().equals(HwidUtil.getHWID())) {
			this.hwidField.setText(HwidUtil.getHWID());
		}
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {

		if (isLoginHovered(p_mouseClicked_1_, p_mouseClicked_3_)) {

			if (uidField.getText().length() == 4) {

				String s = uidField.getText();

				try {

					LupinUtil.login(s);

					if (LupinUtil.isLoginFailed()) {
						setStatus("\u00A74Login Failed (Contact TimeSwitcher If you need help)");
					}

				} catch (Exception e) {

					setStatus("\u00A7eException");
				}

			} else {

				setStatus("\u00A7eYou're user id should be four digits");
			}
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	public void init() {
		this.uidField = new InsertTextBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, I18n.format("UID"));
		this.uidField.setMaxStringLength(4);
		this.children.add(this.uidField);

		this.hwidField = new InsertTextBox(this.font, this.width / 2 - 100, this.height / 2 + 95, 200, 20, I18n.format("HWID"));
		this.hwidField.setText(HwidUtil.getHWID());
		this.children.add(this.hwidField);
	}

	@Override
	public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
		if (this.uidField != null && this.uidField.getText() != null) {
		String s = this.uidField.getText();
		this.init(p_resize_1_, p_resize_2_, p_resize_3_);
		this.uidField.setText(s);
		}
	}

	private boolean isLoginHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100 && mouseY >= this.height / 2 + 17 && mouseY <= this.height / 2 + 37;
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		
		this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

		RenderUtil.drawImage(LupinUtil.LUPINLOGO, this.width / 2 - 100, this.height / 2 - 180, 200, 200, 1.0f);

		Lupin.instance.getFontManager().verdana.drawString("Enter you're user id here", this.width / 2 - 100, this.height / 2 - 25, Color.WHITE.getRGB(), -1);
		//this.drawString(this.font, "Enter you're user id here", this.width / 2 - 100, this.height / 2 - 25, Color.WHITE.getRGB());
		this.uidField.render(p_render_1_, p_render_2_, p_render_3_);

		RenderUtil.drawRect(this.width / 2 - 100, this.height / 2 + 37, this.width / 2 + 100, this.height / 2 + 17, 0, 0, 0, 0.9F);
		
		Lupin.instance.getFontManager().verdana.drawCenteredString("Login", this.width / 2, this.height / 2 + 25, Color.WHITE.getRGB(), -1);
		//this.drawCenteredString(this.font, "Login", this.width / 2, this.height / 2 + 23, Color.WHITE.getRGB());
		Lupin.instance.getFontManager().verdana.drawCenteredString(getStatus(), this.width / 2, this.height / 2 + 42, 16777215, -1);
		//this.drawCenteredString(this.font, getStatus(), this.width / 2, this.height / 2 + 40, 16777215);

		Lupin.instance.getFontManager().verdana.drawCenteredString("If this is you're first time, contact TimeSwitcher to get a Lupin user id", this.width / 2, this.height / 2 + 60, 16777215, -1);
		//this.drawCenteredString(this.font, "If this is you're first time, contact TimeSwitcher to get a Lupin user id", this.width / 2, this.height / 2 + 60, 16777215);
		Lupin.instance.getFontManager().verdana.drawCenteredString("And send the code from the box below to TimeSwitcher", this.width / 2, this.height / 2 + 80, Color.WHITE.getRGB(), -1);
		//this.drawCenteredString(this.font, "And send the code from the box below to TimeSwitcher", this.width / 2, this.height / 2 + 80, Color.WHITE.getRGB());
		this.hwidField.render(p_render_1_, p_render_2_, p_render_3_);

		super.render(p_render_1_, p_render_2_, p_render_3_);
	}

	private String getStatus() {
		return status;
	}

	public static void setStatus(final String s) {
		status = s;
	}
}