package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.RenderEvent2;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@ModuleData(
        name = "Image ESP",
        description = "Displays images",
        category = ModuleCategory.VISUALS
)
public class MemeESP extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Zuiy", "Zuiy2", "Floyd");


    @Subscribe
    public void onEntityRender(RenderEvent2 event) {

        for (final EntityPlayer p : mc.thePlayer.getEntityWorld().playerEntities) {
            if (RenderUtils.isInViewFrustrum(p) && !p.isInvisible() && p.isEntityAlive()) {
                if (p == mc.thePlayer) {
                    continue;
                }
            }

            final double x = RenderUtils.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
            final double y = RenderUtils.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
            final double z = RenderUtils.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
            GlStateManager.pushMatrix();
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glDisable(2929);
            final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
            final double scale = 0.005 * distance;
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(-0.1, -0.1, 0.0);
            switch (modeValue.getValue()) {
                case "Zuiy":
                    //Minecraft.getMinecraft().getTextureManager().bindTexture(this.roblox);
                    break;
            }

            Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, 1, 1, (int) (252.0 * (scale / 2.0)), (int) (476.0 * (scale / 2.0)), 1.0f, 1.0f);
            GL11.glEnable(2929);
            GlStateManager.popMatrix();
        }
    }
}
