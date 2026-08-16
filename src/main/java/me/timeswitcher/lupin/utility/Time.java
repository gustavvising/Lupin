package me.timeswitcher.lupin.utility;

public class Time
{
	private long time;

	public boolean isDelayComplete(float f) {
		return System.currentTimeMillis() - this.time > f;
	}

	public void reset() {
		this.time = System.currentTimeMillis();
	}
}