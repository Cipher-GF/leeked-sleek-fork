package me.kansio.client.modules.impl.movement.speed.viper;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;

public class ViperGround extends SpeedMode {
    public ViperGround() {
        super("Viper Ground");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (!mc.thePlayer.onGround) return;

        mc.timer.timerSpeed = 0.3f;
        if (mc.thePlayer.isMoving()) {
            for (int i = 0; i < 17; ++i) {
                PlayerUtil.TP(event, 0.27, 0);
            }
        }
    }
}
