package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.network.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Cancel", "Packet", "Spoof");

    public NoRotate() {
        super("No Rotate", ModuleCategory.PLAYER);
        register(mode);
    }

    @Subscribe
    public void onSendPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case "Cancel": {
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = event.getPacket();
                    if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0) {
                        event.setCancelled(true);
                    }
                }
                break;
            }
            case "Packet":
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = event.getPacket();
                    if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0) {
                        packet.yaw = mc.thePlayer.rotationYaw;
                        packet.pitch = mc.thePlayer.rotationPitch;
                    }
                }
                break;
            case "Spoof":
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = event.getPacket();
                    if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0) {
                        event.setCancelled(true); //don't receive the packet

                        // send a c03 with the values of the packet,
                        // since the server is expecting you to send one,
                        // and some anticheats can use this to detect if you're using a norotate.
                        // :troll:
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
                    }
                }
                break;
        }
    }
}
