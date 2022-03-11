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
                    if (mc.thePlayer.hurtTime > 1) {
                        mc.thePlayer.motionX -= MathHelper.sin(f) * 0.665F;
                        mc.thePlayer.motionZ += MathHelper.cos(f) * 0.665F;
                    } else {
                        mc.thePlayer.motionX -= MathHelper.sin(f) * 0.215F;
                        mc.thePlayer.motionZ += MathHelper.cos(f) * 0.215F;
                    }
                } else {
                    if (mc.thePlayer.hurtTime > 1) {
                        mc.thePlayer.motionX -= MathHelper.sin(f) * 0.425F;
                        mc.thePlayer.motionZ += MathHelper.cos(f) * 0.425F;
                    } else {
                        mc.thePlayer.motionX -= MathHelper.sin(f) * 0.265F;
                        mc.thePlayer.motionZ += MathHelper.cos(f) * 0.265F;
                    }
                }
                mc.thePlayer.motionY = 0.4181;
            } else {

            }
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}