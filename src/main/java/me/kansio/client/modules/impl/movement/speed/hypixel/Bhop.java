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
            double var1 = getBaseSpeed() * 1.0524;
            hDist.set(var1);
        }
        //event.setRotationYaw((float) PlayerUtil.getDirection());
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = getMotion(0.42f));
        }
        double speed = doFriction(hDist);
        setMotion(speed);
        mc.timer.timerSpeed = 1.2f;
    }

    public double doFriction(AtomicDouble hdist) {
        double value = hdist.get();
        hdist.set(value - value / 159);
        return Math.max(hdist.get(), getVerusBaseSpeed());
    }

    private double getVerusBaseSpeed() {
        double base = 0.2865;
        if (mc.thePlayer.isPotionActive(1)) {
            base *= 1.0 + 0.0495 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    private float getBaseSpeed() {
        float baseSpeed = 0.2873F;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0F + 0.2F * (amp + 1);
        }
        return baseSpeed;
    }

    private double getMotion(float baseMotionY) {
        Potion potion = Potion.jump;
        if (mc.thePlayer.isPotionActive(potion)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            baseMotionY += (amplifier + 1) * 0.1F;
        }

        return baseMotionY;
    }

    private void setMotion(double moveSpeed) {
        //EntityLivingBase entity = KillAura.currentTarget;
        EntityLivingBase entity = KillAura.target;
        TargetStrafe targetStrafeClass = (TargetStrafe) Client.getInstance().getModuleManager().getModuleByName("Target Strafe");
        boolean targetStrafe = targetStrafeClass.canStrafe();
        MovementInput movementInput = mc.thePlayer.movementInput;

        double moveForward = targetStrafe ? mc.thePlayer.getDistanceToEntity(entity) <= targetStrafeClass.radius.getValue().floatValue() ? 0 : 1 : movementInput.moveForward;
        double moveStrafe = targetStrafe ? TargetStrafe.dir : movementInput.moveStrafe;
        double rotationYaw = targetStrafe ? AimUtil.getRotationsRandom(entity).getRotationYaw() : mc.thePlayer.rotationYaw;

        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            mc.thePlayer.motionX = moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin;
            mc.thePlayer.motionZ = moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos;
        }
    }
}
