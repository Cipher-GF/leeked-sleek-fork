package me.kansio.client.modules.impl.movement.speed.viper;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;


public class ViperGround extends SpeedMode {
    public ViperGround() {
        super("Viper Ground");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (!mc.thePlayer.isMovingOnGround()) {
            mc.timer.timerSpeed = 1f;
            mc.thePlayer.motionY = - 5f;
            return;
        }


        if (mc.thePlayer.isMoving()) {
            mc.timer.timerSpeed = 0.3f;
            for (int i = 0; i < 17; ++i) {
                PlayerUtil.TP(event, 0.22, 0);
            }
        }
    }
}
