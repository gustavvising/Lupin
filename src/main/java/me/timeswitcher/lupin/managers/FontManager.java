package me.timeswitcher.lupin.managers;

import java.awt.Font;

import me.timeswitcher.lupin.font.LupinFontRenderer;
import me.timeswitcher.lupin.main.Lupin;

public class FontManager {

    private static String fontName = "Verdana";

    public LupinFontRenderer verdana = new LupinFontRenderer();
    public LupinFontRenderer verdanaBold = new LupinFontRenderer();

    public static String getFontName() {
        return fontName;
    }

    public static void setFontName(String fontName) {
        FontManager.fontName = fontName;
        Lupin.instance.getFontManager().init();
    }

    public void init() {
        verdana.setFont(new Font(fontName, Font.PLAIN, 18), true);
        verdanaBold.setFont(new Font(fontName, Font.BOLD, 18), true);
    }
}