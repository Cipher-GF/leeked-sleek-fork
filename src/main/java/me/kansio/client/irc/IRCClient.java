package me.kansio.client.irc;

import me.kansio.client.Client;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class IRCClient extends WebSocketClient {

    public static char SPLIT = '\u0000';

    public IRCClient() throws URISyntaxException {
        super(new URI("ws://zerotwoclient.xyz:1337"));
        this.setAttachment(Client.getInstance().getUsername());
        this.addHeader("name", this.getAttachment());
    }

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

            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §b" + username + "§f: " + message));
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§bIRC§7] §cSERVER" + "§f: " + s));
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
