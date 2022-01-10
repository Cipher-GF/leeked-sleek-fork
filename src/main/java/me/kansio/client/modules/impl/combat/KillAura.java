package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.Sprint;
import me.kansio.client.notification.Notification;
import me.kansio.client.notification.NotificationManager;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.property.value.SubSettings;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.combat.FightUtil;
import me.kansio.client.utils.rotations.AimUtil;
import me.kansio.client.utils.rotations.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Cartesian;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

import javax.vecmath.Vector2f;
import java.util.*;

public class KillAura extends Module {

    public KillAura() {
        super("Killaura", ModuleCategory.COMBAT);
        registerSubSettings(attackSettings, renderSettings, targetSettings);
        register(
                //enum values:
                mode, rotatemode, targetPriority, autoblockmode, swingmode, targethudmode,

                //sliders:
                swingrage, autoblockRange, cps, cprandom,

                //booleans
                targethud, gcd, players, friends, animals, monsters, invisible, walls
        );
    }

    @Getter @Setter
    private static String focus = "Notch";

    // Switch aura doesn't work rn
    public ModeValue mode = new ModeValue("Mode", this, /*"Switch",*/ "Smart");

    public ModeValue targetPriority = new ModeValue("Target Priority", this, "None", "Distance", "Armor", "HurtTime", "Health");
    public ModeValue rotatemode = new ModeValue("Rotation Mode", this, "None", "Default", "Down", "NCP", "AAC", "GWEN");
    public NumberValue<Double> swingrage = new NumberValue<>("Swing Range", this, 3.0, 1.0, 9.0, 0.1);
    public NumberValue<Double> autoblockRange = new NumberValue<>("Block Range", this, 3.0, 1.0, 12.0, 0.1);
    public NumberValue<Double> cps = new NumberValue<>("CPS", this, 12.0, 1.0, 20.0, 1.0);
    public NumberValue<Double> cprandom = new NumberValue<>("Randomize CPS", this, 3.0, 0.0, 10.0, 1.0);
    public ModeValue swingmode = new ModeValue("Swing Mode", this, "Client", "Server");
    public ModeValue autoblockmode = new ModeValue("Autoblock Mode", this, "None", "Real", "Verus", "Fake");
    public BooleanValue gcd = new BooleanValue("GCD", this, false);
    private SubSettings attackSettings = new SubSettings("Attack Setting", targetPriority, rotatemode);

    public BooleanValue targethud = new BooleanValue("TargetHud", this, false);
    public ModeValue targethudmode = new ModeValue("TargetHud Mode", this, targethud, "Sleek", "Moon");
    private SubSettings renderSettings = new SubSettings("Render Setting", targethudmode);


    public BooleanValue players = new BooleanValue("Players", this, true);
    public BooleanValue friends = new BooleanValue("Friends", this, true);
    public BooleanValue animals = new BooleanValue("Animals", this, true);
    public BooleanValue monsters = new BooleanValue("Monsters", this, true);
    public BooleanValue invisible = new BooleanValue("Invisibles", this, true);
    public BooleanValue walls = new BooleanValue("Walls", this, true);
    private SubSettings targetSettings = new SubSettings("Target Setting", players, friends, animals, monsters, invisible, walls);

    public static EntityLivingBase target;
    public static boolean isBlocking, swinging;
    private int index;
    public final Stopwatch attackTimer = new Stopwatch();
    public Vector2f currentRotation = null;
    private boolean canBlock;
    private Rotation lastRotation;

    @Override
    public void onEnable() {
        index = 0;
        lastRotation = null;
        target = null;
        this.attackTimer.resetTime();
    }

