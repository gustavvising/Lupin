package me.timeswitcher.lupin.screens;

import java.awt.Color;
import java.util.ArrayList;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.mod.Category;
import me.timeswitcher.lupin.mod.Mod;
import me.timeswitcher.lupin.utility.LupinUtil;
import me.timeswitcher.lupin.utility.GameUtil;
import me.timeswitcher.lupin.utility.RenderUtil;
import me.timeswitcher.lupin.utility.Time;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class LupinClickGuiScreen extends Screen {

	public final Color BACKGROUNDCOLOR = new Color(7, 24, 36);
	public final int BACKGROUNDCOLORINT = new Color(7, 24, 36).getRGB();
	private final Color COMPONENTBACKGROUNDCOLOR = new Color(7, 26, 40);

	public final String ENABLEDTEXTCOLOR = "\u00A7f";
	public final String DISABLEDTEXTCOLOR = "\u00A77";

	private final Color MOVECOLOR = new Color(78, 234, 221);
	private final Color COMBATCOLOR = new Color(235, 77, 75);
	private final Color VISUALCOLOR = new Color(167, 94, 187);
	private final Color WORLDCOLOR = new Color(22, 175, 253);
	private final Color PLAYERCOLOR = new Color(83, 235, 136);
	private final Color EXTRACOLOR = new Color(253, 196, 55);

	private final int CATEGORYWIDTH = 90;
	private final int CATEGORYHEIGHT = 30;

	private final int CATEGORYYSTART = -150;

	public final int OVERLINEADJUSTMENT = 5;
	private final int HEIGHTCENTERADJUSTMENT = 18;
	private final int BUTTONSPACEADJUSTMENT = 22;
	private final float BUTTONSIZEADJUSTMENT = 3.5f;

	private final float TOOLTIPDELAY = 750f;

	private ArrayList<Panel> panels = new ArrayList<>();
	
	private LupinClickGuiTargetOptionScreen targetOptionScreen = new LupinClickGuiTargetOptionScreen();

	public LupinClickGuiScreen() {
		super(new TranslationTextComponent("ClickGui"));

		final Panel MOVE = new Panel("Move", -270, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.MOVE, MOVECOLOR.getRGB());
		final Panel COMBAT = new Panel("Combat", -180, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.COMBAT, COMBATCOLOR.getRGB());
		final Panel VISUAL = new Panel("Visual", -90, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.VISUAL, VISUALCOLOR.getRGB());
		final Panel WORLD = new Panel("World", 0, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.WORLD, WORLDCOLOR.getRGB());
		final Panel PLAYER = new Panel("Player", 90, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.PLAYER, PLAYERCOLOR.getRGB());
		final Panel EXTRA = new Panel("Extra", 180, CATEGORYYSTART, CATEGORYWIDTH, CATEGORYHEIGHT, Category.EXTRA, EXTRACOLOR.getRGB());

		panels.add(MOVE);
		panels.add(COMBAT);
		panels.add(VISUAL);
		panels.add(WORLD);
		panels.add(PLAYER);
		panels.add(EXTRA);

		for (Panel panel : panels) {
			Lupin.instance.getModHandler().getAllMods().stream().filter(module -> module.getCategory() == panel.getCategory())
			.forEach(module -> panel.addButton(new Button(panel, module, module.getName())));
		}
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

			RenderUtil.drawRect(x - 270, y + CATEGORYYSTART, x + 270, y + 170, BACKGROUNDCOLOR.getRed() / 255.0F, BACKGROUNDCOLOR.getGreen() / 255.0F, BACKGROUNDCOLOR.getBlue() / 255.0F, 1.0F);

			for (Panel panel : panels) {

				if (panel.isHideButtons()) {

					RenderUtil.drawRect(x + panel.getX(), y + panel.getY() + OVERLINEADJUSTMENT, x + (panel.getX() + panel.getWidth()), y + panel.getY(), panel.getColor());

				} else {

					RenderUtil.drawRect(x + panel.getX(), y - 120, x + (panel.getX() + panel.getWidth()), y + 170, COMPONENTBACKGROUNDCOLOR.getRGB());
					RenderUtil.drawRect(x + panel.getX(), y + panel.getY(), x + (panel.getX() + panel.getWidth()), y + (panel.getY() + panel.getHeight()), panel.getColor());
				}
				//Lupin.instance.getFontManager().verdana.drawStringWithShadow(panel.getName(), (int)(x + panel.getX() + panel.getWidth() / 2 - Lupin.instance.getFontManager().verdana.getStringWidth(panel.getName()) / 2), (int)(y + panel.getY() + (panel.getHeight() - HEIGHTCENTERADJUSTMENT) - 2), Color.WHITE.getRGB(), -1);
				font.drawStringWithShadow(panel.getName(), (int)(x + panel.getX() + panel.getWidth() / 2 - font.getStringWidth(panel.getName()) / 2), (int)(y + panel.getY() + (panel.getHeight() - HEIGHTCENTERADJUSTMENT)), Color.WHITE.getRGB());

				if (!panel.isHideButtons()) {

					for (int i = 0; i < panel.getButtons().size(); i++) {
						final Button button = panel.getButtons().get(i);

						if (button.getMod().isToggled()) {
							RenderUtil.drawRect(x + panel.getX(), y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + BUTTONSIZEADJUSTMENT, x + (panel.getX() + panel.getWidth()), y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + (panel.getHeight() / 2) + BUTTONSIZEADJUSTMENT, panel.getColor());
						}
						String textColor = button.getMod().isToggled() ? ENABLEDTEXTCOLOR : DISABLEDTEXTCOLOR;
						String text = button.getMod().hasOptions() ? (button.getMod().isToggled() ? textColor + button.getMod().getName() + " +" : textColor + button.getMod().getName() + " \u00A7r+") : textColor + button.getMod().getName();
						//Lupin.instance.getFontManager().verdana.drawString(text, (int) (x + panel.getX() + panel.getWidth() / 2 - Lupin.instance.getFontManager().verdana.getStringWidth(button.getText()) / 2), (int) (y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + (BUTTONSIZEADJUSTMENT * 2) - 2), panel.getColor(), -1);
						font.drawString(text, (float) (x + panel.getX() + panel.getWidth() / 2 - font.getStringWidth(button.getText()) / 2), (float) (y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + (BUTTONSIZEADJUSTMENT * 2)), panel.getColor());

						if (button.isHovered(mouseX, mouseY, i)) {

							if (button.toolTipTimer.isDelayComplete(TOOLTIPDELAY)) {

								RenderUtil.drawRect(x - 270, y + 170, x + 270, y + 180, panel.getColor());
								//Lupin.instance.getFontManager().verdana.drawStringWithShadow(button.getMod().getDescription(), x - (Lupin.instance.getFontManager().verdana.getStringWidth(button.getMod().getDescription()) / 2), y + 168, Color.WHITE.getRGB(), -1);
								font.drawStringWithShadow(button.getMod().getDescription(), x - (font.getStringWidth(button.getMod().getDescription()) / 2), y + 171, Color.WHITE.getRGB());
							}

						} else {

							button.toolTipTimer.reset();
						}
					}
					if (panel.getCategory() == Category.COMBAT) {
						RenderUtil.drawRect(x + panel.getX() + 5, y + 155, x + panel.getX() + panel.getWidth() - 5, y + 165, panel.getColor());
						//Lupin.instance.getFontManager().verdana.drawString("Target Options", (int) ((x + panel.getX() + (panel.getWidth() / 2)) - Lupin.instance.getFontManager().verdana.getStringWidth("Target Options") / 2), y + 154, Color.WHITE.getRGB(), -1);
						font.drawString("Target Options", (float) ((x + panel.getX() + (panel.getWidth() / 2)) - font.getStringWidth("Target Options") / 2), y + 156, Color.WHITE.getRGB());
					}
				}
			}
		}
		super.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

		if (mouseButton == 0 || mouseButton == 1) {

			for (int index = panels.size() - 1; index >= 0; index--) {
				final Panel panel = panels.get(index);

				for (int i = 0; i < panel.getButtons().size(); i++) {
					final Button button = panel.getButtons().get(i);

					if (!panel.isHideButtons()) {

						if (button.isHovered(mouseX, mouseY, i)) {

							if (mouseButton == 0) {

								button.getMod().toggleMod();

							} else {

								if (button.getMod().hasOptions()) {
									Lupin.mc.displayGuiScreen(new LupinClickGuiModOptionScreen(button.getMod(), panel.getColor()));
								}
							}
						}
					}
				}
				if (panel.isHovered(mouseX, mouseY)) {

					if (panel.isHideButtons() == true) {

						for (int setHideCount = panels.size() - 1; setHideCount >= 0; setHideCount--) {
							final Panel panelhider = panels.get(setHideCount);

							panelhider.setHideButtons(true);
						}
						panel.setHideButtons(false);

					} else if (panel.isHideButtons() == false) {

						panel.setHideButtons(true);
					}
				}
				if (panel.getCategory() == Category.COMBAT) {
					
					if (isTargetOptionsHovered(panel, mouseX, mouseY)) {
						Lupin.mc.displayGuiScreen(targetOptionScreen);
					}
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onClose() {
		GameUtil.resize();
		Lupin.mc.displayGuiScreen(null);
	}
	
	private boolean isTargetOptionsHovered(Panel panel, double mouseX, double mouseY) {
		double x = LupinClickGuiScreen.this.width / 2;
		double y = LupinClickGuiScreen.this.height / 2;
		return mouseX >= x + panel.getX() + 5 && mouseX <= x + panel.getX() + panel.getWidth() - 5 && mouseY >= y + 155 && mouseY <= y + 165;
	}

	public ArrayList<Panel> getPanels() {
		return panels;
	}

	public void setPanels(ArrayList<Panel> panels) {
		this.panels = panels;
	}

	class Panel {

		private String name;
		private Category category;
		private int color;
		private double x, y, width, height;
		private boolean hideButtons = true;

		private ArrayList<Button> buttons = new ArrayList<>();

		public Panel(String name, double x, double y, double width, double height, Category category, int color) {
			this.name = name;
			this.category = category;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {	
			this.name = name;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getWidth() {
			return width;
		}

		public void setWidth(double width) {

			this.width = width;
		}

		public double getHeight() {
			return height;
		}

		public void setHeight(double height) {
			this.height = height;
		}

		public ArrayList<Button> getButtons() {
			return buttons;
		}

		public void setButtons(ArrayList<Button> buttons) {
			this.buttons = buttons;
		}

		public void addButton(Button button) {
			buttons.add(button);
		}

		public void removeButton(Button button) {
			buttons.remove(button);
		}

		public boolean isHideButtons() {
			return hideButtons;
		}

		public void setHideButtons(boolean hideButtons) {
			this.hideButtons = hideButtons;
		}

		public boolean isHovered(final double mouseX, final double mouseY) {
			double x = LupinClickGuiScreen.this.width / 2;
			double y = LupinClickGuiScreen.this.height / 2;
			return mouseX >= x + getX() && mouseX <= x + getX() + getWidth() && mouseY >= y + getY() && mouseY <= y + getY() + getHeight();
		}
	}

	class Button {

		private Panel panel;
		private Mod mod;
		private String text ="";
		private Time toolTipTimer = new Time();

		public Button(Panel panel, Mod mod, String text) {
			this.panel = panel;
			this.text = text;
			this.mod = mod;
		}

		public Panel getPanel() {
			return panel;
		}

		public void setPanel(Panel panel) {
			this.panel = panel;
		}

		public Mod getMod() {
			return mod;
		}

		public void setMod(Mod mod) {
			this.mod = mod;
		}

		public String getText() {
			return this.text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Time getToolTipTime() {
			return toolTipTimer;
		}

		public void setToolTipTime(Time toolTipTime) {
			this.toolTipTimer = toolTipTime;
		}

		public boolean isHovered(double mouseX, double mouseY, int i) {
			int x = width / 2; 
			int y = height / 2;
			return mouseX >= x + panel.getX() && mouseX <= x + (panel.getX() + panel.getWidth()) && mouseY >= y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + BUTTONSIZEADJUSTMENT && mouseY <= y + CATEGORYYSTART + panel.getHeight() + (BUTTONSPACEADJUSTMENT * i) + (panel.getHeight() / 2) + BUTTONSIZEADJUSTMENT;
		}
	}
}