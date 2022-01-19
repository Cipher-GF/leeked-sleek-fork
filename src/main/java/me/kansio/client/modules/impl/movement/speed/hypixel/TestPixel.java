package me.kansio.client.modules.impl.movement.speed.hypixel;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.potion.Potion;

public class TestPixel extends SpeedMode {
    public TestPixel() {
        super("Test");
    }

    @Override
    public void onEnable() {
        getSpeed().getHDist().set(0);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround) {
            getSpeed().getHDist().set(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.42 : 0.35);
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.moveStrafing != 0) {
            mc.thePlayer.moveStrafing = 0;
            return;
        }
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f));
        }
        double speed = getSpeed().handleFriction(getSpeed().getHDist());
        PlayerUtil.setMotion(speed);
    }
}
