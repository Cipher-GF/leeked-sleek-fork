package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;

public class BridgerLand extends FlightMode {
    public BridgerLand() {
        super("BridgerLand");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        double motionY = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            motionY = 2;
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            motionY = -2;
        }

        mc.thePlayer.motionY = motionY;
        TimerUtil.setTimer(0.1f, 4);
        //works on german bw just got to value patch tmr
        if (mc.thePlayer.ticksExisted % 4 == 0 ) {
            PlayerUtil.setMotion(4);
        } else {
            PlayerUtil.setMotion(0);;
            mc.thePlayer.motionY = 0;
        }
    }

}
