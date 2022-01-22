package me.kansio.client.modules.impl.movement.speed.hypixel;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.potion.Potion;

public class Bhop extends SpeedMode {
    public Bhop() {
        super("Watchdog");
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
