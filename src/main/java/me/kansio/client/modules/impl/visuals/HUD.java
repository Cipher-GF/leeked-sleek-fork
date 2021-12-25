package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.clickgui.utils.render.RenderUtils;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

public class HUD extends Module {

    private StringValue name = new StringValue("Client Name", this, "Sleek");

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        int y = 4;
        Color color = ColorUtil.getGradientOffset(new Color(255, 0, 77), new Color(184, 0, 145), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);

        mc.fontRendererObj.drawStringWithShadow("Sleek", 4, 4, color.getRGB());

        for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj)) {
            if (!mod.isToggled()) continue;

            String name = mod.getName() + "§7" + mod.getSuffix();
            float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;
            Gui.drawRect(xPos - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 80).getRGB());
            Gui.drawRect(event.getSr().getScaledWidth() - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, color.getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
            y = y + 11;
        }

    }

}
