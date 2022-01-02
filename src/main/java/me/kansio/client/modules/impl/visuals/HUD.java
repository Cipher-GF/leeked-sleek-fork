package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
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

    @Getter
    private BooleanValue targetHud = new BooleanValue("Target HUD", this, true);

    private BooleanValue bps = new BooleanValue("BPS", this, true);
    private BooleanValue bpsToMph = new BooleanValue("BPS to MPH", this, true, bps);

    public static boolean notifications;

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
        register(noti, font, bps, bpsToMph);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        notifications = noti.getValue() && isToggled();
        mc.fontRendererObj.drawStringWithShadow("§aS§7leek v0.1", 4, 4, ColorPalette.GREEN.getColor().getRGB());
        if (bps.getValue()) {
            double bps = BPSUtil.getBPS();
            if (bpsToMph.getValue()) {
                bps /= 2.237;
                mc.fontRendererObj.drawStringWithShadow("MPH: " + EnumChatFormatting.GRAY + new DecimalFormat("0.###").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 32 : 20), ColorPalette.GREEN.getColor().getRGB());
            } else {
                mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 32 : 20), ColorPalette.GREEN.getColor().getRGB());
            }
        }
        int y = 4;

        for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj)) {
            if (!mod.isToggled()) continue;

            Color color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);

            String name = mod.getName() + "§7" + mod.getSuffix();
            float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;
            Gui.drawRect(xPos - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 80).getRGB());
            Gui.drawRect(event.getSr().getScaledWidth() - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, color.getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
            y = y + 11;
        }

    }

}
