package me.timeswitcher.lupin.mod;

public class Slider {

	private String name;
	private float sliderValue;
	private float min;
	private float max;
	private float returnValue;
	private boolean maxRound;

	private float sliderWidth = 100;
	private float sliderHeight = 5;
	private float iconWidth = 5;
	private float iconHeight = 5;

	private float iconX = 0;
	private float iconY = 0;

	private boolean isDragged = false;
	private float dragX = 0;

	public Slider(String name, float value, float min, float max, float returnValue, Boolean maxRound) {
		this.name = name;
		this.sliderValue = value;
		this.min = min;
		this.max = max;
		this.returnValue = returnValue;
		this.setMaxRound(maxRound);
	} 

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getSliderValue() {
		return sliderValue;
	}

	public void setSliderValue(float numbervalue) {
		this.sliderValue = numbervalue;
	}

	public float getMin() {
		return this.min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return this.max;
	}

	public void setMax(float max) {
		this.min = max;
	}

	public float getReturnValue() {
		float percent = this.getSliderValue() / this.getMax();
		float value = (float) (maxRound ? Math.round(percent * this.returnValue) : ((int)((percent * this.returnValue) * 10D)) / 10.0D);

		if (value > (0.98*this.returnValue)) {

			value = this.returnValue;	

		} else if (value < (0.02*this.returnValue)) {

			value = this.getMin();
		}
		return value;
	}

	public void setReturnValue(float maxReturnValue) {
		this.returnValue = maxReturnValue;
	}
	
	public boolean isMaxRound() {
		return maxRound;
	}

	public void setMaxRound(boolean maxRound) {
		this.maxRound = maxRound;
	}

	public float getSliderWidth() {
		return sliderWidth;
	}

	public void setSliderWidth(float sliderWidth) {
		this.sliderWidth = sliderWidth;
	}

	public float getSliderHeight() {
		return sliderHeight;
	}

	public void setSliderHeight(float sliderHeight) {
		this.sliderHeight = sliderHeight;
	}

	public float getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(float iconWidth) {
		this.iconWidth = iconWidth;
	}

	public float getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(float iconHeight) {
		this.iconHeight = iconHeight;
	}

	public float getIconX() {
		return iconX;
	}

	public void setIconX(float iconX) {
		this.iconX = iconX;
	}

	public float getIconY() {
		return iconY;
	}

	public void setIconY(float iconY) {
		this.iconY = iconY;
	}

	public boolean isDragged() {
		return isDragged;
	}

	public void setDragged(boolean isDragged) {
		this.isDragged = isDragged;
	}

	public float getDragX() {
		return dragX;
	}

	public void setDragX(float dragX) {
		this.dragX = dragX;
	}
}