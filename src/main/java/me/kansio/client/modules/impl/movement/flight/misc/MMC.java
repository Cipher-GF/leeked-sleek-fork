package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;

public class MMC extends FlightMode {
    public MMC() {
        super("MMC");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.gameSettings.keyBindJump.isKeyDown()) return;
        if (mc.gameSettings.keyBindSneak.isKeyDown()) return;
        TimerUtil.setTimer(0.1f);
        mc.thePlayer.motionY = 0;
        PlayerUtil.setMotion(5.0f);
    }

}
