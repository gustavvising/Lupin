package me.timeswitcher.lupin.mod;

public class CheckBox {

	private String name;
	private boolean checked;
	
	private float boxWidth = 10;
	private float boxHeight = 10;
	
	private float x;
	private float y;

	public CheckBox(String name, boolean checked) {
		this.setName(name);
		this.setChecked(checked);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public float getBoxWidth() {
		return boxWidth;
	}

	public void setBoxWidth(float boxWidth) {
		this.boxWidth = boxWidth;
	}

	public float getBoxHeight() {
		return boxHeight;
	}

	public void setBoxHeight(float boxHeight) {
		this.boxHeight = boxHeight;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	} 
}