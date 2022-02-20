package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.commands.Command;
import me.kansio.client.commands.CommandData;
import me.kansio.client.modules.impl.Module;

@CommandData(
        name = "panic",
        description = "Disables every module"
)
public class PanicCommand extends Command {
    @Override
    public void run(String[] args) {
        for (Module m : Client.getInstance().getModuleManager().getModules()) {
            if (m.isToggled()) {
                m.toggle();
            }
        }
    }
}
