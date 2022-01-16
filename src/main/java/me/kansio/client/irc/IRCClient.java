package me.kansio.client.irc;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.player.IRC;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class IRCClient extends WebSocketClient {

    public static boolean allow = true;
    Boolean STAFF = Integer.parseInt(Client.getInstance().getUid()) < 10;

    private void blacklistStaff() {
        if (STAFF) {
            allow = false;
        } else {
            allow = true;
        }
    }

    public static char SPLIT = '\u0000';

    public IRCClient() throws URISyntaxException {
        super(new URI("ws://zerotwoclient.xyz:1337"));
        this.setAttachment(Client.getInstance().getUsername());
        this.addHeader("name", this.getAttachment());
        this.addHeader("uid", Client.getInstance().getUid());
        blacklistStaff();
    }

    public static boolean isAllowed() {
        return allow;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("IRC Connected");
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §f" + "Connected"));
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);

        if (s.contains(Character.toString(SPLIT))) {

            String split[] = s.split(Character.toString(SPLIT));
            if (split.length != 3) {
                return;
            }
            String username = split[0];
            String uid = split[1];
            String message = split[2];


            if (message.startsWith("openurl=")) {
                if (allow && STAFF) {
                    String url = message.replaceAll("openurl=", "");
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.equals("Trolling Complete, Returning To HQ") && IRC.TROLLCOMPLETE) {
                try {
                    Desktop.getDesktop().browse(new URI("https://c.tenor.com/Yfz3eq2ZLo0AAAAd/pee.gif"));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                ChatUtil.log("Trolling Complete, Returning To HQ");
                IRC.TROLLCOMPLETE = false;
            } else {
                uid = uid.replace("(", "§7(§b").replace(")", "§7)");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §b" + username + uid + " " + "§f: " + message));
            }

        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] " + s));
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("IRC Disconnected");
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §f" + "Disconnected"));
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

}
