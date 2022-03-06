package today.sleek.client.modules.impl.movement.speed.watchdog;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import today.sleek.base.event.impl.MoveEvent;
import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.client.modules.impl.movement.speed.SpeedMode;
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
                float f = mc.thePlayer.rotationYaw * 0.017453292F;
                if (!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    mc.thePlayer.jump();
                } else {
                    mc.thePlayer.motionX -= MathHelper.sin(f) * 0.21F;
                    mc.thePlayer.motionZ += MathHelper.cos(f) * 0.21F;
                }
                mc.thePlayer.motionY = 0.411;
            } else {
                if (mc.thePlayer.ticksExisted % 4 == 0) {
                    PlayerUtil.setMotion(PlayerUtil.getBaseSpeed());
                    mc.thePlayer.motionX *= 0.9;
                    mc.thePlayer.motionZ *= 0.9;
                }
            }
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}