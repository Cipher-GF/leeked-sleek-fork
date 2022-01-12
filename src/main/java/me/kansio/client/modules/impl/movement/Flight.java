package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.java.ReflectUtils;
import me.kansio.client.utils.math.Stopwatch;
import net.minecraft.potion.Potion;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleData(
        name = "Flight",
        category = ModuleCategory.MOVEMENT,
        description = "Allows you to fly"
)
public class Flight extends Module {

    private final List<? extends FlightMode> modes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".flight", FlightMode.class).stream()
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

    private final ModeValue modeValue = new ModeValue("Mode", this, modes.stream().map(FlightMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private FlightMode currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(modeValue.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(modeValue.getValue())).findAny().get() : null;
    @Getter private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1d, 0d, 10d, 0.1);
    @Getter private BooleanValue viewbob = new BooleanValue("View Bobbing", this, true);
    @Getter private BooleanValue boost = new BooleanValue("Boost", this, true, modeValue, "Funcraft");
    @Getter private BooleanValue extraBoost = new BooleanValue("Extra Boost", this, true, modeValue, "Funcraft");
    @Getter private BooleanValue glide = new BooleanValue("Glide", this, true, modeValue, "Funcraft");
    @Getter private ModeValue boostMode = new ModeValue("Boost Mode", this, modeValue, new String[]{"Funcraft"}, "Normal", "Damage", "WOWOMG");
    @Getter private NumberValue<Double> timer = new NumberValue<>("Timer", this, 1d, 1d, 5d, 0.1, modeValue, "Mush");

    private Stopwatch stopwatch = new Stopwatch();

    public float ticks = 0;
    public float prevFOV = mc.gameSettings.fovSetting;
    private int level;
    private double moveSpeed, lastDist;

    public void onEnable() {
        this.currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(modeValue.getValue())) ? modes.stream().filter(speedMode -> speedMode.getName().equalsIgnoreCase(modeValue.getValue())).findAny().get() : null;
        currentMode.onEnable();
    }

    public void onDisable() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        mc.timer.timerSpeed = 1f;
        currentMode.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (viewbob.getValue() && mc.thePlayer.isMoving()) {
            mc.thePlayer.cameraYaw = 1;
            //mc.thePlayer.cameraYaw = 0.1f;
            //mc.thePlayer.cameraYaw = 1000f;
        } else {
            mc.thePlayer.cameraYaw = 0;
        }
        currentMode.onUpdate(event);
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        currentMode.onMove(event);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        currentMode.onPacket(event);
    }

    @Subscribe
    public void onCollide(BlockCollisionEvent event) {
        currentMode.onCollide(event);
    }

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public String getSuffix() {
        return " " + modeValue.getValueAsString();
    }
}
