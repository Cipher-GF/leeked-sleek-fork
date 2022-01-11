package me.kansio.client.modules.impl.visuals.hud.vital;

import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import oshi.SystemInfo;
import oshi.hardware.Processor;

import java.awt.*;

public class Vital extends HudMode {
    public Vital() {
        super("Vital");
    }

    @Override
    public void onRenderOverlay(RenderOverlayEvent event) {
        int color = (new Color(87, 124, 255)).getRGB();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.5D, 2.5D, 2.5D);
        mc.fontRendererObj.drawStringWithShadow("S", 1.0F, 0.F, color);
        GlStateManager.popMatrix();
        mc.fontRendererObj.drawStringWithShadow("leek", 18.0F, 12.0F, color);
        mc.fontRendererObj.drawStringWithShadow("fps: " + Minecraft.getDebugFPS(), 1.0F, 22.0F, color);
        mc.fontRendererObj.drawStringWithShadow("gpu: " + GL11.glGetString(7937), 1.0F, 32.0F, color);
        Processor[] aprocessor = (new SystemInfo()).getHardware().getProcessors();
        mc.fontRendererObj.drawStringWithShadow("cpu: " + aprocessor[0].toString(), 1.0F, 42.0F, color);
    }
}
