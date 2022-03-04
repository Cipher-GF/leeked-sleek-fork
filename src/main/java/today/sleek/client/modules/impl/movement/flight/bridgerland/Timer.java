package today.sleek.client.modules.impl.movement.flight.bridgerland;

import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.client.modules.impl.movement.flight.FlightMode;
import today.sleek.client.utils.player.PlayerUtil;
import today.sleek.client.utils.player.TimerUtil;

public class Timer extends FlightMode {
    public Timer() {
        super("BridgerLand (Timer)");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        double motionY = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            motionY = 1;
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            motionY = -1;
        }

        mc.thePlayer.motionY = motionY;
        TimerUtil.setTimer(0.1f, 5);
        //works on german bw just got to value patch tmr
        if (mc.thePlayer.ticksExisted % 4 == 0) {
            PlayerUtil.setMotion(4);
        } else {
            PlayerUtil.setMotion(0);
            mc.thePlayer.motionY = 0;
        }
    }
}
