package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.PacketDirection;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

    private NumberValue<Integer> v = new NumberValue<>("Vertical", this, 100, 0, 100, 1);
    private NumberValue<Integer> h = new NumberValue<>("Horizontal", this, 100, -50, 200, 1);
    private ModeValue modeValue = new ModeValue("Mode", this, "Packet");
    public BooleanValue explotion = new BooleanValue("Explosion", this, true);

    public Velocity() {
        super("Velocity", ModuleCategory.COMBAT);
        register(modeValue, explotion, v, h);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        final int vertical = v.getValue();
        final int horizontal = h.getValue();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (event.getPacketDirection() != PacketDirection.INBOUND) {
                    if (event.getPacket() instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity packet = event.getPacket();
                        if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                            if (packet.getEntityID() != mc.thePlayer.getEntityId()) return;
                            if (vertical != 0 || horizontal != 0) {
                                packet.setMotionX(horizontal * packet.getMotionX() / 100);
                                packet.setMotionY(vertical * packet.getMotionY() / 100);
                                packet.setMotionZ(horizontal * packet.getMotionZ() / 100);
                            } else event.setCancelled(true);
                        }
                    }
                    if (event.getPacket() instanceof S27PacketExplosion) {
                        S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();
                        if (vertical != 0 || horizontal != 0) {
                            packet.setField_149152_f(horizontal * packet.func_149149_c() / 100);
                            packet.setField_149153_g(vertical * packet.func_149144_d() / 100);
                            packet.setField_149159_h(horizontal * packet.func_149147_e() / 100);
                        } else event.setCancelled(true);
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
