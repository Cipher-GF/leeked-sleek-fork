package me.kansio.client.modules.impl.movement;


import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.NoSlow;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;

@ModuleData(
        name = "Sprint",
        category = ModuleCategory.MOVEMENT,
        description = "Automatically sprints"
)
public class Sprint extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Legit", "Omni");

    @Getter
    private final BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, true); //Handled in NetHandlerPlayerClient at "processEntityAction" and EntityPlayerSP at "setSprinting"

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isSneaking()) return;

        switch (mode.getValue()) {
            case "Legit":
                if (Client.getInstance().getModuleManager().getModuleByName("No Slow").isToggled()) {
                    if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.setSprinting(true);
                    }
                } else {
                    if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.setSprinting(true);
                    }
                }
                break;
            case "Omni":
                if (mc.thePlayer.isMoving())
                    mc.thePlayer.setSprinting(true);
                break;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.setSprinting(false);
    }

}
