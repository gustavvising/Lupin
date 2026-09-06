package me.timeswitcher.lupin.utility;

public class MathUtil {

	public static int getRandomInt(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("Min " + min + " greater than max " + max);
		}
		return (int) ((int) min + Math.random() * ((int) max - min + 1));
	}

	public static double getRandomDouble(final double start, final double end) {
		if (start == end || end - start <= 0.0) {
			return start;
		}
		return start + (end - start) * Math.random();
	}

}