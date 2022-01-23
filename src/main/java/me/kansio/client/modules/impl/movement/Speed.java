package me.kansio.client.modules.impl.movement;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.movement.speed.SpeedMode;
import me.kansio.client.gui.notification.Notification;
import me.kansio.client.gui.notification.NotificationManager;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.java.ReflectUtils;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleData(
        name = "Speed",
        category = ModuleCategory.MOVEMENT,
        description = "Move faster than normal."
)
public class Speed extends Module {

    private final List<? extends SpeedMode> modes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".speed", SpeedMode.class).stream()
            .map(aClass -> {
                try {
                    return aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            })
            .sorted(Comparator.comparing(speedMode -> speedMode != null ? speedMode.getName() : null))
            .collect(Collectors.toList());

    private final ModeValue mode = new ModeValue("Mode", this, modes.stream().map(SpeedMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private SpeedMode currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;

    @Getter private final NumberValue<Double> speed = new NumberValue<>("Speed", this, 0.5, 0.0, 8.0, 0.1);
    @Getter private final NumberValue<Float> timer = new NumberValue<>("Speed", this, 1.0F, 1.0F, 2.5F, 0.1F, mode, "Watchdog");
    @Getter private final BooleanValue forceFriction = new BooleanValue("Force Friction", this, true);
    @Getter private final ModeValue frictionMode = new ModeValue("Friction", this, forceFriction, "NCP", "NEW", "LEGIT", "SILENT");
    @Getter private final AtomicDouble hDist = new AtomicDouble();

    @Override
    public void onEnable() {
        currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
        currentMode.onEnable();
    }

    @Override
    public void onDisable() {
        TimerUtil.Timer(1.0f);
        PlayerUtil.setMotion(0);
        hDist.set(0);

        currentMode.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
        currentMode.onUpdate(event);
        if (mc.thePlayer.ticksExisted < 5) {
            if (isToggled()) {
                NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.INFO, "World Change!", "Speed disabled", 1));
                toggle();
            }
        }
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
        currentMode.onMove(event);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
        currentMode.onPacket(event);
    }
/*
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
                if (mc.thePlayer.isMoving()) {

                    if (mc.thePlayer.onGround) {
                        event.setMotionY(mc.thePlayer.motionY = 0.42);
                    }

                    float sped2 = (float) (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.365 : 0.355);

                    if (mc.thePlayer.hurtTime >= 1) {
                        sped2 = speed.getValue().floatValue();
                    }

                    PlayerUtil.setMotion(event, sped2);
                }
                break;
            }
            case "Verus Ground": {
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            mc.thePlayer.motionY = 0.0;
                            PlayerUtil.strafe(0.61F);
                            event.setMotionY(0.41999998688698);
                        }
                        PlayerUtil.strafe();
                    }
                }
                break;
            }
            case "TestPixel": {
                if (mc.thePlayer.isMovingOnGround()) {
                    double speed = handleFriction(hDist);

                    if (mc.thePlayer.moveStrafing != 0) {
                        ChatUtil.log("This flags");
                        return;
                    }

                    event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(0.42f));
                    PlayerUtil.setMotion(event, speed);
                }
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
            case "TestPixel": {
                hDist.set(PlayerUtil.getBaseSpeed() * 1.06575F);
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
        }
    }*/

    public double handleFriction(AtomicDouble atomicDouble) {
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
        return atomicDouble.get();
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}
