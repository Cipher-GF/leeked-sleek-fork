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

    private ModeValue mode = new ModeValue("Mode", this, "Packet", "Spoof", "Verus");

    public NoFall() {
        super("No Fall", ModuleCategory.PLAYER);
        register(mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && mc.thePlayer.fallDistance > 2F) {
            switch (mode.getValueAsString()) {
                case "Spoof": {
                    event.setOnGround(true);
                    break;
                }
                case "Verus": {
                    double y = Math.round(mc.thePlayer.posY / 0.015625) * 0.015625;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ);
                    event.setOnGround(true);
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.fallDistance = 0;
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (mode.getValueAsString()) {
            case "Packet": {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c03 = event.getPacket();
                    c03.onGround = true;
                }
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }


}
