package me.kansio.client.modules.impl.player.hackerdetect;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

public class HackerDetect extends Module {

    @Getter
    private static HackerDetect instance;

    @Getter @Setter
    private double cageYValue;


    public HackerDetect() {
        super("Hacker Detect", ModuleCategory.PLAYER);
        instance = this;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        for (Check c : Client.getInstance().getCheckManager().getChecks()) {
            c.onUpdate();
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        for (Check c : Client.getInstance().getCheckManager().getChecks()) {
            c.onPacket();
        }
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = event.getPacket();
            String msg = packet.getChatComponent().getFormattedText();

            if (msg.contains("Cages open in:")) {
                for (Check c : Client.getInstance().getCheckManager().getChecks()) {
                    c.onBlocksMCGameStartTick();
                }
            }
        }
    }

}
