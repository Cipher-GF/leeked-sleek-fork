package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.Stopwatch;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.network.PacketUtil;
import me.kansio.client.utils.rotations.AimUtil;
import me.kansio.client.utils.rotations.Rotation;
import me.kansio.client.utils.rotations.RotationUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.util.HashMap;
import java.util.Map;

public class KillAura extends Module {

    public KillAura() {
        super("Killaura", Keyboard.KEY_R, ModuleCategory.COMBAT);

        register(
                //enum values:
                mode, rotation, targetPriority, crackType, autoblockMode, swingMode,

                //sliders:
                reach, crackSize, cps, rand, smoothness,

                //booleans
                crack, randomizeCps, doAim, silent, minecraftRotation, keepSprint, block, monsters, sleeping, invisible, teleportReach, blood, gcd, allowInInventory, autoF5
        );
    }

    public ModeValue mode = new ModeValue("Mode", this, "Single");
    public ModeValue rotation = new ModeValue("Rotation Mode", this, "Default", "Down", "NCP", "AAC", "GWEN");
    public ModeValue targetPriority = new ModeValue("Target Priority", this, "Health", "Distance", "Armor", "None");
    public ModeValue autoblockMode = new ModeValue("Autoblock Mode", this, "Real", "Fake");
    public ModeValue crackType = new ModeValue("Crack Type", this, "Enchant", "Normal");
    public ModeValue swingMode = new ModeValue("Swing Mode", this, "Attack", "None", "Silent");
    public NumberValue crackSize = new NumberValue("Crack Size", this, 8, 1, 20, 1, true);
    public NumberValue cps = new NumberValue("CPS", this, 12, 1, 20, 1, true);
    public BooleanValue randomizeCps = new BooleanValue("Randomize CPS", this, true);
    public NumberValue rand = new NumberValue("Randomize", this, 3, 1, 10, 1, true);
    public BooleanValue doAim = new BooleanValue("Rotate", this, true);
    public NumberValue reach = new NumberValue("Attack Range", this, 4.5f, 2.5f, 9f, 0.1f, false);
    public NumberValue smoothness = new NumberValue("Smoothness", this, 5, 0, 100, 1, true);
    public BooleanValue rayCheck = new BooleanValue("Ray Check", this, true);
    public BooleanValue block = new BooleanValue("Auto Block", this, true);
    public BooleanValue monsters = new BooleanValue("Monsters", this, true);
    public BooleanValue sleeping = new BooleanValue("Sleeping", this, false);
    public BooleanValue invisible = new BooleanValue("Invisibles", this, true);
    public BooleanValue silent = new BooleanValue("Silent", this, true);
    public BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, false);
    public BooleanValue minecraftRotation = new BooleanValue("Minecraft Rotation", this, true);
    public BooleanValue crack = new BooleanValue("Crack", this, false);
    public BooleanValue blood = new BooleanValue("Blood Particles", this, false);
    public BooleanValue teleportReach = new BooleanValue("Teleport Reach", this, false);
    public BooleanValue gcd = new BooleanValue("GCD", this, false);
    public BooleanValue autoF5 = new BooleanValue("Auto F5", this, false);
    public BooleanValue allowInInventory = new BooleanValue("In Inventory", this, false);

    public static EntityLivingBase target;

    public static boolean isBlocking, swinging;


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
    private final Map<EntityLivingBase, Double> entityDamageMap = new HashMap<EntityLivingBase, Double>();

    private Rotation lastRotation;

    @Override
    public void onEnable() {
        this.attackTimer.resetTime();
    }

    @Override
    public void onDisable() {
        this.target = null;
        currentRotation = null;
        isBlocking = false;
        swinging = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        target = this.findTarget();
        if (target == null) {
            isBlocking = false;
        }
        if (target != null) {
            mc.thePlayer.setSprinting(this.keepSprint.getValue());

            if (!this.keepSprint.getValue()) {
                mc.gameSettings.keyBindSprint.pressed = false;
            }

            if (mc.thePlayer.getDistanceToEntity(target) >= reach.getValue()) {
                return;
            }

            if (!doAim.getValue()) return;

            if (!silent.getValue() && target != null && mc.objectMouseOver.entityHit != target) {
                Vector2f rotation = RotationUtil.getRotations(target);

                if (currentRotation == null)
                    currentRotation = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

                if (smoothness.getValue() > 0f) {
                    float yaw = RotationUtil.updateYawRotation(currentRotation.x, rotation.x,
                            Math.max(1, 180 * (1 - smoothness.getValue().floatValue() / 100)));
                    float pitch = RotationUtil.updatePitchRotation(currentRotation.y, rotation.y,
                            Math.max(1, 90f * (1 - smoothness.getValue().floatValue() / 100)));

                    rotation.x = yaw;
                    rotation.y = pitch;

                    currentRotation = rotation;
                }

                if (minecraftRotation.getValue()) rotation = RotationUtil.clampRotation(rotation);

                mc.thePlayer.rotationYaw = rotation.x;
                mc.thePlayer.rotationPitch = rotation.y;
            }
        }
    }

    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (autoF5.getValue()) {
            if (target == null) {
                mc.gameSettings.thirdPersonView = 0;
            } else {
                mc.gameSettings.thirdPersonView = 1;
            }
        }

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
        }
    }


    public EntityLivingBase findTarget() {
        EntityLivingBase currentTarget = null;

        double currentDistance = 0;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != null) {
                if (!this.isEntityValid(entity)) continue;
                if (!(entity instanceof EntityLivingBase)) continue;
                EntityLivingBase target = (EntityLivingBase) entity;
                if (currentTarget != null) {
                    switch (targetPriority.getValue()) {
                        case "Health": {
                            if (target.getHealth() > currentTarget.getHealth()) currentTarget = target;
                            break;
                        }
                        case "Distance": {
                            if (target.getDistanceToEntity(mc.thePlayer) < currentDistance) {
                                currentTarget = target;
                                currentDistance = currentTarget.getDistanceToEntity(mc.thePlayer);
                            }
                            break;
                        }
                        case "Armor": {
                            if (target.getTotalArmorValue() < currentTarget.getTotalArmorValue())
                                currentTarget = target;
                            break;
                        }
                        default:
                            return target;
                    }
                    ;
                } else {
                    currentTarget = target;
                    if (targetPriority.getValue().equalsIgnoreCase("Distance")) //So we don't unnecessary do distance checks
                        currentDistance = currentTarget.getDistanceToEntity(mc.thePlayer);
                }
            }
        }

        return currentTarget;
    }

    private boolean isEntityValid(Entity entity) {
        if (mc.thePlayer.isEntityEqual(entity)) return false;

        if (mc.thePlayer.getDistanceToEntity(entity) >= reach.getValue()) return false;

        if (!(entity instanceof EntityLivingBase))
            return false;

        if (!sleeping.getValue() && ((EntityLivingBase) entity).isPlayerSleeping())
            return false;

        if (entity.isInvisible() && !invisible.getValue())
            return false;

        if (entity instanceof EntityArmorStand)
            return false;

        if (entity.getName().equalsIgnoreCase("UPGRADES") || entity.getName().equalsIgnoreCase("SHOP"))
            return false;

        if (entity.isInvisible() && !invisible.getValue())
            return false;

        if (!monsters.getValue() && (entity instanceof EntityMob || entity instanceof EntityVillager))
            return false;

        return monsters.getValue() || entity instanceof EntityPlayer;
    }

    public boolean sendUseItem() {
        if (autoblockMode.getValue().equalsIgnoreCase("Real") && block.getValue()) {
            if (block.getValue() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                mc.playerController.syncCurrentPlayItem();
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                ItemStack itemstack = mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                if (itemstack != mc.thePlayer.getHeldItem() || itemstack != null) {
                    mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = itemstack;
                    if (itemstack.stackSize == 0)
                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                }
                return true;
            }
            return false;
        }
        return false;
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

    public void swing(Entity target) {
        swinging = true;
        double aps = cps.getValue() + MathUtil.getRandomInRange(0, rand.getValue());

        if (!allowInInventory.getValue() && mc.currentScreen != null) {
            return;
        }

        if (this.attackTimer.timeElapsed((long) (1000L / aps))) {
            this.attackTimer.resetTime();

            switch (swingMode.getValue()) {
                case "Attack": {
                    mc.thePlayer.swingItem();
                    break;
                }
                case "Silent": {
                    PacketUtil.sendPacket(new C0APacketAnimation());
                    break;
                }
            }

            final double distance = mc.thePlayer.getDistanceToEntity(target) - 0.5657;

            //spawn blood particles
            if (blood.getValue()) {
                for (int i = 0; i < this.crackSize.getValue(); i++) {
                    World targetWorld = target.getEntityWorld();
                    double x, y, z;
                    x = target.posX;
                    y = target.posY;
                    z = target.posZ;

                    targetWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + MathUtil.getRandomInRange(-0.5, 0.5), y + MathUtil.getRandomInRange(-1, 1), z + MathUtil.getRandomInRange(-0.5, 0.5), 23, 23, 23, 152);
                }
            }

            //This thing does the tp reach
            //Credits: Auth
            if (teleportReach.getValue() && distance > 2.5) {
                if (mc.theWorld.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() instanceof BlockAir) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(target.posX, target.posY, target.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
            } else {
                //do a normal hit if tp reach isnt on
                doCritical();
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            }
        }
    }

    public void aimAtTarget(UpdateEvent event, Entity target) {

        if (!(target instanceof EntityLivingBase)) {
            return;
        }

        Rotation rot = AimUtil.getRotationsRandom((EntityLivingBase) target);

        if (lastRotation == null) {
            lastRotation = rot;
            attackTimer.resetTime();
            return;
        }

        Rotation temp = rot;

        rot = lastRotation;

        switch (rotation.getValue()) {
            case "Default": {
                Vector2f rotation = RotationUtil.getRotations(target);

                if (smoothness.getValue() > 0f) {
                    if (currentRotation == null)
                        currentRotation = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
                    float yaw = RotationUtil.updateYawRotation(currentRotation.x, rotation.x,
                            Math.max(1, 180 * (1 - smoothness.getValue().floatValue() / 100f)));
                    float pitch = RotationUtil.updatePitchRotation(currentRotation.y, rotation.y,
                            Math.max(1, 90f * (1 - smoothness.getValue().floatValue() / 100f)));

                    rotation.x = yaw;
                    rotation.y = pitch;
                    currentRotation = rotation;
                }

                if (minecraftRotation.getValue()) rotation = RotationUtil.clampRotation(rotation);
                event.setRotationYaw(rotation.x);
                event.setRotationPitch(Math.min(Math.max(rotation.y, -90), 90));
                temp = new Rotation(rotation.x, Math.min(Math.max(rotation.y, -90), 90));
                break;
            }
            case "Down": {
                Vector2f rotation = RotationUtil.getRotations(target);
                temp = new Rotation(rotation.x, 90.0f);
                event.setRotationPitch(90.0F);
                event.setRotationYaw(rotation.x);
                break;
            }
            case "NCP": {
                lastRotation = temp = rot = Rotation.fromFacing((EntityLivingBase) target);
                event.setRotationYaw(rot.getRotationYaw());
                break;
            }
        }

        /*/if (Sulfur.getInstance().getModuleManager().get(gg.sulfur.client.impl.modules.render.Rotations.class).isToggled()) {
            mc.thePlayer.renderPitchHead = temp.getRotationPitch();
            mc.thePlayer.renderYawOffset = temp.getRotationYaw();
            mc.thePlayer.renderYawHead = temp.getRotationYaw();
        }/*/
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
        return " Single";
    }

    public static boolean isSwinging() {
        return swinging;
    }
}