package me.kansio.client.modules.impl.movement.speed.hypixel;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;

public class TestPixel extends SpeedMode {
    public TestPixel() {
        super("Test");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.onGround) {
            getSpeed().getHDist().set(PlayerUtil.getBaseSpeed() * 1.06575F);
        }
        if (mc.thePlayer.isMovingOnGround()) {
            double speed = getSpeed().handleFriction(getSpeed().getHDist());

            if (mc.thePlayer.moveStrafing != 0) {
                ChatUtil.log("This flags " + mc.thePlayer.moveStrafing);
                return;
            }

            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f));
            PlayerUtil.setMotion(event, speed);
        }
    }
}
