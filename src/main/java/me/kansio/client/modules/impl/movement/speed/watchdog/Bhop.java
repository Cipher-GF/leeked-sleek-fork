package me.kansio.client.modules.impl.movement.speed.watchdog;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;


public class Bhop extends SpeedMode {

    public Bhop() {
        super("Watchdog (Hop)");
    }

    @Override
    public void onEnable() {
        getSpeed().getHDist().set(0);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround) {
            double var1 = PlayerUtil.getBaseSpeed() * 1.0524;
            getSpeed().getHDist().set(var1);
        }

    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f));
        }
        double speed = getSpeed().handleFriction(getSpeed().getHDist());
        PlayerUtil.setMotion(speed);
    }


}