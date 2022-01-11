package me.kansio.client.modules.impl.movement.speed.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;

public class TimerHop extends SpeedMode {

    public TimerHop() {
        super("TimerHop");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            mc.thePlayer.speedInAir = 0.0204f;
            mc.timer.timerSpeed = 0.65f;
            mc.gameSettings.keyBindJump.pressed = true;
        } else {
            mc.timer.timerSpeed = 1;
            mc.gameSettings.keyBindJump.pressed = false;
        }

        if (mc.thePlayer.isMoving()) {
            if (mc.thePlayer.fallDistance < 0.1) {
                mc.timer.timerSpeed = 1.81f;
            }
            if (mc.thePlayer.fallDistance > 0.2) {
                mc.timer.timerSpeed = 0.42f;
            }
            if (mc.thePlayer.fallDistance > 0.6) {
                mc.timer.timerSpeed = 1.05f;
                mc.thePlayer.speedInAir = 0.02019f;
            }
        }

        if (mc.thePlayer.fallDistance > 1) {
            mc.timer.timerSpeed = 1;
            mc.thePlayer.speedInAir = 0.02f;
        }
    }
}
