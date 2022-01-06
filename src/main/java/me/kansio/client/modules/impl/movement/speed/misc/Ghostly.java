package me.kansio.client.modules.impl.movement.speed.misc;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;

public class Ghostly extends SpeedMode {

    public Ghostly() {
        super("Ghostly");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.ticksExisted % 3 == 0) {
            PlayerUtil.setMotion(event, getSpeed().getSpeed().getValue());
        } else {
            PlayerUtil.setMotion(event, 0.4);
        }
    }
}
