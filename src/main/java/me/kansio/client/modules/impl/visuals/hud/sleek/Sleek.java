package me.kansio.client.modules.impl.visuals.hud.sleek;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
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

        int y = 4;
        int index = 0;

        ArrayList<Module> sorted = (ArrayList<Module>) Client.getInstance().getModuleManager().getModulesSorted(mc.fontRendererObj);
        sorted.removeIf(m -> !m.isToggled());

        if (hud.font.getValue()) {
            for (Module mod : Client.getInstance().getModuleManager().getModulesSorted(Fonts.Verdana)) {
                if (!mod.isToggled()) continue;

                Color color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / Fonts.Verdana.getHeight() * 9.95);

                switch (((HUD) Client.getInstance().getModuleManager().getModuleByName("HUD")).getColorMode().getValue()) {
                    case "Sleek": {
                        color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / Fonts.Verdana.getHeight() * 9.95);
                        break;
                    }
                    case "Nitrogen": {
                        color = ColorUtils.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D));
                        break;
                    }
                    case "Rainbow": {
                        color = new Color(ColorUtils.getRainbow(6000, 0));
                        break;
                    }
                    case "Astolfo": {
                        color = ColorUtils.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D));
                        break;
                    }
                }

                Fonts.HUD.drawStringWithShadow(ChatUtil.translateColorCodes(hud.clientName.getValueAsString()), 4, 4, color.getRGB());

                String name = mod.getName() + "§7" + mod.getFormattedSuffix();
                String userinfo = "§7" + UserUtil.getBuildType(Integer.parseInt(Client.getInstance().getUid())) + " - §f" + Client.getInstance().getUid();
                float xPos = event.getSr().getScaledWidth() - Fonts.Verdana.getStringWidth(name) - 6;
                Gui.drawRect(xPos - 1.5, y - 3, event.getSr().getScaledWidth(), Fonts.Verdana.getHeight() + y + 1, new Color(0, 0, 0, 80).getRGB());
                Gui.drawRect(event.getSr().getScaledWidth() - 1, y - 3, event.getSr().getScaledWidth(), Fonts.Verdana.getHeight() + y + 1, color.getRGB());



                Fonts.HUD.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
                y = y + 11;

                Fonts.HUD.drawStringWithShadow(userinfo, event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(userinfo) - 2, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), -1);

                if (hud.bps.getValue()) {
                    double bps = BPSUtil.getBPS();
                    Fonts.HUD.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 22 : 10), ColorPalette.GREEN.getColor().getRGB());
                }

            }

        } else {
            for (Module mod : sorted) {
                if (!mod.isToggled()) continue;

                index++;

                Module lastModule = sorted.get(index - 1);

                Color color = null;

                switch (((HUD) Client.getInstance().getModuleManager().getModuleByName("HUD")).getColorMode().getValue()) {
                    case "Sleek": {
                        color = ColorUtils.getGradientOffset(new Color(0, 255, 128), new Color(212, 1, 1), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                        break;
                    }
                    case "Nitrogen": {
                        color = ColorUtils.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                        break;
                    }
                    case "Rainbow": {
                        color = new Color(ColorUtils.getRainbow(6000, y * 15));
                        break;
                    }
                    case "Astolfo": {
                        color = ColorUtils.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                        break;
                    }
                }

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

                mc.fontRendererObj.drawStringWithShadow(name, xPos, (float) (0.5 + y), color.getRGB());
                y = y + 11;

                mc.fontRendererObj.drawStringWithShadow(userinfo, event.getSr().getScaledWidth() - mc.fontRendererObj.getStringWidth(userinfo) - 2, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), -1);

                if (hud.bps.getValue()) {
                    double bps = BPSUtil.getBPS();
                    mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + new DecimalFormat("0.##").format(bps), 3, event.getSr().getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10), ColorPalette.GREEN.getColor().getRGB());
                }

            }
        }

    }
}