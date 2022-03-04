package today.sleek.client.modules.impl.movement.speed.hypixel;

import today.sleek.base.event.impl.MoveEvent;
import today.sleek.client.modules.impl.movement.speed.SpeedMode;
import today.sleek.client.utils.chat.ChatUtil;
import today.sleek.client.utils.player.PlayerUtil;

public class Lowhop extends SpeedMode {
    public Lowhop() {
        super("Watchdog Lowhop");
    }

    @Override
    public void onMove(MoveEvent event) {
        if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())) {
            if (mc.thePlayer != null && mc.theWorld != null) {
                //if (mc.gameSettings.keyBindJump.pressed = false) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = .045;
                    PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed() * 1.5E-1);
                } else {
                    ChatUtil.log("nig");
                    PlayerUtil.setMotion(PlayerUtil.getBaseSpeed() * 1.05E-3);

                }
                //}
            }
        }
    }
}
