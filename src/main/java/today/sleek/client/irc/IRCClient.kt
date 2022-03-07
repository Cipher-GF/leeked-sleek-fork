package today.sleek.client.irc

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import today.sleek.Sleek
import today.sleek.client.irc.IRCClient
import today.sleek.client.modules.impl.player.IRC
import today.sleek.client.utils.chat.ChatUtil
import java.lang.Exception
import java.net.URI

class IRCClient : WebSocketClient(URI("ws://zerotwoclient.xyz:1337")) {

    override fun onOpen(serverHandshake: ServerHandshake) {
        println("IRC Connected")
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("§7[§bIRC§7] §f" + "Connected"))
    }

    override fun onMessage(s: String) {
        println(s)
        if (s.contains(Character.toString(SPLIT))) {
            val split = s.split(Character.toString(SPLIT)).toTypedArray()
            if (split.size != 3) {
                return
            }
            val username = split[0]
            var uid = split[1]
            val message = split[2]
            uid = uid.replace("(", "§7(§b").replace(")", "§7)")
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                ChatComponentText(
                    ChatUtil.translateColorCodes(
                        "§7[§bIRC§7] §b$username$uid §f: $message"
                    )
                )
            )
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("§7[§bIRC§7] $s"))
        }
    }

    override fun onClose(i: Int, s: String, b: Boolean) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("§7[§bIRC§7] §f" + "Disconnected"))
        val irc = Sleek.getInstance().moduleManager.getModuleByName("IRC") as IRC?
        if (irc!!.isToggled) {
            irc.toggle()
        }
    }

    override fun onError(e: Exception) {
        e.printStackTrace()
    }

    companion object {
        var SPLIT = '\u0000'
    }

    init {
        setAttachment(Sleek.getInstance().rank.color.toString().replace("§", "&") + Sleek.getInstance().username)
        addHeader("name", getAttachment())
        addHeader("uid", Sleek.getInstance().uid)
    }
}