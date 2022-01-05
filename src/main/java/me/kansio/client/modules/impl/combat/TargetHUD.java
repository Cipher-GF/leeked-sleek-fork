package me.kansio.client.modules.impl.combat;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.utils.Util;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import optifine.MathUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public class TargetHUD extends Util {

    public static double currentHealthWidth =  (20 * 6.9);

    public static void draw(RenderOverlayEvent event, EntityLivingBase target) {
        KillAura killaura = (KillAura) Client.getInstance().getModuleManager().getModuleByName("KillAura");
        switch (killaura.targethudmode.getValue()) {
            case "Sleek": {
                float targetHealthWidth = (float) (target.getHealth() * 6.9);
                if (targetHealthWidth > 20 * 6.9) {
                    targetHealthWidth = (float) (20 * 6.9);
                    currentHealthWidth = 20 * 6.9;
                }

                if (targetHealthWidth > currentHealthWidth) {
                    currentHealthWidth += targetHealthWidth / 10;
                } else if (targetHealthWidth < currentHealthWidth) {
                    currentHealthWidth -= targetHealthWidth / 10;
                }
                RenderUtils.drawBorderedRoundedRect(150, 350, 150, 90, 10, 2, 2, new Color(target.hurtTime * 6, 0, 0, 100).getRGB());
                mc.fontRendererObj.drawStringWithShadow(target.getName(), 160, 365, -1);

                RenderUtils.drawBorderedRoundedRect(155, 390, (float) (target.getHealth() > 0 ? targetHealthWidth : 6.9), 15, 5, 0.5f, new Color(2).getRGB(), new Color(0, 255, 0, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Health: " + (int) target.getHealth(), 160, 393, -1);

                RenderUtils.drawBorderedRoundedRect(155, 410, (float) (target.getTotalArmorValue() > 0 ? target.getTotalArmorValue() * 6.9 : 6.9), 15, 5, 0.5f, new Color(2).getRGB(), new Color(0, 255, 255, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Armor: " + (int) target.getTotalArmorValue(), 160, 410, -1);
                break;
            }
            case "Moon": {
                float x = (event.getSr().getScaledWidth() >> 1) - 5;
                float y = (event.getSr().getScaledHeight() >> 1) + 120;
                if (target != null) {
                    if (mc.thePlayer != null && target instanceof EntityPlayer) {
                        NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                        final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");
                        final String playerName = "Name: " + net.minecraft.util.StringUtils.stripControlCodes(target.getName());
                        RenderUtils.drawBorderedRect(x, y, 140, 45, 0.5, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 90).getRGB());
                        RenderUtils.drawRect(x, y, 45, 45, new Color(0, 0, 0).getRGB());
                        Fonts.clickGuiSmallFont.drawStringWithShadow(playerName, x + 46.5, y + 4, -1);
                        Fonts.clickGuiSmallFont.drawStringWithShadow("Distance: " + MathUtils.round(mc.thePlayer.getDistanceToEntity(target), 2), x + 46.5, y + 12, -1);
                        Fonts.clickGuiSmallFont.drawStringWithShadow(ping, x + 46.5, y + 28, new Color(0x5D5B5C).getRGB());
                        Fonts.clickGuiSmallFont.drawStringWithShadow("Health: " + MathUtils.round((Float.isNaN(target.getHealth()) ? 20 : target.getHealth()) / 2, 2), x + 46.5, y + 20, getHealthColor(target));
                        drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) target);
                        RenderUtils.drawBorderedRect(x + 46, y + 45 - 10, 92, 8, 0.5, new Color(0).getRGB(), new Color(35, 35, 35).getRGB());
                        double inc = 91 / target.getMaxHealth();
                        double end = inc * (Math.min(target.getHealth(), target.getMaxHealth()));
                        RenderUtils.drawRect(x + 46.5, y + 45 - 9.5, end, 7, getHealthColor(target));
                    }
                }
                break;
            }
        }
    }

    private static int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }

    private static void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            mc.getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception ignored) {
        }
    }
}
