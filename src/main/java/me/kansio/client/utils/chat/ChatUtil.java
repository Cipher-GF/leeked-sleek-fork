package me.kansio.client.utils.chat;

import me.kansio.client.utils.Util;
import net.minecraft.util.ChatComponentText;

public class ChatUtil extends Util {

    public static void log(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§7[§bSleek§7] §f" + message));
    }

    public static String translateColorCodes(String toTranslate) {
        return toTranslate.replaceAll("&", "§");
    }

}
