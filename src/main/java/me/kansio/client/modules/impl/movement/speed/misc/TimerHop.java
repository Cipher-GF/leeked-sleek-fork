package me.kansio.client.modules.impl.movement.speed.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.TimerUtil;

public class TimerHop extends SpeedMode {

    public TimerHop() {
        super("TimerHop");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            mc.thePlayer.speedInAir = 0.0204f;
            TimerUtil.Timer(0.65F);
            mc.gameSettings.keyBindJump.pressed = true;
        } else {
            TimerUtil.Timer(1.0F);
            mc.gameSettings.keyBindJump.pressed = false;
        }

        if (mc.thePlayer.isMoving()) {
            if (mc.thePlayer.fallDistance < 0.1) {
                TimerUtil.Timer(1.81F);
            }
            if (mc.thePlayer.fallDistance > 0.2) {
                TimerUtil.Timer(0.42f);
            }
            if (mc.thePlayer.fallDistance > 0.6) {
                TimerUtil.Timer(1.05f);
                mc.thePlayer.speedInAir = 0.02019f;
            }
        }

        if (mc.thePlayer.fallDistance > 1) {
            TimerUtil.Timer(1.0F);
            mc.thePlayer.speedInAir = 0.02f;
        }
    }
}
