package me.kansio.client.modules.impl.movement;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
@ModuleData(
        name = "Step",
        category = ModuleCategory.MOVEMENT,
        description = "Step over blocks."
)
public class Step extends Module {

    @Subscribe
    public void UpdateEvent(UpdateEvent event) {
        if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (!Client.getInstance().getModuleManager().getModuleByName("Flight").isToggled())) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.stepHeight = 1.0F;
        }
    }
}
