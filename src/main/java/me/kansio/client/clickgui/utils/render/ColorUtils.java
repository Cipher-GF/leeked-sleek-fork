package me.kansio.client.clickgui.utils.render;

import java.awt.Color;

public class ColorUtils {
    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}