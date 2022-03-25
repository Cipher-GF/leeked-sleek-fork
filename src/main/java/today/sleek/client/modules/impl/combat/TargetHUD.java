package today.sleek.client.modules.impl.combat;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import optifine.MathUtils;
import org.lwjgl.opengl.GL11;
import today.sleek.Sleek;
import today.sleek.base.event.impl.RenderOverlayEvent;
import today.sleek.client.utils.Util;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.render.AnimationUtils;
import today.sleek.client.utils.render.ColorUtils;
import today.sleek.client.utils.render.RenderUtil;
import today.sleek.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.Objects;

import static net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect;

public class TargetHUD extends Util {

    public static double currentHealthWidth = (20 * 6.9);

    public static float animation = 0;

    public static void render(RenderOverlayEvent event, EntityLivingBase target, double x, double y) {
        KillAura killaura = (KillAura) Sleek.getInstance().getModuleManager().getModuleByName("KillAura");
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

                //Draw the background with the hurttime animation
                RenderUtils.drawBorderedRoundedRect(150, 350, 150, 60, 10, 2, 2, new Color(target.hurtTime * 6, 0, 0, 100).getRGB());

//                mc.fontRendererObj.drawStringWithShadow(target.getName(), 210, 370, -1);
                NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                Fonts.Verdana.drawString("Name: " + target.getName(), 205, 361, -1);
                final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");
                Fonts.Verdana.drawString("Distance:", 205, 371, -1);
                Fonts.Verdana.drawString(" " + MathUtils.round(mc.thePlayer.getDistanceToEntity(target), 2), 250, 371, -1);
                Fonts.Verdana.drawString(ping, 205, 381, -1);

                if (target instanceof EntityPlayer) {
                    switch (killaura.targetHudPreview.getValue()) {
                        case "Face": {
                            ResourceLocation skin = ((AbstractClientPlayer) target).getLocationSkin();
                            RenderUtils.drawFace(skin, 165, 360, 35, 35);
                            break;
                        }
                        case "Model": {
                            GuiInventory.drawEntityOnScreen(175, 397, 20, 0, 0, target);
                            break;
                        }
                    }
                }

                RenderUtils.drawBorderedRoundedRect(155, 400, (float) (20 * 6.9), 5, 5, 0.5f, new Color(40, 40, 40, 255).getRGB(), new Color(45, 45, 45, 255).getRGB());
                RenderUtils.drawBorderedRoundedRect(155, 400, (float) (target.getHealth() > 0 ? targetHealthWidth : 6.9), 5, 5, 0.5f, ColorUtils.getColorFromHud(1).getRGB(), ColorUtils.getColorFromHud(1).getRGB());
                break;
            }
            case "Flux": {
                render(target, 155, 400);
                break;
            }
            case "Exhi": {

            }
        }
    }


    public static void render(EntityLivingBase ent, float x, float y) {

        Color color;

        GL11.glPushMatrix();
        String playerName = ent.getName();

        String healthStr = Math.round(ent.getHealth() * 10) / 10d + " hp";
        float width = Math.max(75, mc.fontRendererObj.getStringWidth(playerName) + 25);

        //更改TargetHUD在屏幕坐标的初始位置
        GL11.glTranslatef(x, y, 0);
        RenderUtils.drawRoundedRect(0, 0, 58 + width, 28, 2, RenderUtil.reAlpha(0xff000000, 0.6f), 1, RenderUtil.reAlpha(0xff000000, 0.5f));

        mc.fontRendererObj.drawString(playerName, 30, 3, -1);
        mc.fontRendererObj.drawString(healthStr, (int) (52 + width - mc.fontRendererObj.getStringWidth(healthStr) - 2), 4, 0xffcccccc);

        boolean isNaN = Float.isNaN(ent.getHealth());
        float health = isNaN ? 20 : ent.getHealth();
        float maxHealth = isNaN ? 20 : ent.getMaxHealth();
        float healthPercent = MathUtils.clampValue(health / maxHealth, 0, 1);

        int hue = (int) (healthPercent * 120);
        color = Color.getHSBColor(hue / 360f, 0.7f, 1f);

        //RenderUtil.drawRect(37, 14.5f, 26 + width - 2, 17.5f, RenderUtil.reAlpha(1, 0.35f));

        float barWidth = (26 + width - 2) - 37;
        float drawPercent = 37 + (barWidth / 100) * (healthPercent * 100);

        if (animation <= 0) {
            animation = drawPercent;
        }

        if (ent.hurtTime <= 6) {
            animation = AnimationUtils.getAnimationState(animation, drawPercent, (float) Math.max(10, (Math.abs(animation - drawPercent) * 30) * 0.4));
        }

        RenderUtil.drawRect(30, 15.5f, animation, 4.5f, color.darker().getRGB());
        RenderUtil.drawRect(30, 15.5f, drawPercent, 4.5f, color.getRGB());

        //FontManager.icon10.drawString("s", 30f, 13, ColorUtils.WHITE.c);
        //FontManager.icon10.drawString("r", 30f, 20, ColorUtils.WHITE.c);

        float f3 = 37 + (barWidth / 100f) * (ent.getTotalArmorValue() * 5);
        //RenderUtil.drawRect(37, 21.5f, 40 + width - 2, 24.5f, RenderUtil.reAlpha(1, 0.35f));
        RenderUtil.drawRect(30, 21.5f, f3, 4.5f, 0xff4286f5);

        RenderUtils.rectangleBordered(1.5f, 1.5f, 26.5f, 26.5f, 0.5f, 0x00000000, Color.GREEN.getRGB());
        GlStateManager.resetColor();
        for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.getNetHandler().getPlayerInfoMap())) {
            if (mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) == ent) {
                mc.getTextureManager().bindTexture(info.getLocationSkin());
                drawScaledCustomSizeModalRect(2f, 2f, 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);

                //drawScaledCustomSizeModalRect(2f, 2f, 40.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);

                GlStateManager.bindTexture(0);
                break;
            }
        }
        GL11.glPopMatrix();
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
            drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception ignored) {
        }
    }
}