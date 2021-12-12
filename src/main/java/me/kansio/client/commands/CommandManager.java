package me.kansio.client.commands;

import me.kansio.client.commands.impl.BindCommand;
import me.kansio.client.commands.impl.Command;
import me.kansio.client.commands.impl.ReloadCommand;
import me.kansio.client.commands.impl.ToggleCommand;
import me.kansio.client.utils.chat.ChatUtil;

import java.util.ArrayList;

public class CommandManager {

    private ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        registerCommands();
    }

    public void callCommand(String cmd) {
        String[] split = cmd.split(" ");
        String command = split[0];
        String args = cmd.substring(command.length()).trim();
        for (Command command1 : commands) {
            String cmdName = "." + command1.getName();
            if (cmdName.equalsIgnoreCase(command)) {
                try {
                    command1.run(args.split(" "));
                } catch (Exception e) {
                    ChatUtil.log("Invalid command usage.");
                }
            }
        }

    }

    public void clearCommands() {
        commands.clear();
    }

    public void registerCommands() {
        commands.add(new ToggleCommand());
        commands.add(new ReloadCommand());
        commands.add(new BindCommand());
    }
}
