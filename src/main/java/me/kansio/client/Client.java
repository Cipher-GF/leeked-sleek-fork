package me.kansio.client;

import dorkbox.messageBus.MessageBus;
import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.commands.CommandManager;
import me.kansio.client.event.impl.KeyboardEvent;
import me.kansio.client.manager.ValueManager;
import me.kansio.client.modules.ModuleManager;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;

public class Client {

    @Getter
    private static Client instance = new Client();

    @Getter
    private MessageBus eventBus = new MessageBus();

    @Getter
    private ModuleManager moduleManager;

    @Getter
    private CommandManager commandManager;

    @Getter
    private ValueManager valueManager;

    public void onStart() {
        //Subscribe to the event bus
        eventBus.subscribe(this);

        //Set the value manager
        valueManager = new ValueManager();

        //Set the module manager variable
        moduleManager = new ModuleManager();

        //Set the command manager
        commandManager = new CommandManager();

        System.out.println("Client has been started.");
    }

    public void onShutdown() {
        //shutdown the event bus
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
