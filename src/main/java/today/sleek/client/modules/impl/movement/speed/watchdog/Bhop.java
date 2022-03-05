package today.sleek.client.modules.impl.movement.speed.watchdog;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import today.sleek.base.event.impl.MoveEvent;
import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.client.modules.impl.movement.speed.SpeedMode;
import today.sleek.client.utils.chat.ChatUtil;
import today.sleek.client.utils.player.PlayerUtil;


public class Bhop extends SpeedMode {

    public Bhop() {
        super("Watchdog (Hop)");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.311;

                float f = mc.thePlayer.rotationYaw * 0.017453292F;
                if (!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    mc.thePlayer.motionX -= MathHelper.sin(f) * 0.09F;
                    mc.thePlayer.motionZ += MathHelper.cos(f) * 0.09F;
                } else {
                    mc.thePlayer.motionX -= MathHelper.sin(f) * 0.23F;
                    mc.thePlayer.motionZ += MathHelper.cos(f) * 0.23F;
                }
            }

            if (mc.thePlayer.fallDistance > 0.29) {
                //mc.thePlayer.motionY = -0.253;
            }
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}