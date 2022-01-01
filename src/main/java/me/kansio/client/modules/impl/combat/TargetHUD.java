package me.kansio.client.modules.impl.combat;

import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.utils.Util;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;

public class TargetHUD extends Util {

    public static void draw(RenderOverlayEvent event, EntityLivingBase target) {
        RenderUtils.drawBorderedRoundedRect(150, 350, 150, 90, 10, 2, 2, 0x80000000
        );
    }
}
