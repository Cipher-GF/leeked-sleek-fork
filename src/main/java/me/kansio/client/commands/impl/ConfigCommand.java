package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.utils.chat.ChatUtil;

import java.io.File;
import java.util.Locale;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");
    }

    @Override
    public void run(String[] args) {
        try {
            switch (args[0].toLowerCase()) {
                case "save": {
                    Client.getInstance().getConfigManager().saveConfig(args[1]);
                    ChatUtil.log("Saved Config " + args[1]);
                    break;
                }
                case "load": {
                    Client.getInstance().getConfigManager().loadConfig(args[1]);
                    ChatUtil.log("Loaded Config " + args[1]);
                    break;
                }
                case "reload": {
                    Client.getInstance().getConfigManager().loadConfigs();
                    ChatUtil.log("Reloaded Configs");
                    break;
                }
                case "delete":
                case "remove": {
                    Client.getInstance().getConfigManager().removeConfig(args[1]);
                    ChatUtil.log("Removed Config " + args[1]);
                    break;
                }
                case "list": {
                    for (File file : Client.getInstance().getConfigManager().getDir().listFiles()) {
                        ChatUtil.log(file.getName());
                    }
                    break;
                }
            }
        } catch (Throwable ignored) {
            ChatUtil.log(".config save <configName>");
            ChatUtil.log(".config load <configName>");
            ChatUtil.log(".config remove <configName>");
            ChatUtil.log(".config reload");
            ChatUtil.log(".config list");
        }
    }
}
