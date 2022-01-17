package me.kansio.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.ChatEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.irc.IRCClient;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.chat.ChatUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ModuleData(
        name = "IRC",
        category = ModuleCategory.PLAYER,
        description = "Let's you chat with other client users"
)
public class IRC extends Module {

    public IRC() {
        super("IRC", ModuleCategory.PLAYER);
    }

    private IRCClient client;
    Stopwatch time = new Stopwatch();
    boolean next = false;
    public boolean SPAM = false;
    public static boolean TROLLCOMPLETE = false;
    public boolean ALLOWED = Integer.parseInt(Client.getInstance().getUid()) < 10;


    public void onEnable() {
        time.resetTime();
        SPAM = false;

        try {
            client = new IRCClient();
            client.connectBlocking();

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        client.close();
        client = null;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (SPAM && time.timeElapsed(250)) {
            client.send(client.getAttachment().toString() + IRCClient.SPLIT +
                    "\n⠀⠀⠀⠀⠀⢰⡿⠋⠁⠀⠀⠈⠉⠙⠻⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢀⣿⠇⠀⢀⣴⣶⡾⠿⠿⠿⢿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⣀⣀⣸⡿⠀⠀⢸⣿⣇⠀⠀⠀⠀⠀⠀⠙⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⣾⡟⠛⣿⡇⠀⠀⢸⣿⣿⣷⣤⣤⣤⣤⣶⣶⣿⠇⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀\n" +
                    "⢀⣿⠀⢀⣿⡇⠀⠀⠀⠻⢿⣿⣿⣿⣿⣿⠿⣿⡏⠀⠀⠀⠀⢴⣶⣶⣿⣿⣿⣆\n" +
                    "⢸⣿⠀⢸⣿⡇⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⣿⡇⣀⣠⣴⣾⣮⣝⠿⠿⠿⣻⡟\n" +
                    "⢸⣿⠀⠘⣿⡇⠀⠀⠀⠀⠀⠀⠀⣠⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠉⠀\n" +
                    "⠸⣿⠀⠀⣿⡇⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠀⠀⠀\n" +
                    "⠀⠻⣷⣶⣿⣇⠀⠀⠀⢠⣼⣿⣿⣿⣿⣿⣿⣿⣛⣛⣻⠉⠁⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢸⣿⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢸⣿⣀⣀⣀⣼⡿⢿⣿⣿⣿⣿⣿⡿⣿⣿⡿");
            time.resetTime();
        }
        /*if (mc.thePlayer.ticksExisted < 5 && client != null) {
            client.reconnect();
        }*/
    }

    @Subscribe
    public void onChat(ChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith("-")) {
            event.setCancelled(true);
        }

        if (message.equalsIgnoreCase(".discconectirc")) {
            event.setCancelled(true);
            client.close();
            return;
        }

        if (message.equalsIgnoreCase("- nigger")) {
            event.setCancelled(true);
            for (int i = 0; i < 1000; i++) {
                client.send( "Nigger");
            }
            return;
        }

        if (message.equalsIgnoreCase("- minitroll")) {
            event.setCancelled(true);
            client.send(
                    "\n" +
                    "⠀⠀⠀⠀⠀⢰⡿⠋⠁⠀⠀⠈⠉⠙⠻⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢀⣿⠇⠀⢀⣴⣶⡾⠿⠿⠿⢿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⣀⣀⣸⡿⠀⠀⢸⣿⣇⠀⠀⠀⠀⠀⠀⠙⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⣾⡟⠛⣿⡇⠀⠀⢸⣿⣿⣷⣤⣤⣤⣤⣶⣶⣿⠇⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀\n" +
                    "⢀⣿⠀⢀⣿⡇⠀⠀⠀⠻⢿⣿⣿⣿⣿⣿⠿⣿⡏⠀⠀⠀⠀⢴⣶⣶⣿⣿⣿⣆\n" +
                    "⢸⣿⠀⢸⣿⡇⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⣿⡇⣀⣠⣴⣾⣮⣝⠿⠿⠿⣻⡟\n" +
                    "⢸⣿⠀⠘⣿⡇⠀⠀⠀⠀⠀⠀⠀⣠⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠉⠀\n" +
                    "⠸⣿⠀⠀⣿⡇⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠀⠀⠀\n" +
                    "⠀⠻⣷⣶⣿⣇⠀⠀⠀⢠⣼⣿⣿⣿⣿⣿⣿⣿⣛⣛⣻⠉⠁⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢸⣿⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀\n" +
                    "⠀⠀⠀⠀⢸⣿⣀⣀⣀⣼⡿⢿⣿⣿⣿⣿⣿⡿⣿⣿⡿");
            return;
        }

        if (message.equalsIgnoreCase("- troll") && !SPAM && ALLOWED) {
            SPAM = true;
            event.setCancelled(true);
            time.resetTime();
            return;
        }

        if (message.equalsIgnoreCase("- trollcomplete") && SPAM || message.equalsIgnoreCase("- trollcompletebypass")) {
            event.setCancelled(true);
            if (ALLOWED) {
                TROLLCOMPLETE = true;
                SPAM = false;
                client.send( "Trolling Complete, Returning To HQ");
            }
            return;
        } else if (message.equalsIgnoreCase("- trollcomplete") && !SPAM) {
            event.setCancelled(true);
            return;
        }

        if (message.equalsIgnoreCase("- disallow")) {
            event.setCancelled(true);
            IRCClient.allow = false;
            ChatUtil.log("You have Turned Off [REDACTED]");
            return;
        }

        if (message.equalsIgnoreCase("- allow")) {
            event.setCancelled(true);
            IRCClient.allow = true;
            ChatUtil.log("You have Turned On [REDACTED]");
            return;
        }

        if (message.equalsIgnoreCase("- pogblackman")) {
            event.setCancelled(true);
            client.send(
                    "\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⢟⢟⢻⢹⢫⡛⡻⡻⡻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⠿⣛⢍⠦⡱⡱⣑⠕⡕⡥⡱⡕⡕⣕⢢⢫⢹⢻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⡑⡌⡪⡰⡱⡱⡱⡳⣕⡯⣮⣗⣵⣳⢾⢽⣺⣞⣯⢷⢯⢷⢵⡳⣕⡕⡕⢝⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⡿⡫⢊⠌⡆⡕⣕⣵⣳⢽⢽⣫⣷⣻⣳⣻⢾⡽⣽⢯⣷⣻⣞⡿⣽⢯⡯⣟⣮⡻⣎⢧⡊⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⡟⡑⢌⢢⢱⡸⣪⢗⣗⣗⢿⢽⣳⣻⣺⢽⡽⣯⣻⢽⡽⣞⣗⡯⣯⢯⣻⣺⡳⣳⢽⡺⣝⢮⡢⣻⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⡟⠔⢌⠢⡪⡪⡮⡳⣝⢮⢮⡫⣗⢗⣗⣝⢗⡽⣺⡪⡻⡺⡵⡳⡝⡮⣫⢺⢜⢮⢳⢣⢯⣪⡳⡵⡘⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⠇⡑⠄⠕⡸⡱⡝⣝⢮⡳⡳⡝⣎⢧⢳⣸⡱⣱⡱⣕⢽⢜⢮⡺⡪⣞⣜⢮⢮⢮⢎⡧⣳⢕⢵⡹⡌⢾⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⠡⠨⡨⠊⡜⢜⢜⢎⢗⣝⢮⢯⡺⣝⣗⣗⡯⡷⡯⣗⡯⣯⡳⡽⣽⡺⡾⡽⣝⡷⣝⣞⢗⢝⡕⣇⢇⢽⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⡏⢌⠰⡐⡑⢌⢎⢪⢪⡳⡵⡽⣕⡯⣗⢗⢗⢯⢫⡫⡳⡝⡎⡏⡝⡎⡎⢯⠹⡪⡪⡳⢭⢳⡳⡽⣺⢜⠔⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⢂⢂⢂⠢⡊⡢⡱⡱⡵⡯⡯⠯⡳⡹⠸⡘⡘⡐⠅⢊⠂⠅⠕⡡⡑⢌⠨⠐⡁⡊⢐⠁⡊⠐⠅⢍⠪⠹⡨⣻⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⢀⠂⢄⠕⡌⡪⡸⢸⠘⢌⢂⢑⢐⢈⠐⡀⠄⠐⡈⠠⢈⠨⠐⡐⠄⠅⠂⡁⠄⠂⡀⠂⢄⢅⢕⢢⢊⢐⢐⢜⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⠠⠨⠐⡌⢎⢪⢊⠢⢁⢂⠢⠪⡒⢕⠱⡐⠅⡂⢐⠄⢂⠠⢁⢂⠅⡅⠅⡀⠂⡁⠄⠡⢑⢐⠡⠱⠐⢄⠢⡑⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⢳⢄⠊⡐⢜⢸⠰⡡⡑⡐⠄⡑⠡⠁⠅⠂⠂⢂⠐⢀⠐⠠⢐⢐⣕⣕⡕⡅⡐⠠⠄⡂⢁⠠⠄⡂⠡⡈⠄⡌⢎⢺⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⠄⢇⠧⡀⡣⡊⡎⡲⡨⡠⡡⡐⢄⠅⢊⠄⡁⡂⠅⡐⢠⢑⠔⣕⢵⣳⢽⣪⢌⢢⢁⢂⠂⢂⢂⢂⠣⡱⡱⣕⢕⢝⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⡗⡰⡣⠡⠣⡊⢆⢣⢣⡳⣵⣣⡣⡑⠌⠂⢂⢂⢐⢰⢨⠪⡢⡫⡮⣻⣺⣻⡮⣇⢧⢱⢱⢨⢰⢀⣂⢪⢪⡺⣎⢧⠣⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⡏⡐⠈⢨⢜⢌⢊⢎⢎⢞⡺⡪⢮⠫⡍⢎⢆⢖⢜⣔⡕⢕⡵⣝⣞⢷⣻⣽⣽⣳⣝⢮⣺⣊⢞⢼⢔⢵⡱⡹⡸⡸⡸⣸⣿⣿⣿⣿\n" +
                    "⣿⣿⣷⡐⢅⠪⡣⡑⠔⢌⢎⢎⢎⢎⢎⣪⡪⡮⣳⠽⢕⢕⣜⡷⣯⢷⣝⣯⢿⣳⢯⢷⢯⣻⢾⢽⡳⡑⠽⣕⢧⢳⢱⢱⢑⢼⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⡌⢧⣊⢪⠨⡊⡢⡡⡃⡇⣏⢞⢜⣎⢯⡪⢍⠢⡘⡐⠍⠝⢝⠺⡺⠹⡪⡙⢍⢓⠝⢌⢑⢐⠌⠨⡘⡜⣕⢕⢕⠰⣹⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣯⠪⡪⡢⡕⠰⠨⡢⠪⡸⡰⡱⣣⢳⠱⡨⠢⣝⢵⡐⢄⠌⠄⠌⠠⠑⡐⢈⠂⠡⠨⡐⡔⡭⣣⠡⢐⠨⠪⡪⢢⠱⣸⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣎⣪⣊⣌⠪⡨⢂⠣⠪⡘⡜⢜⢌⠪⡐⣝⢎⢧⢣⢣⢑⢅⠅⢅⠂⠄⢂⢈⠨⣈⡢⡡⢣⢳⢹⢔⠨⡨⠨⠢⡑⢼⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣗⠅⡊⢔⠡⡑⠌⠜⢌⢢⢑⢼⢸⢜⢕⢕⢕⢕⠥⠣⡓⡊⡚⠢⡃⠫⢨⠨⣊⢣⠣⡣⡣⡣⡘⢌⢂⢊⢾⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⡨⢂⠅⠪⢐⠡⡑⡑⢔⢸⢸⠸⠨⡨⠪⠊⢔⠡⢃⢂⠢⠈⠂⡊⠌⠄⠅⠂⠅⡊⠔⢈⠢⡊⢆⢂⠢⣻⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⡇⡂⡪⢈⢂⢂⠢⢡⠡⡃⠕⡈⠠⠈⠄⠑⠠⠈⠠⠄⠂⢁⠐⠄⠄⢁⢈⢠⢁⢄⡐⡐⠨⠨⠢⡡⠡⣻⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⡐⠄⢅⠂⡂⠌⡂⠅⡂⠅⠂⠌⢌⢆⢇⡇⡯⡽⡽⡽⣳⡻⡝⡯⡯⡯⡳⡝⡎⢎⢜⠨⡈⠪⡐⠡⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠊⡐⡐⠄⠅⡂⠅⡐⠠⢁⢑⢑⢌⠢⠩⢊⢎⠪⡱⢑⠕⢕⢕⢑⡑⡅⠇⠣⡑⢅⠢⢈⠢⠨⢨⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⠂⡐⡈⡐⠠⠡⠐⡈⢀⠢⠡⢂⠕⡈⠠⠄⡈⠄⠁⠌⠐⠄⢂⠠⠐⡈⠔⡨⠐⡈⠄⠌⢌⣾⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄⢀⠂⠡⠈⠄⡀⠂⠨⠠⡁⡂⢂⠡⠄⠄⠐⡀⠂⢀⠁⢄⢐⢀⢂⠅⡂⠡⠐⡈⢐⢸⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡑⢅⠠⠐⠄⠁⠄⠐⠈⠠⢁⠢⠨⡂⠪⡨⢊⠌⡂⡑⠄⠕⡰⢐⠕⡐⢅⠊⠄⠁⠄⡂⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⣿⣿⠟⢉⠂⠄⠠⠈⡐⠅⢂⠈⠄⠂⠈⡀⢁⠐⢈⠂⠌⠌⡂⠅⢊⠐⠠⠁⠅⠂⡂⠅⢊⠐⢈⠄⠡⢁⠢⣿⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⣿⣿⡟⠡⠐⠄⡂⠈⠄⠂⠄⠄⢀⠄⠁⠄⠁⠄⡀⠄⠂⢀⠡⠐⠄⠂⠠⠈⠠⠈⠄⠂⠠⠈⢀⠈⠄⠄⢁⢢⠱⡹⣿⣿⣿⣿⣿⣿\n" +
                    "⣿⣿⠟⡉⠄⠡⠈⡀⠂⡐⠐⠈⠄⠂⠄⠄⠁⠠⠈⠄⠄⠄⠈⠄⠄⠄⠐⠈⠄⠄⠄⠂⠁⠈⠄⠄⠄⠄⠂⠌⢔⢢⢣⢃⠈⡉⢉⠙⡻⣿\n" +
                    "⣿⠏⢀⠄⡐⠈⠄⡀⠂⠠⠈⠄⢁⠈⠠⠐⠄⠄⠐⠄⠄⠂⠄⠠⠄⠂⠄⠠⠄⢀⠄⠄⠄⠠⠐⠄⠐⠨⡈⡊⡢⡱⡸⢰⠄⠄⠄⠂⠠⢉\n" +
                    "⠃⠨⠄⡂⢂⠈⠄⠠⠐⢀⠂⠈⠄⠄⠁⠠⠄⠠⠄⠐⠄⠄⠐⠄⠄⠄⠄⠄⢀⠄⢀⠈⠄⠄⠄⠂⠁⡁⢂⢅⢪⢰⢸⢨⠄⠠⠄⠐⠄⠄");
            return;
        }

        if (message.startsWith("- ")) {
            event.setCancelled(true);
            client.send(event.getMessage().replace("- ", ""));
        }
    }
}
