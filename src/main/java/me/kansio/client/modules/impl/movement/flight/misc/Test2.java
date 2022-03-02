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
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class Test2 extends FlightMode {

    private List<Packet<INetHandlerPlayServer>> packets = new ArrayList<>();
    private boolean disabled = false;
    private int ticks = 0;


    public Test2() {
        super("Zonecraft");
    }

    @Override
    public void onEnable() {
        disabled = false;
        ticks = 0;
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-7D, mc.thePlayer.posZ, false));
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.3, mc.thePlayer.posZ, true));
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    @Override
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            disabled = true;
            ChatUtil.log("gooooo");
        }
        if(event.getPacket() instanceof C03PacketPlayer){
            if(ticks < 10)
                return;
            ((C03PacketPlayer) event.getPacket()).onGround = true;
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        if(!disabled)
            return;

        ticks++;
        if(ticks >= 10)
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
        if(ticks > 10){
            ticks = 0;
            mc.thePlayer.jump();
            PlayerUtil.setMotion(0.38725);
            PlayerUtil.setMotion(event,-0.0217);
            event.setMotionY(mc.thePlayer.motionY);
        }else{

        }
        event.setStrafeSpeed(0.25);
    }



}
