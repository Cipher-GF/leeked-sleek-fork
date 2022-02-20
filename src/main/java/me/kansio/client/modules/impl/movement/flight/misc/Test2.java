package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.PacketDirection;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.network.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.util.ArrayList;
import java.util.List;

public class Test2 extends FlightMode {

    private List<Packet<INetHandlerPlayServer>> packets = new ArrayList<>();

    public Test2() {
        super("Test 2");
    }

    @Override
    public void onMove(MoveEvent event) {
        event.setMotionY(mc.thePlayer.motionY = 0);
    }

    @Override
    public void onDisable() {
        for (Packet<INetHandlerPlayServer> packet : packets) {
            PacketUtil.sendPacketNoEvent(packet);
            packets.remove(packet);
        }
    }

    @Override
    public void onPacket(PacketEvent event) {
        if (event.getPacketDirection() != PacketDirection.INBOUND) {
            return;
        }
        event.setCancelled(true);
        packets.add(event.getPacket());
    }
}
