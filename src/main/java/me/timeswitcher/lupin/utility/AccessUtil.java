package me.timeswitcher.lupin.utility;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;

public class AccessUtil {

    public static Boolean isMinecraftCodeInSRG = null;

    public static Boolean isSRG() {

        if (isMinecraftCodeInSRG == null) {

            Class<?> mcClass = Minecraft.class;

            try {

                @SuppressWarnings("unused")
                Field timerField = mcClass.getDeclaredField("timer");
                isMinecraftCodeInSRG = true;

            } catch (NoSuchFieldException e) {

                isMinecraftCodeInSRG = false;
            }
        }
        return isMinecraftCodeInSRG;
    }

    public static void applyTimerSpeed(float tickLength) {
        try {
            Class<?> mcClass = Minecraft.class;
            Field timerField = mcClass.getDeclaredField("timer");
            timerField.setAccessible(true);
            try {
                Object timer = timerField.get(Minecraft.getInstance());
                Class<?> timerClass = timer.getClass();
                Field timerSpeedField = timerClass.getDeclaredField("tickLength");
                timerSpeedField.setAccessible(true);
                timerSpeedField.setFloat(timer, tickLength);
            } catch (IllegalAccessException ignored) {

            }
        } catch (NoSuchFieldException ignored) {

        }
    }

    public static void applyTimerSpeedSRG(float tickLength) {
        try {
            Class<?> mcClass = Minecraft.class;
            Field timerField = mcClass.getDeclaredField("field_71428_T");
            timerField.setAccessible(true);
            try {
                Object timer = timerField.get(Minecraft.getInstance());
                Class<?> timerClass = timer.getClass();
                Field timerSpeedField = timerClass.getDeclaredField("field_194149_e");
                timerSpeedField.setAccessible(true);
                timerSpeedField.setFloat(timer, tickLength);
            } catch (IllegalAccessException ignored) {

            }
        } catch (NoSuchFieldException ignored) {

        }
    }
    
}