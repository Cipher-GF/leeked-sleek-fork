package me.kansio.client.modules.impl.movement.speed.hypixel;

import com.google.common.util.concurrent.AtomicDouble;
import me.kansio.client.Client;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.combat.KillAura;
import me.kansio.client.modules.impl.combat.TargetStrafe;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.player.PlayerUtil;

import me.kansio.client.utils.rotations.AimUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class Bhop extends SpeedMode {
    public Bhop() {
        super("Watchdog");
    }

    private final AtomicDouble hDist = new AtomicDouble();

    @Override
    public void onEnable() {
        hDist.set(0);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround) {
            double var1 = PlayerUtil.getBaseSpeed() * 1.0524;
            hDist.set(var1);
        }
        //event.setRotationYaw((float) PlayerUtil.getDirection());
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f));
        }
        double speed = doFriction(hDist);
        PlayerUtil.setMotion(speed);
        mc.timer.timerSpeed = getSpeed().getTimer().getValue();
    }

    public double doFriction(AtomicDouble hdist) {
        double value = hdist.get();
        hdist.set(value - value / 159);
        return Math.max(hdist.get(), PlayerUtil.getVerusBaseSpeed());
    }
}
