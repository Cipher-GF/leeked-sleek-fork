package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.java.ReflectUtils;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Flight extends Module {


    private final List<? extends FlightMode> modes = ReflectUtils.getRelects(this.getClass().getPackage().getName() + ".flight", FlightMode.class).stream()
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
    /*new ModeValue("Mode", this, "Vanilla", "Verus", "VerusDamage", "Verus Jump", "Funcraft", "Collide", "Ghostly", "Mush", "VerusGlide");*/
    @Getter private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1d, 0d, 10d, 0.1);
    @Getter private BooleanValue viewbob = new BooleanValue("View Bobbing", this, true);
    @Getter private BooleanValue boost = new BooleanValue("Boost", this, true, modeValue, "Funcraft");
    @Getter private BooleanValue extraBoost = new BooleanValue("Extra Boost", this, true, modeValue, "Funcraft");
    @Getter private BooleanValue glide = new BooleanValue("Glide", this, true, modeValue, "Funcraft");
    @Getter private ModeValue boostMode = new ModeValue("Boost Mode", this, modeValue, new String[]{"Funcraft"}, "Normal", "Damage", "WOWOMG");
    @Getter private NumberValue<Double> timer = new NumberValue<>("Timer", this, 1d, 1d, 5d, 0.1, modeValue, "Mush");

    Stopwatch stopwatch = new Stopwatch();

    public float ticks = 0;
    private int level;
    private double moveSpeed, lastDist;



    public Flight() {
        super("Flight", ModuleCategory.MOVEMENT);
        register(modeValue, speed, viewbob, boost, extraBoost, boostMode, timer);
    }


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
            mc.thePlayer.cameraYaw = 0.035f;
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
