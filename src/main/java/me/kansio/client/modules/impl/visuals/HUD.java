package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.clickgui.utils.render.RenderUtils;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

public class HUD extends Module {

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        mc.fontRendererObj.drawStringWithShadow("§bClient §7[1.0]", 4, 4, -1);

        int y = 3;

        for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj)) {
            if (!mod.isToggled()) continue;

            String name = mod.getName() + "§7" + mod.getSuffix();
            float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;
            Gui.drawRect(event.getSr().getScaledWidth() - 1.5, y - 2, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 3, -1);
            Gui.drawRect(xPos - 3, y - 2, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 3, new Color(0, 0, 0, 80).getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (1 + y), -1);
            y = y + 14;
        }

    }

}
