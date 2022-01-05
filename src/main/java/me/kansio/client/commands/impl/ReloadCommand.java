package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.utils.chat.ChatUtil;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void run(String[] args) {
        Client.getInstance().getModuleManager().reloadModules();
        Client.getInstance().getKeybindManager().load();
        ChatUtil.log("Reloaded.");
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) {
                Client.getInstance().getCommandManager().clearCommands();
                Client.getInstance().getCommandManager().registerCommands();
            }
        }
    }
}
