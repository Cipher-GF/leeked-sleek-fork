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
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.combat.FightUtil;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.network.PacketUtil;
import me.kansio.client.utils.rotations.AimUtil;
import me.kansio.client.utils.rotations.Rotation;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.util.*;

public class KillAura extends Module {

    public KillAura() {
        super("Killaura", ModuleCategory.COMBAT);

        register(
                //enum values:
                mode, rotation, targetPriority, crackType, autoblockMode, swingMode,

                //sliders:
                reach, crackSize, cps, rand,

                //booleans
                crack, randomizeCps, keepSprint, block, players, animals, walls, monsters, invisible, teleportReach, blood, bloodsound, gcd, allowInInventory, autoF5
        );
    }

    public ModeValue mode = new ModeValue("Mode", this, "Switch", "Smart");
    public ModeValue rotation = new ModeValue("Rotation Mode", this, "Default", "Down", "NCP", "AAC", "GWEN");
    public ModeValue targetPriority = new ModeValue("Target Priority", this, "Health", "Distance", "Armor", "HurtTime", "None");
    public ModeValue autoblockMode = new ModeValue("Autoblock Mode", this, "Real", "Fake");
    public ModeValue crackType = new ModeValue("Crack Type", this, "Enchant", "Normal");
    public ModeValue swingMode = new ModeValue("Swing Mode", this, "Attack", "None", "Silent");
    public NumberValue<Integer> crackSize = new NumberValue<>("Crack Size", this, 8, 1, 20, 1);
    public NumberValue<Double> cps = new NumberValue<>("CPS", this, 12.0, 1.0, 20.0, 1.0);
    public BooleanValue randomizeCps = new BooleanValue("Randomize CPS", this, true);
    public NumberValue<Double> rand = new NumberValue<>("Randomize", this, 3.0, 1.0, 10.0, 1.0, randomizeCps);
    //public BooleanValue doAim = new BooleanValue("Rotate", this, true);
    public NumberValue<Double> reach = new NumberValue<>("Attack Range", this, 4.5, 2.5, 9.0, 0.1);
    public NumberValue<Double> blockRange = new NumberValue<>("Block Range", this, 3.0, 1.0, 12.0, 0.1);
    //public NumberValue<Integer> smoothness = new NumberValue<>("Smoothness", this, 5, 0, 100, 1);
    //public BooleanValue rayCheck = new BooleanValue("Ray Check", this, true);
    public BooleanValue block = new BooleanValue("Auto Block", this, true);
    public BooleanValue players = new BooleanValue("Players", this, true);
    public BooleanValue monsters = new BooleanValue("Monsters", this, true);
    public BooleanValue animals = new BooleanValue("Animals", this, true);
    public BooleanValue walls = new BooleanValue("Animals", this, true);
    //public BooleanValue sleeping = new BooleanValue("Sleeping", this, false);
    public BooleanValue invisible = new BooleanValue("Invisibles", this, true);
    //public BooleanValue silent = new BooleanValue("Silent", this, true);
    public BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, false);
    //public BooleanValue minecraftRotation = new BooleanValue("Minecraft Rotation", this, true);
    public BooleanValue crack = new BooleanValue("Crack", this, false);
    public BooleanValue blood = new BooleanValue("Blood Particles", this, false);
    public BooleanValue bloodsound = new BooleanValue("Blood Sound", this, false, blood);
    public BooleanValue teleportReach = new BooleanValue("Teleport Reach", this, false);
    public BooleanValue gcd = new BooleanValue("GCD", this, false);
    public BooleanValue autoF5 = new BooleanValue("Auto F5", this, false);
    public BooleanValue allowInInventory = new BooleanValue("In Inventory", this, false);

    public static EntityLivingBase target;

    public static boolean isBlocking, swinging;
    private int index;


    public final Stopwatch attackTimer = new Stopwatch();
    public Vector2f currentRotation = null;
    private float animated;
    //EntityLivingBase target;
    Object o;
    ScaledResolution lr;
    FontRenderer fontRenderer;
    String name;
    Double lastDamage;
    float modelWidth, sWidth, sHeight, middleX, middleY, top, xOffset, nameYOffset, nameHeight, scale, healthTextHeight, healthPercentage, width, height, half, left, right, bottom, textLeft, healthTextY, health, downScale, healthBarY, healthBarHeight, healthBarRight, dif, healthWidth, healthBarEnd, damage, damageWidth;
    String healthText;
    int fadeColor = 0;
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
        swinging = false;
        currentRotation = null;
        this.target = null;
    }


    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (!keepSprint.getValue()) {
            if (target != null) {
                mc.thePlayer.setSprinting(false);
            }
        }
        if (autoF5.getValue()) {
            if (target == null) {
                mc.gameSettings.thirdPersonView = 0;
            } else {
                mc.gameSettings.thirdPersonView = 1;
            }
        }

        List<EntityLivingBase> entities = FightUtil.getMultipleTargets(reach.getValue(), players.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());
        List<EntityLivingBase> blockRangeEntites = FightUtil.getMultipleTargets(blockRange.getValue(), players.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());

        entities.removeIf(e -> e.getName().contains("[NPC]"));

        ItemStack heldItem = mc.thePlayer.getHeldItem();

        canBlock = !blockRangeEntites.isEmpty() && block.getValue()
                && heldItem != null
                && heldItem.getItem() instanceof ItemSword;

        if (event.isPre()) {
            target = null;
        }

        if (entities.isEmpty()) {
            index = 0;

            if (isBlocking && !autoblockMode.getValue().equalsIgnoreCase("real")) {
                unblock();
            }
        } else {
            if (index >= entities.size()) index = 0;

            if (canBlock && !autoblockMode.getValue().equalsIgnoreCase("fake")) {
                if (!event.isPre()) {
                    blockHit(entities.get(index));
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
                aimAtTarget(event, rotation.getValue(), target);
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


    public void doCritical() {
        //DoCriticalEvent criticalEvent = new DoCriticalEvent();
        //Sulfur.getInstance().getEventBus().post(criticalEvent);

        if (this.crack.getValue()) {
            for (int i = 0; i < this.crackSize.getValue(); i++) {
                if (this.crackType.getValue().equalsIgnoreCase("Normal")) {
                    mc.thePlayer.onCriticalHit(target);
                }

                if (this.crackType.getValue().equalsIgnoreCase("Enchant")) {
                    mc.thePlayer.onEnchantmentCritical(target);
                }
            }
        }
    }

    private boolean attack(EntityLivingBase entity, double chance, String blockMode) {
        if (FightUtil.canHit(chance / 100)) {
            if (swingMode.getValue().equals("Attack")) {
                mc.thePlayer.swingItem();
            }

            if (keepSprint.getValue()) {
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            }
            doCritical();
            if (blood.getValue()) {
                doBlood(bloodsound.getValue(), target);
            }

            if ((blockMode.equalsIgnoreCase("real") && canBlock)) {
                blockHit(entity);
            }

            return true;
        } else {
            mc.thePlayer.swingItem();
        }
        return false;
    }

    private void doBlood(boolean sound, EntityLivingBase target) {

        double x, y, z;
        x = target.posX;
        y = target.posY;
        z = target.posZ;

        if (sound) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("dig.stone"), (float) x, (float) y, (float) z));
        }

        if (blood.getValue()) {
            for (int i = 0; i < this.crackSize.getValue(); i++) {
                World targetWorld = target.getEntityWorld();

                targetWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + MathUtil.getRandomInRange(-0.5, 0.5), y + MathUtil.getRandomInRange(-1, 1), z + MathUtil.getRandomInRange(-0.5, 0.5), 23, 23, 23, 152);
            }
        }

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
            case "NCP":
                lastRotation = temp = rotation = Rotation.fromFacing((EntityLivingBase) target);
                event.setRotationYaw(rotation.getRotationYaw());
                break;
            case "DEFAULT":
                event.setRotationYaw(rotation.getRotationYaw());
                event.setRotationPitch(rotation.getRotationPitch());
                break;
            case "DOWN":
                temp = new Rotation(mc.thePlayer.rotationYaw, 90.0f);
                event.setRotationPitch(90.0F);
                break;
            case "GWEN":
                temp = mc.thePlayer.ticksExisted % 5 == 0 ? AimUtil.getRotationsRandom((EntityLivingBase) target) : lastRotation;
                event.setRotationYaw(temp.getRotationYaw());
                event.setRotationPitch(temp.getRotationPitch());
                break;
            case "AAC":
                rotation = new Rotation(mc.thePlayer.rotationYaw, temp.getRotationPitch());
                event.setRotationPitch(rotation.getRotationPitch());
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
        mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
        PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
        isBlocking = true;
    }

    private void unblock() {
        PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
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
        if (mode.getValue().equals("Smart")) {
            return " Smart | " + targetPriority.getValue();
        }
        return " " + mode.getValue();
    }

    public static boolean isSwinging() {
        return swinging;
    }
}