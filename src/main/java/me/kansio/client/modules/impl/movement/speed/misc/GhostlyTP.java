package me.kansio.client.modules.impl.movement.speed.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;

public class GhostlyTP extends SpeedMode {
    public GhostlyTP() {
        super("Ghostly TP");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * getSpeed().getSpeed().getValue();
        double z = Math.cos(yaw) * getSpeed().getSpeed().getValue();

        if (!mc.thePlayer.isMoving()) return;

        if (mc.thePlayer.ticksExisted % 5 == 0) {
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }
}
