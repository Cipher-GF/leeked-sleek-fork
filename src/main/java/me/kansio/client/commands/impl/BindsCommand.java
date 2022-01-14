package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds");
    }

    @Override
    public void run(String[] args) {
        ChatUtil.log("The Current Binds Are:");
        for (Module module : Client.getInstance().getModuleManager().getModules()) {
            if (module.getKeyBind() != 0) {
                ChatUtil.log(module.getName() + " - " + Keyboard.getKeyName(module.getKeyBind()));
            }
        }
    }
}
