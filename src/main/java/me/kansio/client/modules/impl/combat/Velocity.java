package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Packet");

    public Velocity() {
        super("Velocity", ModuleCategory.COMBAT);
        register(modeValue);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        Packet packet = event.getPacket();

        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity packetEntityVelocity = event.getPacket();
                    event.setCancelled(mc.theWorld != null && mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) == mc.thePlayer);
                } else if (packet instanceof S27PacketExplosion) {
                    event.setCancelled(true);
                }
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return "" + modeValue.getValueAsString();
    }
}
