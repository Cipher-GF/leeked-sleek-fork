package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.gui.notification.Notification;
import me.kansio.client.gui.notification.NotificationManager;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;

public class MMC extends FlightMode {
    public MMC() {
        super("MMC");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isMoving()) {
            double motionY = 0;

            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.gameSettings.keyBindSneak.pressed = false;
            }

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.gameSettings.keyBindJump.pressed = false;
            }

            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                mc.gameSettings.keyBindLeft.pressed = false;
            }
            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                mc.gameSettings.keyBindRight.pressed = false;
            }

            mc.thePlayer.motionY = motionY;
            TimerUtil.setTimer(0.1f);
            PlayerUtil.setMotion(8.0f);
        } else  {
            TimerUtil.Reset();
        }
    }

}
