package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.clickgui.utils.render.RenderUtils;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

public class HUD extends Module {

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        mc.fontRendererObj.drawStringWithShadow("§aSleek v0.1", 4, 4, ColorPalette.GREEN.getColor().getRGB());

        int y = 4;

        for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj)) {
            if (!mod.isToggled()) continue;

            Color color = ColorUtil.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);

            String name = mod.getName() + "§7" + mod.getSuffix();
            float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;
            Gui.drawRect(xPos - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 80).getRGB());
            Gui.drawRect(event.getSr().getScaledWidth() - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, color.getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
            y = y + 11;
        }

    }

}
