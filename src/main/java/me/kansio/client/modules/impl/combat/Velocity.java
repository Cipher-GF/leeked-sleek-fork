package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

    private NumberValue<Integer> v = new NumberValue<>("Vertical", this, 100, 0, 100, 1);
    private NumberValue<Integer> h = new NumberValue<>("Horizotal", this, 100, 0, 100, 1);
    private ModeValue modeValue = new ModeValue("Mode", this, "Packet");
    public BooleanValue explotion = new BooleanValue("Explotion", this, true);

    public Velocity() {
        super("Velocity", ModuleCategory.COMBAT);
        register(modeValue, explotion, v, h);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        Packet packet = event.getPacket();

        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity packetEntityVelocity = event.getPacket();
                    event.setCancelled(mc.theWorld != null && mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) == mc.thePlayer);
                }
                if (explotion.getValue()) {
                    if (packet instanceof S27PacketExplosion) {
                        event.setCancelled(true);
                    }
                }
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return " " + modeValue.getValueAsString();
    }
}
