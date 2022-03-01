package me.kansio.client.modules.impl.visuals.hud.watermark

import me.kansio.client.Client
import me.kansio.client.modules.impl.visuals.hud.WaterMarkMode
import me.kansio.client.event.impl.RenderOverlayEvent
import me.kansio.client.utils.network.UserUtil
import me.kansio.client.utils.server.ServerUtil
import net.minecraft.client.Minecraft

/**
 * @author Kansio
 */
class Simple : WaterMarkMode("Simple") {

    override fun onRenderOverlay(event: RenderOverlayEvent) {
        mc.fontRendererObj.drawStringWithShadow("§7" + hud.clientName.value + " §f- §d(${UserUtil.getBuildType()}) §7Build",
            4.0F, 4.0F, -1)
        mc.fontRendererObj.drawStringWithShadow("§7FPS: ${Minecraft.getDebugFPS()}",
            4.0F, 14.0F, -1)
    }
}