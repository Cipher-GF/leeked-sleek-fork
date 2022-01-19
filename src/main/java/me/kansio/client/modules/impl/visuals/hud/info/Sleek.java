package me.kansio.client.modules.impl.visuals.hud.info;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.modules.impl.visuals.hud.InfoMode;
import me.kansio.client.utils.math.BPSUtil;
import me.kansio.client.utils.network.UserUtil;
import me.kansio.client.utils.render.ColorPalette;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;

public class Sleek extends InfoMode {
    public Sleek() {
        super("Sleek");
    }

    @Override
    public void onRenderOverlay(RenderOverlayEvent event) {
        HUD hud = (HUD) Client.getInstance().getModuleManager().getModuleByName("HUD");
        String userinfo = "§7" + UserUtil.getBuildType(Integer.parseInt(Client.getInstance().getUid())) + " - §f" + Client.getInstance().getUid();
        mc.fontRendererObj.drawStringWithShadow(userinfo, event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(userinfo) - 2, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), -1);
        if (hud.bps.getValue()) {
            double bps = BPSUtil.getBPS();
            mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), ColorPalette.GREEN.getColor().getRGB());
        }
    }

}
