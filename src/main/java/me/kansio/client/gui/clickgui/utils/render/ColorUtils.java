package me.kansio.client.gui.clickgui.utils.render;

import java.awt.*;

public class ColorUtils {
    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}