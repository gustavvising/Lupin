package me.timeswitcher.lupin.utility;

import java.util.ArrayList;

public class HwidUtil {

	private static String hwid = "";

	public static String getHWID() {

		String returnhwid = "";

		if (hwid.isEmpty()) {

			String hwid = System.getProperty("user.name") + System.getProperty("user.home") + System.getProperty("user.country") + System.getProperty("os.name");

			for (String s : getSubstrings(hwid)) {
				returnhwid = returnhwid + convertToString(s);
			}
		} else {
			returnhwid = hwid;
		}
		hwid = returnhwid;
		return returnhwid;
	}

	private static int convertToString(String lette) {
		LETTERS letter = null;
		for (int i = 0; i < LETTERS.values().length; i++) {
			if (getLetter(i).equalsIgnoreCase(lette)) {
				letter = LETTERS.values()[i];
			}
		}
		int string = -1;
		if (letter == LETTERS.A) {
			string = 2;
		} else if (letter == LETTERS.B) {
			string = 3;
		} else if (letter == LETTERS.C) {
			string = 5;
		} else if (letter == LETTERS.D) {
			string = 7;
		} else if (letter == LETTERS.E) {
			string = 11;
		} else if (letter == LETTERS.F) {
			string = 13;
		} else if (letter == LETTERS.G) {
			string = 17;
		} else if (letter == LETTERS.H) {
			string = 19;
		} else if (letter == LETTERS.I) {
			string = 23;
		} else if (letter == LETTERS.J) {
			string = 29;
		} else if (letter == LETTERS.K) {
			string = 31;
		} else if (letter == LETTERS.L) {
			string = 37;
		} else if (letter == LETTERS.M) {
			string = 41;
		} else if (letter == LETTERS.N) {
			string = 43;
		} else if (letter == LETTERS.O) {
			string = 47;
		} else if (letter == LETTERS.P) {
			string = 53;
		} else if (letter == LETTERS.Q) {
			string = 59;
		} else if (letter == LETTERS.R) {
			string = 61;
		} else if (letter == LETTERS.S) {
			string = 67;
		} else if (letter == LETTERS.T) {
			string = 71;
		} else if (letter == LETTERS.U) {
			string = 73;
		} else if (letter == LETTERS.V) {
			string = 79;
		} else if (letter == LETTERS.W) {
			string = 83;
		} else if (letter == LETTERS.X) {
			string = 89;
		} else if (letter == LETTERS.Y) {
			string = 97;
		} else if (letter == LETTERS.Z) {
			string = 101;
		}
		return string;
	}

	private static ArrayList<String> getSubstrings(String s) {
		ArrayList<String> substrings = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			String substring = s.substring(i, i + 1);
			substrings.add(substring);
		}
		return substrings;
	}

	private enum LETTERS {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	}

	private static String getLetter(int letter) {
		return LETTERS.values()[letter].name();
	}
}