    @Override
    public void onDisable() {
        if (isBlocking) unblock();
        isBlocking = false;
        mc.gameSettings.keyBindUseItem.pressed = false;
        swinging = false;
        currentRotation = null;
        target = null;

        if (!mc.thePlayer.isBlocking()) {
            isBlocking = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @Subscribe
    public void doHoldBlock(UpdateEvent event) {
        if (autoblockmode.getValue().equalsIgnoreCase("Hold")) {
            mc.gameSettings.keyBindUseItem.pressed = KillAura.target != null;
        }
    }


    @Subscribe
    public void onMotion(UpdateEvent event) {
        List<EntityLivingBase> entities = FightUtil.getMultipleTargets(swingrage.getValue(), players.getValue(), friends.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());

        if (mc.currentScreen != null) return;


        if (mc.thePlayer.ticksExisted < 5) {
            if (isToggled()) {
                NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.INFO, "World Change!", "Killaura disabled", 5));
                toggle();
            }
        }

        List<EntityLivingBase> blockRangeEntites = FightUtil.getMultipleTargets(autoblockRange.getValue(), players.getValue(), friends.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());

        entities.removeIf(e -> e.getName().contains("[NPC]"));

        ItemStack heldItem = mc.thePlayer.getHeldItem();

        canBlock = !blockRangeEntites.isEmpty()
                && heldItem != null
                && heldItem.getItem() instanceof ItemSword;

        if (event.isPre()) {
            target = null;
        }

        if (entities.isEmpty()) {
            index = 0;

            isBlocking = false;
        } else {
            if (index >= entities.size()) index = 0;

            if (canBlock) {
                switch (autoblockmode.getValue()) {
                    case "Real":
                        if (!event.isPre()) {
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                            isBlocking = true;
                        }
                        break;
                    case "Fake": {
                        isBlocking = true;
                        break;
                    }

                    case "Verus": {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        isBlocking = true;
                        break;
                    }
                }
            }


            if (event.isPre()) {
                switch (mode.getValue()) {
                    case "Smart": {
                        switch (targetPriority.getValue().toLowerCase()) {
                            case "distance": {
                                entities.sort(Comparator.comparingInt(e -> (int) -e.getDistanceToEntity(mc.thePlayer)));
                                break;
                            }
                            case "armor": {
                                entities.sort(Comparator.comparingInt(e -> -e.getTotalArmorValue()));
                                break;
                            }
                            case "hurttime": {
                                entities.sort(Comparator.comparingInt(e -> -e.hurtResistantTime));
                                break;
                            }
                            case "health": {
                                entities.sort(Comparator.comparingInt(e -> (int) -e.getHealth()));
                                break;
                            }
                        }
                        Collections.reverse(entities);
                        target = entities.get(0);
                        entities.forEach(entityLivingBase -> {
                            if (entityLivingBase.getName().equals(focus)) {
                                target = entities.get(entities.indexOf(entityLivingBase));
                            }
                        });
                        break;
                    }
                    case "switch": {
                        target = entities.get(index);
                        if (target == null) {
                            target = entities.get(0);
                        }
                        break;
                    }
                }
                aimAtTarget(event, rotatemode.getValue(), target);
            }

            if (event.isPre()) {

                boolean canIAttack = attackTimer.timeElapsed((long) (1000L / cps.getValue()));

                if (canIAttack) {
                    if (cps.getValue() > 9) {
                        cps.setValue(cps.getValue() - RandomUtils.nextInt(0, cprandom.getValue().intValue()));
                    } else {
                        cps.setValue(cps.getValue() + RandomUtils.nextInt(0, cprandom.getValue().intValue()));
                    }
                    switch (mode.getValue()) {
                        case "Switch": {
                            if (canIAttack && attack(target, RandomUtils.nextInt(90, 100), autoblockmode.getValue())) {
                                index++;
                                attackTimer.resetTime();
                            }
                            break;
                        }
                        case "Smart": {
                            if (canIAttack && attack(target, RandomUtils.nextInt(90, 100), autoblockmode.getValue())) {
                                attackTimer.resetTime();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean attack(EntityLivingBase entity, double chance, String blockMode) {
        if (FightUtil.canHit(chance / 100)) {
            if (swingmode.getValue().equalsIgnoreCase("client"))
                mc.thePlayer.swingItem();
            else if (swingmode.getValue().equalsIgnoreCase("server"))
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());

            mc.playerController.attackEntity(mc.thePlayer, entity);
            return true;
        } else {
            mc.thePlayer.swingItem();
        }
        return false;
    }

    public void aimAtTarget(UpdateEvent event, String mode, Entity target) {
        Rotation rotation = AimUtil.getRotationsRandom((EntityLivingBase) target);

        if (lastRotation == null) {
            lastRotation = rotation;
            attackTimer.resetTime();
            return;
        }

        Rotation temp = rotation;

        rotation = lastRotation;

        switch (mode.toUpperCase()) {
            case "DEFAULT":
                event.setRotationYaw(rotation.getRotationYaw());
                event.setRotationPitch(rotation.getRotationPitch());
                break;
            case "DOWN":
                temp = new Rotation(mc.thePlayer.rotationYaw, 90.0f);
                event.setRotationPitch(90.0F);
                break;
            case "NCP":
                lastRotation = temp = rotation = Rotation.fromFacing((EntityLivingBase) target);
                event.setRotationYaw(rotation.getRotationYaw());
                break;
            case "AAC":
                rotation = new Rotation(mc.thePlayer.rotationYaw, temp.getRotationPitch());
                event.setRotationPitch(rotation.getRotationPitch());
                break;
            case "GWEN":
                temp = mc.thePlayer.ticksExisted % 5 == 0 ? AimUtil.getRotationsRandom((EntityLivingBase) target) : lastRotation;
                event.setRotationYaw(temp.getRotationYaw());
                event.setRotationPitch(temp.getRotationPitch());
                break;
        }
        lastRotation = temp;
    }


    @Subscribe
    public void onPacket(PacketEvent event) {
        Packet packet = event.getPacket();
        if (isBlocking && ((packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) || packet instanceof C08PacketPlayerBlockPlacement)) {
            event.setCancelled(true);
        }
        if (packet instanceof C09PacketHeldItemChange) {
            isBlocking = false;
        }

        if (gcd.getValue() && target != null && event.getPacket() instanceof C03PacketPlayer && ((C03PacketPlayer) event.getPacket()).getRotating()) {
            C03PacketPlayer p = event.getPacket();
            float m = (float) (0.005 * mc.gameSettings.mouseSensitivity / 0.005);
            double f = m * 0.6 + 0.2;
            double gcd = m * m * m * 1.2;
            p.pitch -= p.pitch % gcd;
            p.yaw -= p.yaw % gcd;
        }
    }

    @Subscribe
    public void onRender(RenderOverlayEvent event) {
        if (target == null) {
            return;
        }
        if (targethud.getValue()) {
            TargetHUD.draw(event, target);
        }
    }

    private void unblock() {
        isBlocking = false;
    }

    public static boolean isSwinging() {
        return swinging;
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}