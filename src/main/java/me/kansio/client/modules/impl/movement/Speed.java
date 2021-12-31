package me.kansio.client.modules.impl.movement;

import com.google.common.util.concurrent.AtomicDouble;
import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private final ModeValue mode = new ModeValue("Mode", this, "Vanilla", "VanillaHop", "Verus", "Verus2", "Ghostly", "Ghostly TP");
    private final NumberValue<Double> speed = new NumberValue<>("Speed", this, 0.5, 0.0, 8.0, 0.1);
    private final BooleanValue forceFriction = new BooleanValue("Force Friction", this, true);
    private final ModeValue frictionMode = new ModeValue("Friction", this, forceFriction, "NCP", "NEW", "LEGIT", "SILENT");
    private final AtomicDouble hDist = new AtomicDouble();

    public Speed() {
        super("Speed", ModuleCategory.MOVEMENT);
        register(mode, speed, forceFriction, frictionMode);
    }

    @Override
    public void onDisable() {
        PlayerUtil.setMotion(0);
        hDist.set(0);
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        switch (mode.getValueAsString()) {
            case "Ghostly": {
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    PlayerUtil.setMotion(speed.getValue().floatValue());
                } else {
                    PlayerUtil.setMotion(0.4f);
                }
                break;
            }
            case "Verus": {
                if (mc.thePlayer.onGround) {
                    event.setMotionY(mc.thePlayer.motionY = 0.42);
                }

                float sped2 = (float) (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.365 : 0.355);

                if (mc.thePlayer.hurtTime >= 1) {
                    sped2 = speed.getValue().floatValue();
                }

                PlayerUtil.setMotion(sped2);
                break;
            }
            case "Verus2": {
                if (mc.thePlayer.onGround) {
                    PlayerUtil.damageVerusNoMotion();
                    //event.setMotionY(handleFriction(hDist));
                }
                PlayerUtil.setMotion(event, (speed.getValue() >= 5.5 ? 5.4 : speed.getValue()));
                break;
            }
        }
    }


    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValueAsString()) {
            case "Vanilla": {
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
            case "VanillaHop": {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f);
                }
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
            case "Ghostly TP": {
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                double x = -Math.sin(yaw) * 1.8;
                double z = Math.cos(yaw) * 1.8;

                if (!mc.thePlayer.isMoving()) return;

                if (mc.thePlayer.ticksExisted % 5 == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                }
                break;
            }
            case "Verus2": {
                if (mc.thePlayer.onGround) {
                    hDist.set(speed.getValue());
                }
                break;
            }
        }
    }

    @Subscribe
    public void dortSaidToDoThisOkKansio(BlockCollisionEvent event) {
        switch (mode.getValue()) {
            case "Verus2": {
                if (event.getBlock() instanceof BlockAir) {
                    if (mc.thePlayer.isSneaking())
                        return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
                    }
                }
                break;
            }

        }
    }

    private double handleFriction(AtomicDouble atomicDouble) {
        if (forceFriction.getValue()) {
            double value = atomicDouble.get();
            switch (frictionMode.getValue()) {
                case "NCP":
                    atomicDouble.set(value - value / 159);
                    break;
                case "NEW":
                    atomicDouble.set(value * 0.98);
                    break;
                case "LEGIT":
                    atomicDouble.set(value * 0.91);
                    break;
                case "SILENT":
                    atomicDouble.set(value - 1.0E-9);
                    break;
            }
            return Math.max(atomicDouble.get(), PlayerUtil.getVerusBaseSpeed());
        }
        return this.speed.getValue();
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}
