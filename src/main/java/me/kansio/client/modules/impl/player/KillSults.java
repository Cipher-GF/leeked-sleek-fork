package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.network.play.server.S02PacketChat;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;

public class KillSults extends Module {

    private final List<String> messages = Arrays.asList(
            /*/"You got sleeked L",
            "Sleek is just better...",
            "Verus got killed by Sleek",
            "You just got absolutely raped by Sleek :)",
            "Sleek too op I guess",
            "You got killed by " + Client.getInstance().getUsername() + "(uid: " + Client.getInstance().getUid() + ") using Sleek hake",
            "We do be doing slight amounts of trolling using Sleek",
            "me and da sleek bois destroying blocksmc",
            "sussy among us sleek hack???",/*/
            "mad? rage at me on discord: " + Client.getInstance().getDiscordTag(),
            "got angry? rage at me on discord: " + Client.getInstance().getDiscordTag(),
            "rage at me on discord: " + Client.getInstance().getDiscordTag(),
            "mad? rage at me on discord: "+ Client.getInstance().getDiscordTag()  +" :troll:"
    );

    private final ModeValue modeValue = new ModeValue("Mode", this, "BlocksMC");

    public KillSults() {
        super("Kill Sults", ModuleCategory.PLAYER);
        register(modeValue);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat p = event.getPacket();
            String msg = p.getChatComponent().getFormattedText();

            switch (modeValue.getValue()) {
                case "BlocksMC": {
                    if (msg.contains("for killing")) {
                        sendKillSult();
                    }
                    break;
                }
            }
        }
    }

    public void sendKillSult() {
        final List<String> messages = Arrays.asList(
            /*/"You got sleeked L",
            "Sleek is just better...",
            "Verus got killed by Sleek",
            "You just got absolutely raped by Sleek :)",
            "Sleek too op I guess",
            "You got killed by " + Client.getInstance().getUsername() + "(uid: " + Client.getInstance().getUid() + ") using Sleek hake",
            "We do be doing slight amounts of trolling using Sleek",
            "me and da sleek bois destroying blocksmc",
            "sussy among us sleek hack???",/*/
                "mad? rage at me on discord: " + Client.getInstance().getDiscordTag(),
                "got angry? rage at me on discord: " + Client.getInstance().getDiscordTag(),
                "rage at me on discord: " + Client.getInstance().getDiscordTag(),
                "mad? rage at me on discord: "+ Client.getInstance().getDiscordTag()  +" :troll:",
                "like da hack? https://discord.gg/GUauVwtFKj",
                "hack too good? get it here: https://discord.gg/GUauVwtFKj"
        );
        mc.thePlayer.sendChatMessage(messages.get(RandomUtils.nextInt(0, messages.size() - 1)));
    }
}
