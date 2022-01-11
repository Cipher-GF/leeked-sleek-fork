package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.Arrays;
import java.util.List;

public class AntiSpam extends Module {

    private List<String> spammers = Arrays.asList("FDPClient", "moonclient.xyz");

    public AntiSpam() {
        super("Anti Spammer", ModuleCategory.VISUALS);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        //make sure it's a chat packet :Troll:
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chatPacket = event.getPacket();
            //get the formatted text
            String formattedText = chatPacket.getChatComponent().getFormattedText();

            //go thru all the blocked strings
            for (String text : spammers) {
                //if it contains a blocked string, hide it
                if (formattedText.toLowerCase().contains(text.toLowerCase())) {
                    System.out.println("[Anti Spammer] Hid the message '" + formattedText + "'.");
                    event.setCancelled(true);
                }
            }
        }
    }
}
