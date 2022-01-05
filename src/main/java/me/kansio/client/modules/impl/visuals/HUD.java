package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.math.BPSUtil;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.text.DecimalFormat;

public class HUD extends Module {

    private BooleanValue font = new BooleanValue("Font", this, false);
    private BooleanValue noti = new BooleanValue("Notifications", this, true);
    private BooleanValue bps = new BooleanValue("BPS", this, true);

    public static boolean notifications;

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
        register(noti, font, bps);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        notifications = noti.getValue() && isToggled();

        mc.fontRendererObj.drawStringWithShadow("§aS§7leek v0.1", 4, 4, ColorPalette.GREEN.getColor().getRGB());

        if (bps.getValue()) {
            double bps = BPSUtil.getBPS();
                mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 32 : 20), ColorPalette.GREEN.getColor().getRGB());
        }
        int y = 4;

        if (font.getValue()) {
            for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(Fonts.clickGuiFont)) {
                if (!mod.isToggled()) continue;

                Color color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);


                String name = mod.getName() + "§7" + mod.getFormattedSuffix();
                float xPos = event.getSr().getScaledWidth() - Fonts.clickGuiFont.getStringWidth(name) - 6;
                Gui.drawRect(xPos - 1.5, y - 3, event.getSr().getScaledWidth(), Fonts.clickGuiFont.getHeight() + y + 1, new Color(0, 0, 0, 80).getRGB());
                Gui.drawRect(event.getSr().getScaledWidth() - 1, y - 3, event.getSr().getScaledWidth(), Fonts.clickGuiFont.getHeight() + y + 1, color.getRGB());
                Fonts.clickGuiFont.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
                y = y + 11;
            }

        } else {
            for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj)) {
                if (!mod.isToggled()) continue;

                Color color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);


                String name = mod.getName() + "§7" + mod.getFormattedSuffix();
                float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;
                Gui.drawRect(xPos - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 80).getRGB());
                Gui.drawRect(event.getSr().getScaledWidth() - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, color.getRGB());
                mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
                y = y + 11;
            }
        }

    }

}
