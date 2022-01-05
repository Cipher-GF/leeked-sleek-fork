package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.NoSlowEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sprint extends Module {

    private final BooleanValue omni = new BooleanValue("Omni", this, false);
    public BooleanValue keepsprint = new BooleanValue("Keep Sprint", this, true);
    private BooleanValue legit = new BooleanValue("Legit Sprint", this, true);

    @Getter
    private final BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, true); //Handled in NetHandlerPlayerClient at "processEntityAction" and EntityPlayerSP at "setSprinting"

    public Sprint() {
        super("Sprint", ModuleCategory.PLAYER);
        register(omni, legit, keepsprint);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isSneaking()) return;

        if (mc.thePlayer.isMoving()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.setSprinting(false);
    }

}
