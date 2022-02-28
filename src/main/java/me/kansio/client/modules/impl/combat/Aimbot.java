package me.kansio.client.modules.impl.combat;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.rotations.RotationUtil;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Kansio
 */
@ModuleData(
        name = "Aimbot",
        description = "Automatically aims at enemies",
        category = ModuleCategory.COMBAT
)
public class Aimbot extends Module {

    private EntityPlayer target;
    private Stopwatch speedLimit = new Stopwatch();
    public NumberValue Hspeed = new NumberValue("Horizontal Speed", this, 0.4f, 0.1f, 3.0f, 0.1f);
    public NumberValue Vspeed = new NumberValue("Vertical Speed", this, 0.4f, 0.1f, 3.0f, 0.1f);
    public NumberValue range = new NumberValue("Range", this, 4.2f, 1.0f, 6.0f, 0.1f);
    public NumberValue fov = new NumberValue("FoV", this, 60.0f, 1.0f, 360.0f, 10f);
    public BooleanValue AttackInvisible = new BooleanValue("Invisibles", this, true);
    public BooleanValue autodisable = new BooleanValue("Auto Disable", this, true);
    public BooleanValue checksword = new BooleanValue("Check Weapon", this, true);

    @Subscribe
    public void onUpdate(final UpdateEvent event) {

        this.target = this.getTarget();

        if (this.target == null || !speedLimit.timeElapsed(1) || this.target.getDistanceToEntity(this.mc.thePlayer) > 5.0)
            return;

        if (this.mc.objectMouseOver.entityHit != null)
            return;


        this.mc.thePlayer.rotationYaw = this.faceTarget(target, Hspeed.getValue().floatValue(), Vspeed.getValue().floatValue(), false)[0];
        speedLimit.resetTime();
    }

    private boolean isVaildToAttack(EntityPlayer entity) {
        if (entity == null || entity.isDead || entity.getHealth() <= 0)
            return false;

        float range = 4.2f;

        if (mc.thePlayer.getDistanceToEntity(entity) > range) {
            return false;
        }

        //modify
        if (entity.isInvisible() && !AttackInvisible.getValue())
            return false;

        if (fov.getValue().floatValue() != 360f && !RotationUtil.isVisibleFOV(entity, fov.getValue().floatValue()))
            return false;

        return true;
    }

    private EntityPlayer getTarget() {
        List<EntityPlayer> players = mc.theWorld.playerEntities.stream().filter(this::isVaildToAttack).sorted(Comparator.comparing(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());

        if (players.size() <= 0) {
            return null;
        }

        return players.get(0);
    }

    private float[] faceTarget(final Entity target, final float p_70625_2_, final float p_70625_3_, final boolean miss) {
        final double var4 = target.posX - this.mc.thePlayer.posX;
        final double var5 = target.posZ - this.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        }
        final Random rnd = new Random();
        final float offset = miss ? (rnd.nextInt(15) * 0.25f + 5.0f) : 0.0f;
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float) (Math.atan2(var5 + offset, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float) (-(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.5f : 0.0f) + offset, var8) * 180.0 / 3.141592653589793));
        final float pitch = changeRotation(this.mc.thePlayer.rotationPitch, var10, p_70625_3_);
        final float yaw = changeRotation(this.mc.thePlayer.rotationYaw, var9, p_70625_2_);
        return new float[]{yaw, pitch};
    }

    private float changeRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

}
