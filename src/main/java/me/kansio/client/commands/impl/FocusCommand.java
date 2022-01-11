package me.kansio.client.commands.impl;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.combat.KillAura;
import me.kansio.client.utils.chat.ChatUtil;

public class FocusCommand extends Command {
    public FocusCommand() {
        super("focus");
    }

    @Override
    public void run(String[] args) {
        if (args.length > 0) {
            Client.getInstance().getTargetManager().getTarget().add(args[0]);
            ChatUtil.log("Added them as a target.");
        }
    }
}
