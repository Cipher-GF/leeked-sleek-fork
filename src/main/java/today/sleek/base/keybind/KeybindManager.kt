package today.sleek.base.keybind

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import today.sleek.Sleek
import today.sleek.client.modules.impl.Module
import java.io.*
import java.lang.Exception
import java.util.function.Consumer

class KeybindManager(dir: File?) {

    private val keybindFile: File

    fun load() {
        try {
            if (!keybindFile.exists()) {
                keybindFile.createNewFile()
                return
            }
            val reader: Reader = FileReader(keybindFile)
            val node = JsonParser().parse(reader)
            if (!node.isJsonArray) {
                return
            }
            val arr = node.asJsonArray
            arr.forEach(Consumer { element: JsonElement ->
                val obj = element.asJsonObject
                val modName = obj["name"].asString
                val m = Sleek.getInstance().moduleManager.getModuleByName(modName)
                if (m != null) {
                    if (obj["keybind"] != null) {
                        m.keyBind = obj["keybind"].asInt
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            val arr = JsonArray()
            Sleek.getInstance().moduleManager.modules.forEach(Consumer { module: Module -> arr.add(module.saveKeybind()) })
            val writer: Writer = FileWriter(keybindFile)
            val json = GsonBuilder().setPrettyPrinting().create().toJson(arr)
            println(json)
            writer.write(json)
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        keybindFile = File(dir, "keybinds.json")
        load()
    }
}