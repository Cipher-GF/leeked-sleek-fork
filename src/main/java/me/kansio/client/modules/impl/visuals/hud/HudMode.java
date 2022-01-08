package me.kansio.client.modules.impl.visuals.hud;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.utils.Util;

public abstract class HudMode extends Util {

    private final String name;

    public HudMode(String name) {
        this.name = name;
    }

    public void onRenderOverlay(RenderOverlayEvent event) {}
    public void onEnable() {}
    public void onDisable() {}

    public String getName() {
        return name;
    }

    public HUD getSpeed() {
        return (HUD) Client.getInstance().getModuleManager().getModuleByName("Hud");
    }


}
