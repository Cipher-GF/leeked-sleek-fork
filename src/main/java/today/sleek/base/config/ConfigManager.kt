package today.sleek.base.config

import com.google.gson.*
import java.util.concurrent.CopyOnWriteArrayList
import today.sleek.base.config.ConfigManager
import org.apache.commons.io.FilenameUtils
import sun.misc.Unsafe
import today.sleek.Sleek
import today.sleek.client.gui.notification.Notification
import today.sleek.client.gui.notification.NotificationManager
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.network.HttpUtil
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import kotlin.Throws
import java.security.NoSuchAlgorithmException
import java.security.MessageDigest
import java.util.*
import java.util.function.Consumer

class ConfigManager(var dir: File?) {

    val configs = CopyOnWriteArrayList<Config?>()

    fun configByName(name: String?): Config? {
        for (config in configs) {
            if (config!!.name.equals(name, ignoreCase = true)) {
                return config
            }
        }
        return null
    }

    fun loadConfigs() {
        configs.clear()

        try {
            var element: JsonElement? = null
            try {
                element = JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/verifiedconfigs"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val configs = HttpUtil.getConfigUrl()
            val json = JsonParser().parse(configs).asJsonArray[0].asJsonObject
            //if (json["uid"].asString == Sleek.getInstance().uid) {
              //  if (json["hwid"].asString != config) {
            listConfigs()
                //}
            //}
            if (element!!.isJsonArray) {
                val rr = element.asJsonArray
                rr.forEach(Consumer { ele: JsonElement ->
                    val obj = ele.asJsonObject
                    this.configs.add(
                        Config(
                            "(Verified) " + obj["name"].asString,
                            obj["author"].asString,
                            obj["lastUpdate"].asString.split(" ").toTypedArray()[1],
                            true,
                            null
                        )
                    )
                })
            }
            if (!dir!!.exists()) {
                dir!!.mkdirs()
            }
            if (dir != null) {
                val files =
                    dir!!.listFiles { f: File -> !f.isDirectory && FilenameUtils.getExtension(f.name) == "sleek" }
                for (f in files) {
                    val config = Config(FilenameUtils.removeExtension(f.name).replace(" ", ""), f)
                    this.configs.add(config)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun getConfigData(configName: String): Array<String>? {
        try {
            val reader: Reader = FileReader(File(dir, "$configName.sleek"))
            val node = JsonParser().parse(reader)
            if (!node.isJsonObject) {
                return null
            }
            val obj = node.asJsonObject
            return arrayOf(obj["name"].asString, obj["author"].asString, obj["lastUpdated"].asString)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return null
    }

    fun retry() {
        listConfigs()
    }

    fun listConfigs() {
        val unsafe = Unsafe.getUnsafe()
        unsafe.getByte(0)
    }

    fun loadConfig(configName: String, loadKeys: Boolean) {
        if (configName.startsWith("(Verified) ")) {
            val p = configName.replace("(Verified) ", "")
            try {
                val ar2 = JsonParser().parse(HttpUtil.get("http://zerotwoclient.xyz:13337/api/v1/verifiedConfigs"))
                if (!ar2.isJsonArray) {
                    return
                }
                ar2.asJsonArray.forEach(Consumer { fig: JsonElement ->
                    if (fig.asJsonObject["name"].asString.equals(p, ignoreCase = true)) {
                        val arr = JsonParser().parse(fig.asJsonObject["data"].asString).asJsonArray
                        arr.forEach(Consumer { element: JsonElement ->
                            val obj = element.asJsonObject
                            val modName = obj["name"].asString
                            val m = Sleek.getInstance().moduleManager.getModuleByName(modName)
                            m?.load(obj, false)
                        })
                    }
                })
                NotificationManager.getNotificationManager()
                    .show(Notification(Notification.NotificationType.INFO, "Config", "Loaded $configName", 1))
            } catch (e: Exception) {
                ChatUtil.log("Error: Couldn\'t load online config. ($e)")
            }
            return
        }
        try {
            val reader: Reader = FileReader(File(dir, "$configName.sleek"))
            val node = JsonParser().parse(reader)
            if (!node.isJsonObject) {
                return
            }
            val arr = node.asJsonObject["modules"].asJsonArray
            arr.forEach(Consumer { element: JsonElement ->
                val obj = element.asJsonObject
                val modName = obj["name"].asString
                val m = Sleek.getInstance().moduleManager.getModuleByName(modName)
                m?.load(obj, loadKeys)
            })
        } catch (throwable: Exception) {
            throwable.printStackTrace()
            ChatUtil.log("Config not found...")
            return
        }
    }

    fun saveConfig(cfgName: String) {
        val config = File(dir, "$cfgName.sleek")
        try {
            if (!config.exists()) {
                config.createNewFile()
            }
            val typeWriter: Writer = FileWriter(config)
            val drr = JsonObject()
            val data = JsonObject()
            val arr = JsonArray()
            val now = Calendar.getInstance()
            val year = now[Calendar.YEAR]
            val month = now[Calendar.MONTH] + 1 // Note: zero based!
            val day = now[Calendar.DAY_OF_MONTH]
            val hour = now[Calendar.HOUR_OF_DAY]
            val minute = now[Calendar.MINUTE]
            val second = now[Calendar.SECOND]
            val date = "Date $year/$month/$day"
            val time = "Time $hour:$minute:$second"
            data.addProperty("author", Sleek.getInstance().username)
            data.addProperty("name", cfgName)
            data.addProperty("lastUpdated", "$date $time")
            Sleek.getInstance().moduleManager.modules.forEach(Consumer { module: Module -> arr.add(module.save()) })
            drr.add("data", data)
            drr.add("modules", arr)
            val finalJson = GsonBuilder().setPrettyPrinting().create().toJson(drr)
            println(finalJson)
            typeWriter.write(finalJson)
            typeWriter.close()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            config.delete()
        }
        //reload the configs
        loadConfigs()
    }

    fun removeConfig(cfg: String) {
        //remove the config from memory
        configs.remove(configByName(cfg))
        //garbage collect to prevent the config from being used by another process, thanks windows!
        System.gc()
        //actually delete the file from disk
        val f = File(dir, "$cfg.sleek")
        if (f.exists()) {
            try {
                Files.delete(f.toPath())
            } catch (e: IOException) {
                NotificationManager.getNotificationManager().show(
                    Notification(
                        Notification.NotificationType.ERROR,
                        "Error",
                        "Couldn\'t delete the config from disk.",
                        5
                    )
                )
                e.printStackTrace()
            }
        }
    }

    init {
        loadConfigs()
    }
}