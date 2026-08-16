package me.timeswitcher.lupin.screens;

import java.awt.Color;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.renderings.InsertTextBox;
import me.timeswitcher.lupin.utility.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinAltLoginScreen extends Screen {

	private InsertTextBox userField;
	private InsertTextBox passField;
	private InsertTextBox userpassField;
	private Screen parentScreen;
	private boolean error = false;
	private int e = 0;

	LupinAltLoginScreen(Screen parentScreen) {
		super(new TranslationTextComponent("Alt login"));
		this.parentScreen = parentScreen;
	}

	@Override
	public void tick() {
		this.passField.tick();
		this.userField.tick();
		this.userpassField.tick();
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {

		if (isLoginHovered(p_mouseClicked_1_, p_mouseClicked_3_)) {

			if (passField.getText().length() > 1 && userField.getText().length() > 1) {

				String s = userField.getText();
				String s1 = passField.getText();
				String CheckID = Lupin.mc.getSession().getPlayerID();

				try {

					Lupin.instance.getAltManager().login(s, s1);

					if (Lupin.mc.getSession().getPlayerID() != CheckID) {

						e = 0;
						error = false;

						Lupin.mc.displayGuiScreen(parentScreen);

					} else {

						e = 0;
						error = true;
					}

				} catch (Exception e) {

				}

			} else {

				if (userpassField.getText().length() > 2) {

					String[] credentials = userpassField.getText().split(":");

					String s = credentials[0];
					String s1 = credentials[1];
					String CheckID = Lupin.mc.getSession().getPlayerID();

					try {

						Lupin.instance.getAltManager().login(s, s1);

						if (Lupin.mc.getSession().getPlayerID() != CheckID) {

							e = 0;
							error = false;

							Lupin.mc.displayGuiScreen(parentScreen);

						} else {

							e = 0;
							error = true;
						}

					} catch (Exception e) {

					}
				} else {

					Lupin.instance.getAltManager().loginOffline(userField.getText());

					Lupin.mc.displayGuiScreen(parentScreen);
				}
			}
		}

		if (isBackHovered(p_mouseClicked_1_, p_mouseClicked_3_)) {

			Lupin.mc.displayGuiScreen(parentScreen);
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	protected void init() {
		this.minecraft.keyboardListener.enableRepeatEvents(true);

		this.userField = new InsertTextBox(this.font, this.width / 2 - 100, 66, 200, 20, I18n.format("User"));
		this.userField.setText("");
		this.children.add(this.userField);

		this.passField = new InsertTextBox(this.font, this.width / 2 - 100, 106, 200, 20, I18n.format("Password"));
		this.passField.setText("");
		this.children.add(this.passField);

		this.userpassField = new InsertTextBox(this.font, this.width / 2 - 100, 146, 200, 20, I18n.format("User:Password"));
		this.userpassField.setText("");
		this.children.add(this.userpassField);
	}

	@Override
	public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
		String s = this.userField.getText();
		String s1 = this.passField.getText();
		String s2 = this.userpassField.getText();
		this.init(p_resize_1_, p_resize_2_, p_resize_3_);
		this.userField.setText(s);
		this.passField.setText(s1);
		this.userpassField.setText(s2);
	}

	@Override
	public void removed() {
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void onClose() {
		Lupin.mc.displayGuiScreen(parentScreen);
	}

	private boolean isLoginHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100 && mouseY >= this.height / 2 + 55 + 10 && mouseY <= this.height / 2 + 55 + 30;
	}

	private boolean isBackHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100 && mouseY >= this.height / 2 + 55 + 40 && mouseY <= this.height / 2 + 55 + 60;
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
		Lupin.instance.getFontManager().verdana.drawCenteredString("Change minecraft account", this.width / 2, 20, 16777215, -1);
		//this.drawCenteredString(this.font, "Change minecraft account", this.width / 2, 17, 16777215);
		Lupin.instance.getFontManager().verdana.drawString("User", this.width / 2 - 100, 53, Color.WHITE.getRGB(), -1);
		//this.drawString(this.font, I18n.format("User"), this.width / 2 - 100, 53, Color.WHITE.getRGB());
		Lupin.instance.getFontManager().verdana.drawString("Password", this.width / 2 - 100, 93, Color.WHITE.getRGB(), -1);
		//this.drawString(this.font, I18n.format("Password"), this.width / 2 - 100, 94, Color.WHITE.getRGB());
		Lupin.instance.getFontManager().verdana.drawString("User:Password", this.width / 2 - 100, 133, Color.WHITE.getRGB(), -1);
		//this.drawString(this.font, I18n.format("User:Password"), this.width / 2 - 100, 135, Color.WHITE.getRGB());
		this.userField.render(p_render_1_, p_render_2_, p_render_3_);
		this.passField.render(p_render_1_, p_render_2_, p_render_3_);
		this.userpassField.render(p_render_1_, p_render_2_, p_render_3_);

		if (error == true) {

			Lupin.instance.getFontManager().verdana.drawCenteredString("\u00A74Login failed", this.width / 2, 35, Color.RED.getRGB(), -1);
			//drawCenteredString(font, "\u00A74Login failed", this.width / 2, 35, Color.RED.getRGB());

			e += 1;

			if (e >= 250) {

				e = 0;
				error = false;
			}
		}
		RenderUtil.drawRect(this.width / 2 - 100, this.height / 2 + 55 + 10, this.width / 2 + 100, this.height / 2 + 55 + 30, 0, 0, 0, 0.9f);
		Lupin.instance.getFontManager().verdana.drawString("Login", this.width / 2 - 12, this.height / 2 + 69, Color.WHITE.getRGB(), -1);
		//font.drawString("Login", this.width / 2 - 12, this.height / 2 + 55 + 16, Color.WHITE.getRGB());

		RenderUtil.drawRect(this.width / 2 - 100, this.height / 2 + 55 + 60, this.width / 2 + 100, this.height / 2 + 55 + 40, 0, 0, 0, 0.9f);
		Lupin.instance.getFontManager().verdana.drawString("Back", this.width / 2 - 10, this.height / 2 + 99, Color.WHITE.getRGB(), -1);
		//font.drawString("Back", this.width / 2 - 10, this.height / 2 + 55 + 46, Color.WHITE.getRGB());

		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
}