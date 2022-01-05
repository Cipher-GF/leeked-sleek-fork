package me.kansio.client.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Fonts {
    public static final MCFontRenderer MainMenuTitle = new MCFontRenderer(new Font("Arial", Font.BOLD,30),true,true);
    public static final MCFontRenderer clickGuiFont = new MCFontRenderer(new Font("Arial", Font.PLAIN,18),true,true);
    public static final MCFontRenderer clickGuiSmallFont = new MCFontRenderer(new Font("Arial", Font.PLAIN,12),true,true);
    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
