package me.kansio.client.commands.impl

import me.kansio.client.Client
import me.kansio.client.commands.Command
import me.kansio.client.commands.CommandData
import me.kansio.client.modules.impl.Module
import me.kansio.client.utils.chat.ChatUtil
import java.util.function.Consumer

/**
 * @author Kansio
 */
@CommandData(
    name = "showmodule",
    description = "Hides a module"
)
class ShowModuleCommand : Command() {

    override fun run(args: Array<String>) {
        if (args.size != 1) {
            ChatUtil.log("Please specify a module to hide. You can also hide all modules by typing 'all'")
            return
        }
        if (args[0].equals("all", ignoreCase = true)) {
            Client.getInstance().moduleManager.modules.forEach(Consumer { module: Module -> module.isHidden = false })
            ChatUtil.log("You've shown all the modules.")
            return
        }
        val name = args[0]
        Client.getInstance().moduleManager.getModuleByNameIgnoreSpace(name).isHidden = false
        ChatUtil.log("You've shown the module '$name'.")
    }
}