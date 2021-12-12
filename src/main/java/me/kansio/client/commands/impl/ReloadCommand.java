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
        ChatUtil.log("Reloaded.");
    }
}
