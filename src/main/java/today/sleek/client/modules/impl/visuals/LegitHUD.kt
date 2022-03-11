package today.sleek.client.modules.impl.visuals

import com.google.common.eventbus.Subscribe
import today.sleek.base.event.impl.RenderOverlayEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.modules.ModuleData
import today.sleek.base.value.value.BooleanValue
import today.sleek.base.value.value.NumberValue
import today.sleek.client.modules.impl.Module


/**
 * @author Kansio
 */

@ModuleData(
    name = "Legit HUD",
    category = ModuleCategory.VISUALS,
    description = "''Legit'' looking hud"
)
class LegitHUD : Module() {

    private val toggleSneak = BooleanValue("Toggle Sneak", this, true)
    private val tsX = NumberValue("Toggle Sneak X", this, 5, 0, 3000, 1, toggleSneak)
    private val tsY = NumberValue("Toggle Sneak Y", this, 5, 0, 3000, 1, toggleSneak)

    @Subscribe
    fun onRender(event: RenderOverlayEvent) {
        if (toggleSneak.value) {
            mc.fontRendererObj.drawStringWithShadow("[Sprinting (Toggled)]", tsX.value.toFloat(), tsY.value.toFloat(), -1)
        }
    }

}