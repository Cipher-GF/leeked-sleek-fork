package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sprint extends Module {

    private final BooleanValue omni = new BooleanValue("Omni", this, false);

    public Sprint() {
        super("Sprint", ModuleCategory.PLAYER);
        register(omni);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isSneaking()) return;

        if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction entityAction = event.getPacket();
            if (entityAction.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                //event.setCancelled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
