package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.combat.KillAura;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.math.MathUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AttackEffect extends Module {

    // Particals
    public ModeValue mode = new ModeValue("Particle Mode", this, "Blood", "None");
    public NumberValue<Double> modenum = new NumberValue<>("Amount", this,1.0, 1.0, 20.0, 1.0);
    public BooleanValue modesound = new BooleanValue("Play Sound", this, false);

    // HitMarker Sound
    public BooleanValue hitmarkersound = new BooleanValue("Play Sound", this, false);
    public ModeValue hitmarkermode = new ModeValue("Sound Mode", this, hitmarkersound, "Blood", "Call Of Duty", "Skeet");

    // Crack Particals
    public BooleanValue crit = new BooleanValue("Criticals", this, false);
    public NumberValue<Double> critnum = new NumberValue<>("Amount", this,1.0, 1.0, 2.0, 1.0, crit);
    public BooleanValue ench = new BooleanValue("Enchants", this, false);
    public NumberValue<Double> enchnum = new NumberValue<>("Amount", this,1.0, 1.0, 2.0, 1.0, ench);

    public AttackEffect() {
        super("AttackEffect", ModuleCategory.VISUALS);
        register(mode, modenum, hitmarkersound, hitmarkermode, crit, critnum, ench, enchnum);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (KillAura.target != null && KillAura.target.hurtTime > 8 ){

                doParticle(KillAura.target);
                doCrack(KillAura.target);
                if (hitmarkersound.getValue()) {
                    doSound(KillAura.target);
                }

        }
    }

    public void doParticle(EntityLivingBase target) {
            if (crit.getValue()) {
                for (int i = 0; i < critnum.getValue(); i++) {
                    mc.thePlayer.onCriticalHit(target);
                }
            }

            if (ench.getValue()) {
                for (int i = 0; i < enchnum.getValue(); i++) {
                    mc.thePlayer.onEnchantmentCritical(target);
                }
            }
    }

    public void doCrack(EntityLivingBase target) {
        double x, y, z;
        x = target.posX;
        y = target.posY;
        z = target.posZ;

        switch (mode.getValue()){
            case "Blood":
                for (int i = 0; i < modenum.getValue(); i++) {
                    World targetWorld = target.getEntityWorld();
                    targetWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + MathUtil.getRandomInRange(-0.5, 0.5), y + MathUtil.getRandomInRange(-1, 1), z + MathUtil.getRandomInRange(-0.5, 0.5), 23, 23, 23, 152);
                }
                break;
            case "None":
                break;
        }

    }

    public void doSound(EntityLivingBase target) {

        double x, y, z;
        x = target.posX;
        y = target.posY;
        z = target.posZ;

        switch (hitmarkermode.getValue()) {
            case "Blood":
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), (float) x, (float) y, (float) z));
                break;
            case "Call Of Duty":
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("hitmarker.callofduty"), (float) x, (float) y, (float) z));
                break;
            case "Skeet":
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("hitmarker.skeet"), (float) x, (float) y, (float) z));
                break;
        }
    }
}
