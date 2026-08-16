package me.timeswitcher.lupin.screens;

import java.awt.Color;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.particlesystem.ParticleSystem;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinMainMenuScreen extends Screen {

	private final ResourceLocation SINGLEPLAYER = new ResourceLocation("modid", "singleplayer.png");

	private final ResourceLocation MULTIPLAYER = new ResourceLocation("modid", "multiplayer.png");

	private final ResourceLocation REALM = new ResourceLocation("modid", "realm.png");

	private final ResourceLocation FORGE = new ResourceLocation("modid", "forge.png");

	private final ResourceLocation LANGUAGE = new ResourceLocation("modid", "language.png");

	private final ResourceLocation SETTINGS = new ResourceLocation("modid", "settings.png");

	private final ResourceLocation ACCESSIBILITY = new ResourceLocation("modid", "accessibility.png");

	private final ResourceLocation QUIT = new ResourceLocation("modid", "quit.png");

	private final int BUTTONTEXTCOLOR = Color.WHITE.getRGB();

	private final float BUTTONHOVERALPHA = 0.3f;

	private ParticleSystem particleSystem = null;

	public LupinMainMenuScreen() {
		super(new TranslationTextComponent("MainMenu"));
	}

	@Override
	public void tick() {

		particleSystem.tick(this.width, this.height);

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
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void init() {
		if (particleSystem == null) {
			particleSystem = new ParticleSystem();
			particleSystem.addParticles(this.width, this.height, 250);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {

		drawBackground();

		drawParticles(mouseX, mouseY);

		if (LupinUtil.isShowLogo()) {
			drawLogo();
		}

		drawButtons(mouseX, mouseY);

		super.render(mouseX, mouseY, partialTicks);
	}

	private void drawBackground() {
		this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
	}

	private void drawParticles(final int mouseX, final int mouseY) {
		particleSystem.render(mouseX, mouseY);
	}

	private void drawLogo() {

		RenderUtil.drawImage(LupinUtil.LUPINLOGO, this.width / 2 - 200, this.height / 2 - 380, 400, 400, 1.0f);
		
		//int x = this.width / 2;
		//int y = this.height / 2;
		
		//RenderUtil.drawRect(x - 99, y - 206, x - 91, y - 150, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x - 91, y - 158, x - 59, y - 150, RenderUtil.getWhiteFadeEffect());

		//RenderUtil.drawRect(x - 51, y - 190, x - 43, y - 158, RenderUtil.getRainbowLight(15000, 9000));
		//RenderUtil.drawRect(x - 43, y - 158, x - 19, y - 150, RenderUtil.getRainbowLight(15000, 9000));
		//RenderUtil.drawRect(x - 19, y - 190, x - 11, y - 150, RenderUtil.getRainbowLight(15000, 9000));

		//RenderUtil.drawRect(x - 3, y - 190, x + 5, y - 142, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 5, y - 166, x + 29, y - 158, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 29, y - 182, x + 37, y - 166, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 13, y - 190, x + 29, y - 182, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 5, y - 182, x + 13, y - 174, RenderUtil.getWhiteFadeEffect());

		//RenderUtil.drawRect(x + 45, y - 190, x + 53, y - 150, RenderUtil.getRainbowLight(15000, 9000));
		//RenderUtil.drawRect(x + 45, y - 206, x + 53, y - 198, RenderUtil.getRainbowLight(15000, 9000));

		//RenderUtil.drawRect(x + 61, y - 190, x + 69, y - 150, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 69, y - 190, x + 93, y - 182, RenderUtil.getWhiteFadeEffect());
		//RenderUtil.drawRect(x + 93, y - 182, x + 101, y - 150, RenderUtil.getWhiteFadeEffect());
	}

	private void drawButtons(int mouseX, int mouseY) {

		int x = this.width / 2;
		int y = this.height / 2;

		RenderUtil.drawImage(SINGLEPLAYER, x - 240, y - 90, 120, 120, 1.0f);
		if (isSingleplayerHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x - 240, y - 90, x - 120, y + 30, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(MULTIPLAYER, x - 120, y - 90, 120, 120, 1.0f);
		if (isMultiplayerHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x - 120, y - 90, x, y + 30, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(REALM, x, y - 90, 120, 120, 1.0f);
		if (isRealmHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x, y - 90, x + 120, y + 30, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(FORGE, x + 120, y - 90, 120, 120, 1.0f);
		if (isForgeHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x + 120, y - 90, x + 240, y + 30, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(LANGUAGE, x - 240, y + 30, 120, 120, 1.0f);
		if (isLanguageHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x - 240, y + 30, x - 120, y + 150, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(SETTINGS, x - 120, y + 30, 120, 120, 1.0f);
		if (isSettingsHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x - 120, y + 30, x, y + 150, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(ACCESSIBILITY, x, y + 30, 120, 120, 1.0f);
		if (isAccessibilityHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x, y + 30, x + 120, y + 150, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		RenderUtil.drawImage(QUIT, x + 120, y + 30, 120, 120, 1.0f);
		if (isQuitHovered(mouseX, mouseY)) {
			RenderUtil.drawRect(x + 120, y + 30, x + 240, y + 150, 1.0f, 1.0f, 1.0f, BUTTONHOVERALPHA);
		}

		Lupin.instance.getFontManager().verdanaBold.drawString("SINGLEPLAYER", x - 219, y + 10, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("MULTIPLAYER", x - 95, y + 10, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("REALM", x + 43, y + 10, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("FORGE", x + 163, y + 10, BUTTONTEXTCOLOR, -1);

		Lupin.instance.getFontManager().verdanaBold.drawString("LANGUAGE", x - 207, y + 130, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("SETTINGS", x - 85, y + 130, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("ACCESSIBILITY", x + 20, y + 130, BUTTONTEXTCOLOR, -1);
		Lupin.instance.getFontManager().verdanaBold.drawString("QUIT", x + 167, y + 130, BUTTONTEXTCOLOR, -1);
		
		//font.drawStringWithShadow("Singleplayer", x - 210, y + 10, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Multiplayer", x - 86.5f, y + 10, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Realm", x + 43.5f, y + 10, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Forge", x + 165.5f, y + 10, BUTTONTEXTCOLOR);

		//font.drawStringWithShadow("Language", x - 205.5f, y + 130, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Settings", x - 80.5f, y + 130, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Accessibility", x + 30, y + 130, BUTTONTEXTCOLOR);
		//font.drawStringWithShadow("Quit", x + 171.5f, y + 130, BUTTONTEXTCOLOR);
	}

	private boolean isSingleplayerHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 240 && mouseX <= this.width / 2 - 120 && mouseY >= this.height / 2 - 90 && mouseY <= this.height / 2 + 30;
	}

	private boolean isMultiplayerHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 120 && mouseX <= this.width / 2 && mouseY >= this.height / 2 - 90 && mouseY <= this.height / 2 + 30;
	}

	private boolean isRealmHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 && mouseX <= this.width / 2 + 120 && mouseY >= this.height / 2 - 90 && mouseY <= this.height / 2 + 30;
	}

	private boolean isForgeHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 + 120 && mouseX <= this.width / 2 + 240 && mouseY >= this.height / 2 - 90 && mouseY <= this.height / 2 + 30;
	}

	private boolean isLanguageHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 240 && mouseX <= this.width / 2 - 120 && mouseY >= this.height / 2 + 30 && mouseY <= this.height / 2 + 150;
	}

	private boolean isSettingsHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 - 120 && mouseX <= this.width / 2 && mouseY >= this.height / 2 + 30 && mouseY <= this.height / 2 + 150;
	}

	private boolean isAccessibilityHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 && mouseX <= this.width / 2 + 120 && mouseY >= this.height / 2 + 30 && mouseY <= this.height / 2 + 150;
	}

	private boolean isQuitHovered(final double mouseX, final double mouseY) {
		return mouseX >= this.width / 2 + 120 && mouseX <= this.width / 2 + 240 && mouseY >= this.height / 2 + 30 && mouseY <= this.height / 2 + 150;
	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (isSingleplayerHovered(mouseX, mouseY)) {
			this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isMultiplayerHovered(mouseX, mouseY)) {

			if (this.minecraft.gameSettings.field_230152_Z_) {

				this.minecraft.displayGuiScreen(new MultiplayerScreen(this));

			} else {

				this.minecraft.displayGuiScreen(new MultiplayerWarningScreen(this));
			}
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isRealmHovered(mouseX, mouseY)) {
			switchToRealms();
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isForgeHovered(mouseX, mouseY)) {
			this.minecraft.displayGuiScreen(new net.minecraftforge.fml.client.gui.screen.ModListScreen(this));
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isLanguageHovered(mouseX, mouseY)) {
			this.minecraft.displayGuiScreen(new LanguageScreen(this, this.minecraft.gameSettings, this.minecraft.getLanguageManager()));
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isSettingsHovered(mouseX, mouseY)) {
			this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isAccessibilityHovered(mouseX, mouseY)) {
			this.minecraft.displayGuiScreen(new AccessibilityScreen(this, this.minecraft.gameSettings));
			GameUtil.resize();
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (isQuitHovered(mouseX, mouseY)) {
			GameUtil.resize();
			this.minecraft.shutdown();
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}