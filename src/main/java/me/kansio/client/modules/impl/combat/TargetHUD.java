package me.kansio.client.modules.impl.combat;

import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.utils.Util;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class TargetHUD extends Util {

    public static void draw(RenderOverlayEvent event, EntityLivingBase target) {
        RenderUtils.drawBorderedRoundedRect(150, 350, 150, 90, 10, 2, 2, new Color(target.hurtTime * 6, 0, 0, 100).getRGB());

        RenderUtils.drawBorderedRoundedRect(155, 390, (float) (target.getHealth() * 6.9), 15, 5, 2, 2, new Color(0, 255, 0, 255).getRGB());
        RenderUtils.drawBorderedRoundedRect(155, 410, (float) (target.getTotalArmorValue() * 6.9), 15, 5, 2, 2, new Color(0, 255, 255, 255).getRGB());
        //RenderUtils.drawBorderedRoundedRect(150, 350, 150, 90, 10, 2, 2, new Color(0, 255, 255, 255).getRGB());

        mc.fontRendererObj.drawStringWithShadow(target.getName(), 160, 365, -1);
        mc.fontRendererObj.drawStringWithShadow("Health: " + target.getHealth(), 160, 393, -1);
        mc.fontRendererObj.drawStringWithShadow("Armor: " + target.getTotalArmorValue(), 16, 410, -1);
    }
}
