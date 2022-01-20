package me.kansio.client.modules.impl.visuals.hud.watermark;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.modules.impl.visuals.hud.WaterMarkMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.render.ColorUtils;

import java.awt.*;

public class Sleek extends WaterMarkMode {

    public Sleek() {
        super("Sleek");
    }

    @Override
    public void onRenderOverlay(RenderOverlayEvent event) {
        HUD hud = (HUD) Client.getInstance().getModuleManager().getModuleByName("HUD");

        int y = hud.arrayListY.getValue().intValue();
        Color color = ColorUtils.getColorFromHud(y);
        mc.fontRendererObj.drawStringWithShadow(ChatUtil.translateColorCodes(getHud().clientName.getValueAsString()), 4, 4, color.getRGB());
    }
}
