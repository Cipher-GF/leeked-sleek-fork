package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;

public class HUD extends Module {

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        mc.fontRendererObj.drawStringWithShadow("hack", 2, 2, -1);

        int y = 22;

        for (Module mod : Client.getInstance().getModuleManager().getModules()) {
            if (mod.isToggled()) {
                mc.fontRendererObj.drawStringWithShadow(mod.getName(), 2, 2 + y, -1);
                y = y + 10;
            }
        }

    }

}
