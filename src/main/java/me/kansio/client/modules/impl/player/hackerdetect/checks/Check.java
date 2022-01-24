package me.kansio.client.modules.impl.player.hackerdetect.checks;

import me.kansio.client.modules.impl.player.HackerDetect;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Check {

    public abstract String name();

    protected HackerDetect detect = HackerDetect.getInstance();

    public void onUpdate() {

    }

    public void onBlocksMCGameStartTick() {


    }
    public void onPacket() {

    }

    public void flag(EntityPlayer player) {
        ChatUtil.logSleekCheater(player.getName(), name());
    }

}
