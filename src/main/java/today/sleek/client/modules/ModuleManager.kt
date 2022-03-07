package today.sleek.client.modules

import java.lang.IllegalAccessException
import today.sleek.client.utils.java.ReflectUtils
import net.minecraft.client.gui.FontRenderer
import today.sleek.Sleek
import today.sleek.client.utils.font.MCFontRenderer
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.value.Value
import today.sleek.client.modules.impl.Module
import java.lang.Exception
import java.util.*

class ModuleManager {
    val modules = ArrayList<Module>()
    fun registerModule(module: Module) {
        modules.add(module)
        for (field in module.javaClass.declaredFields) {
            try {
                field.isAccessible = true
                val obj = field[module]
                if (obj is Value<*>) Collections.addAll(Sleek.getInstance().valueManager.objects, obj)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        //Collections.addAll(Client.getInstance().getValueManager().getObjects(), values);
    }

    fun reloadModules() {
        for (mod in modules) {
            if (mod.isToggled) mod.toggle()
        }
        modules.clear()
        //load them using reflections
        for (mod in ReflectUtils.getReflects(this.javaClass.getPackage().name + ".impl", Module::class.java)) {
            try {
                val module = mod.getDeclaredConstructor().newInstance() as Module
                registerModule(module)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getModulesSorted(customFontRenderer: FontRenderer): List<Module> {
        val moduleList: MutableList<Module> = ArrayList(modules)
        moduleList.removeIf { obj: Module -> obj.isHidden }
        moduleList.sortWith { a: Module, b: Module ->
            val dataA = if (a.formattedSuffix == null) "" else a.formattedSuffix
            val dataB = if (b.formattedSuffix == null) "" else b.formattedSuffix
            val nameA = a.name
            val nameB = b.name
            customFontRenderer.getStringWidth(nameB + dataB) - customFontRenderer.getStringWidth(nameA + dataA)
        }
        return moduleList
    }

    fun getModulesSorted(customFontRenderer: MCFontRenderer): List<Module> {
        val moduleList: MutableList<Module> = ArrayList(modules)
        moduleList.removeIf { obj: Module -> obj.isHidden }
        moduleList.sortWith { a: Module, b: Module ->
            val dataA = if (a.formattedSuffix == null) "" else a.formattedSuffix
            val dataB = if (b.formattedSuffix == null) "" else b.formattedSuffix
            val nameA = a.name
            val nameB = b.name
            customFontRenderer.getStringWidth(nameB + dataB) - customFontRenderer.getStringWidth(nameA + dataA)
        }
        return moduleList
    }

    fun sort(fontRenderer: FontRenderer) {
        modules.sortWith { a: Module, b: Module ->
            val dataA = if (a.formattedSuffix == null) "" else a.formattedSuffix
            val dataB = if (b.formattedSuffix == null) "" else b.formattedSuffix
            val first = fontRenderer.getStringWidth(a.name + dataA)
            val second = fontRenderer.getStringWidth(b.name + dataB)
            second - first
        }
    }

    fun <T : Module?> getModuleByClass(clazz: Class<out T>): T {
        return modules.stream().filter { element: Module -> element.javaClass == clazz }
            .findFirst()
            .orElseThrow { NoSuchElementException("RETARD ALERT: Element belonging to class \'" + clazz.name + "\' not found") } as T
    }

    fun getModuleByName(name: String?): Module? {
        for (module in modules) {
            if (module.name.equals(name, ignoreCase = true)) {
                return module
            }
        }
        return null
    }

    fun getModuleByNameIgnoreSpace(name: String?): Module? {
        for (module in modules) {
            if (module.name.replace(" ".toRegex(), "").equals(name, ignoreCase = true)) {
                return module
            }
        }
        return null
    }

    fun getModulesFromCategory(category: ModuleCategory): List<Module> {
        val mods = ArrayList<Module>()
        for (module in modules) {
            if (module.category == category) {
                mods.add(module)
            }
        }
        return mods
    }

    init {
        for (mod in ReflectUtils.getReflects(this.javaClass.getPackage().name + ".impl", Module::class.java)) {
            try {
                val module = mod.getDeclaredConstructor().newInstance() as Module
                registerModule(module)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}