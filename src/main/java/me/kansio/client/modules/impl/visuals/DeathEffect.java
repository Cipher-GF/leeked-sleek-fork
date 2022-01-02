package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.val;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ResourceLocation;

public class DeathEffect extends Module {

    public ModeValue mode = new ModeValue("Effect Mode", this, "Blood", "Lightning");
    public BooleanValue soundon = new BooleanValue("Play Sound", this, false);
    public BooleanValue oofsoundon = new BooleanValue("Death Sounds", this, false);
    public ModeValue oofsound = new ModeValue("OOF Sound", this, oofsoundon, "OOF", "N Word1", "N Word2", "Win Error", "Yoda", "GTA");

    public DeathEffect() {
        super("DeathEffect", ModuleCategory.VISUALS);
        register(mode, soundon, oofsoundon, oofsound);
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
        if (!oofsoundon.getValue()) return;
        if (event.getPacket() instanceof S02PacketChat) {

            S02PacketChat packet = event.getPacket();

            final String msg = packet.getChatComponent().getUnformattedText();
            final String[] split = msg.split(" ");
            for (String trigger : deathMessage) {
                if (msg.contains(trigger) && msg.contains(mc.thePlayer.getName()) && !split[0].equalsIgnoreCase(mc.thePlayer.getName())) {
                    playSound(oofsound.getValue());
                }
            }
        }
    }

    public void playSound(String mode) {
        ChatUtil.log("deatheffect." + mode.toLowerCase().replace(" ", ""));
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("deatheffect." + mode.toLowerCase().replace(" ", "")),
                1.0f));
    }
    
}