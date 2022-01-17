package me.kansio.client.modules.impl.visuals.hud.intent;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Comparator;

public class IntentBase extends HudMode {

    public IntentBase() {
        super("Intent");
    }

    @Override
    public void onRenderOverlay(RenderOverlayEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fr = mc.fontRendererObj;

        Client.getInstance().getModuleManager().getModules().sort(Comparator.comparingInt(m ->
                        mc.fontRendererObj.getStringWidth(((Module) m).getName()))
                .reversed()
        );

        GlStateManager.pushMatrix();
        GlStateManager.translate(4, 4, 0);
        GlStateManager.scale(2, 2, 1);
        GlStateManager.translate(-4, -4, 0);
        fr.drawString(ChatUtil.translateColorCodes(getHud().clientName.getValue()), 4, 4, ColorUtils.getColorFromHud(1).getRGB());
        GlStateManager.popMatrix();

        int count = 0;

        for (Module m : Client.getInstance().getModuleManager().getModules()) {
            if (!m.isToggled()) {
                continue;
            }

            double offset = count * (fr.FONT_HEIGHT + 6);

            Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 10, offset, sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 8, 6 + fr.FONT_HEIGHT + offset, ColorUtils.getColorFromHud(count).getRGB());
            Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 8, offset, sr.getScaledWidth(), 6 + fr.FONT_HEIGHT + offset, 0x90000000);
            fr.drawStringWithShadow(m.getName(), sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 4, (float) (4 + offset), ColorUtils.getColorFromHud(count).getRGB());

            count++;
        }
    }
}
