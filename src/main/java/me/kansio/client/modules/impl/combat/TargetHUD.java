package me.kansio.client.modules.impl.combat;

import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;
import me.kansio.client.utils.Util;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class TargetHUD extends Util {

    public static double currentHealthWidth = (20 * 6.9);

    public static void draw(RenderOverlayEvent event, EntityLivingBase target) {
        KillAura killaura = (KillAura) Client.getInstance().getModuleManager().getModuleByName("KillAura");
        switch (killaura.targethudmode.getValue()) {
            case "Sleek":
                float targetHealthWidth = (float) (target.getHealth() * 6.9);
                if (targetHealthWidth > 20 * 6.9) {
                    targetHealthWidth = (float) (20 * 6.9);
                    currentHealthWidth = 20 * 6.9;
                }

                if (targetHealthWidth > currentHealthWidth) {
                    currentHealthWidth += targetHealthWidth / 10;
                } else if (targetHealthWidth < currentHealthWidth) {
                    currentHealthWidth -= targetHealthWidth / 10;
                }
                RenderUtils.drawBorderedRoundedRect(150, 350, 150, 90, 10, 2, 2, new Color(target.hurtTime * 6, 0, 0, 100).getRGB());
                mc.fontRendererObj.drawStringWithShadow(target.getName(), 160, 365, -1);

                RenderUtils.drawBorderedRoundedRect(155, 390, (float) (target.getHealth() > 0 ? targetHealthWidth : 6.9), 15, 5, 0.5f, new Color(2).getRGB(), new Color(0, 255, 0, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Health: " + (int) target.getHealth(), 160, 393, -1);

                RenderUtils.drawBorderedRoundedRect(155, 410, (float) (target.getTotalArmorValue() > 0 ? target.getTotalArmorValue() * 6.9 : 6.9), 15, 5, 0.5f, new Color(2).getRGB(), new Color(0, 255, 255, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Armor: " + (int) target.getTotalArmorValue(), 160, 410, -1);
                break;
            case "Moon":
                break;
        }
    }
}
