package me.timeswitcher.lupin.main;

public class LupinUser {

	public static final String SPECIALIGN = "TimeSwitched";
	
	private static String UID;

	public static String getUID() {
		return UID;
	}

	public static void setUID(String UID) {
		LupinUser.UID = UID;
	}
}