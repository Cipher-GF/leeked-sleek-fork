package today.sleek.client.modules.impl.movement.flight.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.sleek.base.event.impl.MoveEvent;
import today.sleek.base.event.impl.PacketEvent;
import today.sleek.client.modules.impl.movement.flight.FlightMode;
import today.sleek.client.utils.chat.ChatUtil;
import today.sleek.client.utils.network.PacketUtil;
import today.sleek.client.utils.player.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

public class Zonecraft extends FlightMode {

    public Zonecraft() {
        super("Zonecraft");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = 0.42F);
        } else {
            event.setMotionY(mc.thePlayer.motionY = 0.0F);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1E-4, mc.thePlayer.posZ);
            PlayerUtil.setMotion(event, PlayerUtil.getBaseMoveSpeed(mc.thePlayer));
        }
    }
}
