package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.val;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

public class DeathEffect extends Module {

    public ModeValue mode = new ModeValue("Effect Mode", this, "Blood", "Lightning", "OOF Mod");
    public BooleanValue soundon = new BooleanValue("Play Sound", this, false);
    public ModeValue soundon = new ModeValue("Play Sound", this, "Windows", "Yoda", );

    public DeathEffect() {
        super("DeathEffect", ModuleCategory.VISUALS);
        register(mode, soundon);
    }
    
    String[] deathMessage = {
            "foi jogado no void por",
            "morreu para",
            "was slain by",
            "slain by",
            "was killed by",
            "hit the ground too hard thanks to",
            "was shot by"
    };

    @Subscribe
    public void onChat(PacketEvent event) {
        if (!mode.getValue().equals("OOF Mod")) return;
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = event.getPacket();

            final String msg = packet.getChatComponent().getUnformattedText();
            final String[] split = msg.split(" ");
            for (String trigger : deathMessage) {
                if (msg.contains(trigger) && msg.contains(mc.thePlayer.getName()) && !split[0].equalsIgnoreCase(mc.thePlayer.getName())) {
                    playSound("windows");
                }
            }
        }
    }

    public void playSound(String mode) {

    }
    
}