package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.commands.Command;
import me.kansio.client.commands.CommandData;
import me.kansio.client.utils.chat.ChatUtil;

/**
 * @author Kansio
 */
@CommandData(
        name = "hidemodule",
        description = "Hides a module"
)
public class HideModuleCommand extends Command {

    @Override
    public void run(String[] args) {
        if (args.length != 1) {
            ChatUtil.log("Please specify a module to hide. You can also hide all modules by typing 'all'");
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            Client.getInstance().getModuleManager().getModules().forEach(module -> module.setHidden(true));
            ChatUtil.log("You've hid all the modules.");
            return;
        }

        String name = args[0].replaceAll("_", " ");
        Client.getInstance().getModuleManager().getModuleByName(name).setHidden(true);
        ChatUtil.log("You've hid the module '" + name + "'.");
    }
}
