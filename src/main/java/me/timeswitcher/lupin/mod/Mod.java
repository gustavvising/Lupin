package me.timeswitcher.lupin.mod;

import java.util.ArrayList;
import java.util.Random;

import me.timeswitcher.lupin.main.Lupin;
import net.minecraft.client.Minecraft;

public class Mod {

	private String name;
	private boolean toggled;
	private Category category;
	private int toggleKey;
	private String description;
	private Mode currentMode;
	private int animation;

	private ArrayList<Mode> modes = new ArrayList<Mode>();
	private ArrayList<Slider> sliders = new ArrayList<Slider>();
	private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();

	protected static Minecraft mc = Lupin.mc;

	protected static final Random R = new Random();

	public Mod(String name, Category category, int toggleKey, String description) {
		this.setName(name);
		this.setCategory(category);
		this.setToggleKey(toggleKey);
		this.setDescription(description);

		if (this.getModes().isEmpty()) {

			this.currentMode = null;

		} else {

			this.currentMode = this.getModes().get(1);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameAndMode() {
		String s;

		if (currentMode != null) {
			s = name + " \u00A77" + currentMode.getName();
		} else {
			s = name;
		}
		return s;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getToggleKey() {
		return toggleKey;
	}

	public void setToggleKey(int toggleKey) {
		this.toggleKey = toggleKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public ArrayList<Slider> getSliders() {
		return sliders;
	}

	public ArrayList<Mode> getModes() {
		return modes;
	}

	public void setModes(ArrayList<Mode> modes) {
		this.modes = modes;
	}

	public void setSliders(ArrayList<Slider> sliderValues) {
		this.sliders = sliderValues;
	}

	public ArrayList<CheckBox> getCheckBoxes() {
		return checkBoxes;
	}

	public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
		this.checkBoxes = checkBoxes;
	}

	public boolean hasOptions() {
		return !getSliders().isEmpty() || !getCheckBoxes().isEmpty() || !getModes().isEmpty();
	}

	public void toggleMod() {
		if (this.isToggled()) {
			this.onDisable();
			this.setAnimation(0);
			this.setToggled(false);
		} else {
			this.onEnable();
			this.setAnimation(0);
			this.setToggled(true);
		} 
	}

	public void onEnable() {}
	public void onDisable() {}
	public void onUpdate() {}

}