package me.kansio.client.modules.impl.movement.speed.misc;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;

public class NCPHop extends SpeedMode {
    public NCPHop() {
        super("NCPHop");
    }
    @Override
    public void onMove(MoveEvent event) {


        if (mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 1.8f;
            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.35f));
        }
        PlayerUtil.setMotion(event, 0.8);
        if (!mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 1.2f;
            event.setMotionX(mc.thePlayer.motionX * 0.98);
            event.setMotionZ(mc.thePlayer.motionZ * 0.98);
        }

    }
}
