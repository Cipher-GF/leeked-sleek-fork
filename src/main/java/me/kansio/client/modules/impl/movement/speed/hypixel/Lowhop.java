package me.kansio.client.modules.impl.movement.speed.hypixel;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.potion.Potion;

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
