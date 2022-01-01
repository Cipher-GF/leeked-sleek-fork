package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Packet", "Client-Side");

    public NoRotate() {
        super("No Rotate", ModuleCategory.PLAYER);
        register(mode);
    }

    @Subscribe
    public void onSendPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case "Packet":
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = event.getPacket();
                    if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0) {
                        packet.yaw = mc.thePlayer.rotationYaw;
                        packet.pitch = mc.thePlayer.rotationPitch;
                    }
                }
                break;
            case "Client-Side":
                break;
        }
    }
}
