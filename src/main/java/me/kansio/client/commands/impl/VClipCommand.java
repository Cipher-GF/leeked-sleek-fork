package me.kansio.client.commands.impl;

import me.kansio.client.utils.chat.ChatUtil;

public class VClipCommand extends Command{

    public VClipCommand() {
        super("vclip");
    }

    @Override
    public void run(String[] args) {
        if (args.length > 0) {
            ChatUtil.log("Vclipped " + Integer.parseInt(args[0]) + " Blocks");
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Integer.parseInt(args[0]), mc.thePlayer.posZ);
        } else {
            ChatUtil.log(".vclip <amount>");
        }
    }
}
