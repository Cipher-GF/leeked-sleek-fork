package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.PacketDirection;
import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.network.PacketUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class Test2 extends FlightMode {

    private List<Packet<INetHandlerPlayServer>> packets = new ArrayList<>();

    public Test2() {
        super("Test 2");
    }

    @Override
    public void onEnable() {
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.3, mc.thePlayer.posZ, true));
    }

    @Override
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            ChatUtil.log("gooooo");
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        event.setMotionY(mc.thePlayer.motionY = 0F);
        PlayerUtil.setMotion(event, 0.34463563);
    }



}
