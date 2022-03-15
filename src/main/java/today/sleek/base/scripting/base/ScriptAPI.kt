package today.sleek.base.scripting.base

import today.sleek.base.scripting.base.ScriptMod
import com.google.common.eventbus.Subscribe
import today.sleek.Sleek
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.client.modules.impl.Module

object ScriptAPI {
    fun registerModule(name: String?): ScriptMod {
        val mod = ScriptMod(name, "Test...")
        Sleek.getInstance().moduleManager.registerModule(object : Module(name, ModuleCategory.SCRIPT) {
            override fun onEnable() {
                mod.onEnable()
            }

            override fun onDisable() {
                mod.onDisable()
            }

            @Subscribe
            fun onUpdate(event: UpdateEvent?) {
                mod.onEvent("update", event)
            }
        })
        mod.onLoad()
        return mod
    }
}