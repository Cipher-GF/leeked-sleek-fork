package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

    private NumberValue<Double> v = new NumberValue<>("Vertical", this, 100.0, 0.0, 100.0, 1.0);
    private NumberValue<Double> h = new NumberValue<>("Horizontal", this, 100.0, 0.0, 100.0, 1.0);
    private ModeValue modeValue = new ModeValue("Mode", this, "Packet");
    public BooleanValue explotion = new BooleanValue("Explosion", this, true);

    public Velocity() {
        super("Velocity", ModuleCategory.COMBAT);
        register(modeValue, explotion);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = event.getPacket();
                    event.setCancelled(mc.theWorld != null && mc.thePlayer != null && mc.theWorld.getEntityByID(packet.getEntityID()) == mc.thePlayer);
                } else if (event.getPacket() instanceof S27PacketExplosion) {
                    if (explotion.getValue()) {
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
