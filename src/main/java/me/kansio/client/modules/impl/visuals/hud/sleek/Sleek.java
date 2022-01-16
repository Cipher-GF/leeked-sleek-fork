package me.kansio.client.modules.impl.visuals.hud.sleek;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.math.BPSUtil;
import me.kansio.client.utils.network.UserUtil;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.ColorUtils;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Sleek extends HudMode {

    public Sleek() {
        super("Sleek");
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        HUD hud = (HUD) Client.getInstance().getModuleManager().getModuleByName("HUD");
        HUD.notifications = hud.noti.getValue() && hud.isToggled();

        int y = hud.arrayListY.getValue().intValue();
        int index = 0;

        ArrayList<Module> sorted = (ArrayList<Module>) Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj);
        sorted.removeIf(m -> !m.isToggled());

        if (hud.hideRender.getValue())
            sorted.removeIf(m -> m.getCategory() == ModuleCategory.VISUALS);

        for (Module mod : sorted) {
            if (!mod.isToggled()) continue;

            index++;

            Module lastModule = sorted.get(index - 1);

            Color color = ColorUtils.getColorFromHud(y);

            if (hud.watermark.getValue())
                mc.fontRendererObj.drawStringWithShadow(ChatUtil.translateColorCodes(hud.clientName.getValueAsString()), 4, 4, color.getRGB());

            String name = mod.getName() + "§7" + mod.getFormattedSuffix();
            String userinfo = "§7" + UserUtil.getBuildType(Integer.parseInt(Client.getInstance().getUid())) + " - §f" + Client.getInstance().getUid();
            float xPos = event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 6;

            Gui.drawRect(xPos - 1.5, y - 1, event.getSr().getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 80).getRGB());
            Gui.drawRect(xPos - 2.5, y - 1, xPos - 1.5, mc.fontRendererObj.FONT_HEIGHT + y + 1, color.getRGB());

            if (sorted.size() > index) {
                Module nextMod = sorted.get(index);

                String nextName = nextMod.getName() + "§7" + nextMod.getFormattedSuffix();
                float nextxPos = (float) (event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(nextName) - 7.5);

                Gui.drawRect(xPos - 2.5, mc.fontRendererObj.FONT_HEIGHT + y + 1, nextxPos, mc.fontRendererObj.FONT_HEIGHT + y + 2, color.getRGB());
            } else {
                Gui.drawRect(xPos - 2.5, mc.fontRendererObj.FONT_HEIGHT + y + 1, xPos + 100, mc.fontRendererObj.FONT_HEIGHT + y + 2, color.getRGB());
            }

            mc.fontRendererObj.drawStringWithShadow(name, (float) (xPos + 1.5), (float) (0.5 + y), color.getRGB());
            y = y + 11;

            mc.fontRendererObj.drawStringWithShadow(userinfo, event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(userinfo) - 2, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), -1);

            if (hud.bps.getValue()) {
                double bps = BPSUtil.getBPS();
                mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), ColorPalette.GREEN.getColor().getRGB());
            }

        }
    }
}