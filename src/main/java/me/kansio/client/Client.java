package me.kansio.client;

import dorkbox.messageBus.MessageBus;
import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.commands.CommandManager;
import me.kansio.client.config.ConfigManager;
import me.kansio.client.event.impl.KeyboardEvent;
import me.kansio.client.keybind.KeybindManager;
import me.kansio.client.manager.ValueManager;
import me.kansio.client.modules.ModuleManager;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import java.io.File;

public class Client {

    @Getter private File dir;

    @Getter
    private static Client instance = new Client();

    @Getter
    private MessageBus eventBus = new MessageBus();

    @Getter
    private ModuleManager moduleManager;

    @Getter
    private CommandManager commandManager;

    @Getter
    private ConfigManager configManager;

    @Getter
    private ValueManager valueManager;

    @Getter
    private KeybindManager keybindManager;

    public void onStart() {
        //Set the client file directory
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Sleek");

        //Subscribe to the event bus
        eventBus.subscribe(this);

        //Set the value manager
        valueManager = new ValueManager();

        //Set the module manager variable
        moduleManager = new ModuleManager();

        //Set the command manager
        commandManager = new CommandManager();

        //Set the config manager
        configManager = new ConfigManager(new File(dir, "configs"));

        //Set the keybind manager
        keybindManager = new KeybindManager(dir);
        keybindManager.load();

        System.out.println("Client has been started.");
        Display.setTitle("Sleek v0.1");
    }

    public void onShutdown() {
        //shutdown the event bus
        System.out.println("Shutting down...");
        eventBus.shutdown();
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        int key = event.getKeyCode();
        //This handles keybinds.
        for (Module module : moduleManager.getModules()) {
            //check if the keybind is -1, if it is, just continue.
            if (module.getKeyBind() == -1)
                continue;

            //if the bind == the key, toggle the module
            if (module.getKeyBind() == key) {
                module.toggle();
            }
        }
    }

}
