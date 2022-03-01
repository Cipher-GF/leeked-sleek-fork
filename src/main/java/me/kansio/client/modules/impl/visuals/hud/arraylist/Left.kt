package me.kansio.client.modules.impl.visuals.hud.arraylist

import me.kansio.client.Client
import me.kansio.client.event.impl.RenderOverlayEvent
import me.kansio.client.modules.api.ModuleCategory
import me.kansio.client.modules.impl.Module
import me.kansio.client.modules.impl.visuals.HUD
import me.kansio.client.modules.impl.visuals.hud.ArrayListMode
import me.kansio.client.utils.font.Fonts
import me.kansio.client.utils.render.ColorUtils
import net.minecraft.client.gui.Gui
import java.awt.Color


/**
 * @author Kansio
 */
class Left : ArrayListMode("Left") {

    override fun onRenderOverlay(event: RenderOverlayEvent) {
        val hud = hud
        HUD.notifications = hud.noti.value && hud.isToggled
        var y = hud.arrayListY.value.toInt()
        var index = 0
        var color: Color
        val sorted = Client.getInstance().moduleManager.getModulesSorted(mc.fontRendererObj) as ArrayList<Module>
        sorted.removeIf { m: Module -> !m.isToggled }

        if (hud.hideRender.value)
            sorted.removeIf { m: Module -> m.category == ModuleCategory.VISUALS }

        for (mod in sorted) {
            if (!mod.isToggled) continue
            index++
            color = ColorUtils.getColorFromHud(y)
            val name = mod.name + "§f" + mod.formattedSuffix

            mc.fontRendererObj.drawStringWithShadow(name, getHud().arrayListX.value.toFloat(), (0.5 + y).toFloat(), color.rgb)
            y += 11
        }
    }
}