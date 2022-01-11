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
            "mad? rage at me on discord: Kansio#2996",
            "got angry? rage at me on discord: Kansio#2996",
            "like da hack? https://discord.gg/GUauVwtFKj",
            "hack too good? get it here: https://discord.gg/GUauVwtFKj"
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
        mc.thePlayer.sendChatMessage(messages.get(RandomUtils.nextInt(0, messages.size() - 1)));
    }
}
