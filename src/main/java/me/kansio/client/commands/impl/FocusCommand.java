package me.kansio.client.commands.impl;

import me.kansio.client.modules.impl.combat.KillAura;

public class FocusCommand extends Command {
    public FocusCommand() {
        super("focus");
    }

    @Override
    public void run(String[] args) {
        if (args.length > 0) {
            KillAura.setFocus(args[0]);
        }
    }
}
