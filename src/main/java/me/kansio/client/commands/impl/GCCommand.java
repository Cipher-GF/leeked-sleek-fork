package me.kansio.client.commands.impl;

import me.kansio.client.commands.Command;
import me.kansio.client.commands.CommandData;

@CommandData(name = "gc", description = "Runs System.gc()")
public class GCCommand extends Command {
    @Override
    public void run(String[] args) {
        System.gc();
    }
}
