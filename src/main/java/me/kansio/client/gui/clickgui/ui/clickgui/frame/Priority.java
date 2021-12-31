package me.kansio.client.gui.clickgui.ui.clickgui.frame;

import me.kansio.client.utils.render.ColorPalette;

import java.awt.Color;

public interface Priority {
    int stringColor = -1;

    int defaultWidth = 125;
    int defaultHeight = 300;

    int enabledColor = ColorPalette.GREEN.getColor().getRGB();

    int mainColor = ColorPalette.DARK_GREY.getColor().getRGB();
    int darkerMainColor = new Color(mainColor).darker().getRGB();

    int outlineWidth = 1;
    int categoryNameHeight = 20;

    int moduleHeight = 15;

    boolean hoveredColor = false;
}
