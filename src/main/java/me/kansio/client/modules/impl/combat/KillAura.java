package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.Stopwatch;
import me.kansio.client.utils.combat.FightUtil;
import me.kansio.client.utils.network.PacketUtil;
import me.kansio.client.utils.rotations.AimUtil;
import me.kansio.client.utils.rotations.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import org.apache.commons.lang3.RandomUtils;

import javax.vecmath.Vector2f;
import java.util.*;

public class KillAura extends Module {

    public KillAura() {
        super("Killaura", ModuleCategory.COMBAT);

        register(
                //enum values:
                mode, moderotation, targetPriority, autoblockMode, swingMode,

                //sliders:
                reach, autoblockRange, cps, rand,

                //booleans
                autoblock, players, animals, monsters, invisible, walls, gcd, keepSprint
        );
    }

    public ModeValue mode = new ModeValue("Mode", this, "Switch", "Smart");
    public ModeValue moderotation = new ModeValue("Rotation Mode", this, "Default", "None", "Down", "NCP", "AAC", "GWEN");
    public ModeValue targetPriority = new ModeValue("Target Priority", this, "Health", "Distance", "Armor", "HurtTime", "None");
    public ModeValue autoblockMode = new ModeValue("Autoblock Mode", this,"Real", "Fake");
    public ModeValue swingMode = new ModeValue("Swing Mode", this, "Client", "None", "Server");
    public NumberValue<Double> reach = new NumberValue<>("Attack Range", this, 4.5, 2.5, 9.0, 0.1);
    public NumberValue<Double> autoblockRange = new NumberValue<>("Block Range", this,3.0, 1.0, 12.0, 0.1);
    public NumberValue<Double> cps = new NumberValue<>("CPS", this, 12.0, 1.0, 20.0, 1.0);
    public NumberValue<Double> rand = new NumberValue<>("Randomize CPS", this, 3.0, 0.0, 10.0, 1.0);
    public BooleanValue autoblock = new BooleanValue("Auto Block", this, true);
    public BooleanValue players = new BooleanValue("Players", this, true);
    public BooleanValue animals = new BooleanValue("Animals", this, true);
    public BooleanValue monsters = new BooleanValue("Monsters", this, true);
    public BooleanValue invisible = new BooleanValue("Invisibles", this, true);
    public BooleanValue walls = new BooleanValue("Walls", this, true);
    public BooleanValue gcd = new BooleanValue("GCD", this, false);
    public BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, false);

    public static EntityLivingBase target;

    public static boolean isBlocking, swinging;
    private int index;

    public final Stopwatch attackTimer = new Stopwatch();
    public Vector2f currentRotation = null;
    private boolean canBlock;
    private final Map<EntityLivingBase, Double> entityDamageMap = new HashMap<EntityLivingBase, Double>();

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
        swinging = false;
        currentRotation = null;
        target = null;
    }


    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (!keepSprint.getValue()) {
            if (target != null) {
                mc.thePlayer.setSprinting(false);
            }
        }

        List<EntityLivingBase> entities = FightUtil.getMultipleTargets(reach.getValue(), players.getValue(), animals.getValue(), monsters.getValue(), invisible.getValue(), walls.getValue());
        List<EntityLivingBase> blockRangeEntites = FightUtil.getMultipleTargets(autoblockRange.getValue(), players.getValue(), animals.getValue(), monsters.getValue(), invisible.getValue(), walls.getValue());

        entities.removeIf(e -> e.getName().contains("[NPC]"));

        ItemStack heldItem = mc.thePlayer.getHeldItem();

        canBlock = !blockRangeEntites.isEmpty() && autoblock.getValue()
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
                switch (autoblockMode.getValue()) {
                    case "Real": {
                        if (!event.isPre()) {
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());

                        }
                        break;
                    }

                    case "Fake": {
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
                        break;
                    }
                    case "switch": {
                        target = entities.get(index);
                        break;
                    }
                }
                aimAtTarget(event, moderotation.getValue(), target);
            }

            if (event.isPre()) {

                boolean canIAttack = attackTimer.timeElapsed((long) (1000L / cps.getValue()));

                if (canIAttack) {
                    if (cps.getValue() > 9) {
                        cps.setValue(cps.getValue() - RandomUtils.nextInt(0, rand.getValue().intValue()));
                    } else {
                        cps.setValue(cps.getValue() + RandomUtils.nextInt(0, rand.getValue().intValue()));
                    }
                    switch (mode.getValue()) {
                        case "Switch": {
                            if (canIAttack && attack(target, RandomUtils.nextInt(90, 100), autoblockMode.getValue())) {
                                index++;
                                attackTimer.resetTime();
                            }
                            break;
                        }
                        case "Smart": {
                            if (canIAttack && attack(target, RandomUtils.nextInt(90, 100), autoblockMode.getValue())) {
                                attackTimer.resetTime();
                            }
                        }
                    }
                }
            }
        }
/*
        if (target != null && event.isPre()) {

            if (mc.thePlayer.getDistanceToEntity(target) >= reach.getValue()) {
                swinging = false;
                return;
            }

            if (silent.getValue() && doAim.getValue()) {
                aimAtTarget(event, target);
            }

            swing(target);

            if (block.getValue() && autoblockMode.getValue().equalsIgnoreCase("Fake")) {
                isBlocking = true;
            }

            sendUseItem();
        }*/
    }

    private boolean attack(EntityLivingBase entity, double chance, String blockMode) {
        if (FightUtil.canHit(chance / 100)) {
            if (swingMode.getValue().equals("Client")) {
                mc.thePlayer.swingItem();
            }

            if (keepSprint.getValue()) {
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            }
            if ((blockMode.equalsIgnoreCase("real") && canBlock)) {
                blockHit(entity);
            }
            //doPartical(entity);
            //doCrack(entity);
            //doSound(entity);

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
            case "None":

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
        if (!keepSprint.getValue()) {
            if (event.getPacket() instanceof C0BPacketEntityAction) {
                event.setCancelled(true);
            }
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
        TargetHUD.draw(event, target);
    }

    public void blockHit(Entity target) {

    }

    private void unblock() {
        isBlocking = false;
    }

    private boolean checkEnemiesNearby() {
        for (Entity ent : mc.theWorld.playerEntities) {
            if (ent.getDistanceSqToEntity(mc.thePlayer) <= 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }

    public static boolean isSwinging() {
        return swinging;
    }
}