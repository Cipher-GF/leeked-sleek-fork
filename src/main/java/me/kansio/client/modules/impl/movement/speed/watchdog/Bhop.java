package me.kansio.client.modules.impl.movement.speed.watchdog;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;
import net.minecraft.potion.Potion;


public class Bhop extends SpeedMode {

    public Bhop() {
        super("Watchdog (Hop)");
    }

    @Override
    public void onEnable() {
        getSpeed().getHDist().set(0.37245 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.5 : 0));
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        getSpeed().getHDist().set(0.37245 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.5 : 0));
        event.setRotationYaw((float) PlayerUtil.getDirection());
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            return;
        }
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = 0.42f);
        }
        double speed = getSpeed().getHDist().get();
        PlayerUtil.setMotion(speed);
        if (!mc.thePlayer.onGround) {
            // for friction
            event.setMotionX(mc.thePlayer.motionX *= 0.80);
            event.setMotionZ(mc.thePlayer.motionZ *= 0.80);
        }
    }


}