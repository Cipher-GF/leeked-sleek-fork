package me.kansio.client.commands.impl;

import me.kansio.client.utils.chat.ChatUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class NameCommand extends Command {
    public NameCommand() {
        super("name");
    }

    @Override
    public void run(String[] args) {
        final String name = mc.thePlayer.getName();
        ChatUtil.log("Your Name Is: " + name + ", And Has Been Copied To Your Clipboard");
        StringSelection namefinal = new StringSelection(name);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(namefinal, namefinal);
    }
}


