package me.kansio.client.utils.font;

import me.kansio.client.utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Fonts extends Util {
    public static final MCFontRenderer Arial30 = new MCFontRenderer(new Font("Arial", Font.BOLD,30),true,true);
    public static final MCFontRenderer Verdana = new MCFontRenderer(new Font("Verdana", Font.PLAIN,18),true,true);
    public static final MCFontRenderer Arial12 = new MCFontRenderer(new Font("Arial", Font.PLAIN,12),true,true);
    public static final MCFontRenderer HUD = new MCFontRenderer(new Font("Arial", Font.PLAIN,18),true,true);

    public static final MCFontRenderer SEGOE18 = new MCFontRenderer(new Font("Tahoma", Font.PLAIN,18),true,true);
    public static final MCFontRenderer SEGOE12 = new MCFontRenderer(new Font("Tahoma", Font.PLAIN,12),true,true);

    public static final MCFontRenderer NotifIcon = new MCFontRenderer(fontFromTTF(new ResourceLocation("sleek/fonts/notif-icon.ttf"),20,0), true, true);
    public static final MCFontRenderer UbuntuLight = new MCFontRenderer(fontFromTTF(new ResourceLocation("sleek/fonts/Ubuntu-Light.ttf"),20,0), true, true);
    public static final MCFontRenderer UbuntuMedium = new MCFontRenderer(fontFromTTF(new ResourceLocation("sleek/fonts/Ubuntu-Medium.ttf"),20,0), true, true);


    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, mc.getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
