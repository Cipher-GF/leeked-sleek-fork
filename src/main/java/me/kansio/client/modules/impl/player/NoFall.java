package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Packet", "Spoof");

    public NoFall() {
        super("No Fall", ModuleCategory.PLAYER);
        register(modeValue);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (modeValue.getValueAsString()) {
            case "Spoof": {
                if (mc.thePlayer.fallDistance >= 3) {
                    event.setOnGround(true);
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c03 = event.getPacket();
                    c03.onGround = true;
                }
                break;
            }
        }
    }
}
