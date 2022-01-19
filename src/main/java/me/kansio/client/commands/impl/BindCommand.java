package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind");
    }

    @Override
    public void run(String[] args) {
        try {
            if (args[0].equalsIgnoreCase("list")) {
                ChatUtil.log("The Current Binds Are:");
                for (Module module : Client.getInstance().getModuleManager().getModules()) {
                    if (module.getKeyBind() != 0) {
                        ChatUtil.log(module.getName() + " - " + Keyboard.getKeyName(module.getKeyBind()));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("del")) {
                Module module = Client.getInstance().getModuleManager().getModuleByName(args[2]);
                ChatUtil.log("Deleted the bind.");
                module.setKeyBind(0, true);
            } else {
                Module module = Client.getInstance().getModuleManager().getModuleByName(args[0]);
                if (module != null) {
                    int key = Keyboard.getKeyIndex(args[1].toUpperCase());
                    if (key != -1) {
                        ChatUtil.log("You've set the bind to " + Keyboard.getKeyName(key) + ".");
                        module.setKeyBind(key, true);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            ChatUtil.log("Usage: bind [module] [key]");
            ChatUtil.log("Usage: bind del [module]");
        } catch (Exception exception) {
            exception.printStackTrace();
//            ChatUtil.displayChatMessage("Invalid arguments.");
        }
    }
}
