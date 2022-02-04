package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.Render3DEvent;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.combat.AntiBot;
import me.kansio.client.utils.render.RenderUtil;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.ModeValue;
import me.kansio.client.utils.render.RenderUtils;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

//made by ohare

@ModuleData(
        name = "ESP",
        category = ModuleCategory.VISUALS,
        description = "Shows player locations"
)
public class ESP extends Module {

    private BooleanValue players = new BooleanValue("Players", this, true);
    private BooleanValue animals = new BooleanValue("Animals", this, true);
    private BooleanValue mobs = new BooleanValue("Mobs", this, false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", this, false);
    private BooleanValue passives = new BooleanValue("Passives", this, true);
    private BooleanValue box = new BooleanValue("Box", this, true);
    private BooleanValue health = new BooleanValue("Health", this, true);
    private BooleanValue armor = new BooleanValue("Armor", this, true);
    private BooleanValue filled = new BooleanValue("Filled", this, false);
    private BooleanValue corner = new BooleanValue("Corner", this, false);
    private NumberValue<Double> thickness = new NumberValue<>("Thickness", this, 1.5d, 0d, 10d, 0.25);
    private final NumberValue brightness = new NumberValue("Brightness", this, 255, 255, 255, 1);
    public final List collectedEntities;
    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;
    private final int backgroundColor;
    private final int black;

    public ESP() {
        this.collectedEntities = new ArrayList();
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
        this.backgroundColor = (new Color(0, 0, 0, 120)).getRGB();
        this.black = Color.BLACK.getRGB();
    }

    @Subscribe
    public void onRender2D(Render3DEvent event) {
        mc.theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && RenderUtil.isInViewFrustrum(ent)) {
                    double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                    double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                    double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    mc.entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        final Color clr = new Color(getColor(ent));
                        GL11.glPushMatrix();
                        final float x = (float) position.x;
                        final float w = (float) position.z - x;
                        final float y = (float) position.y + 3;
                        final float h = (float) position.w - y;
                        if (health.getValue()) {
                            RenderUtil.drawBar((float) (x - 3f - thickness.getValue() / 2), y - 1, 1.5f, h + 2, ((int)ent.getMaxHealth()) / 2, ((int)ent.getHealth()) / 2, getHealthColor(ent));
                        }
                        if (armor.getValue() && entity instanceof EntityPlayer) {
                            double armorstrength = 0;
                            EntityPlayer player = (EntityPlayer) entity;
                            for (int index = 3; index >= 0; index--) {
                                final ItemStack stack = player.inventory.armorInventory[index];
                                if (stack != null) {
                                    armorstrength += getArmorStrength(stack);
                                }
                            }
                            if (armorstrength > 0.0f) {
                                RenderUtil.drawBar((float) (x + w + 1.5f + thickness.getValue() / 2), y - 1, 1.5f, h + 2, 4, (int) (Math.min(armorstrength, 40) / 10),0xff5C7AFF);
                            }
                        }
                        if (filled.getValue())
                            RenderUtil.drawRect(x, y, w, h, new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), 120).getRGB());
                        if (box.getValue()) {
                            if (corner.getValue()) {
                                RenderUtil.drawCornerRect(x - 1.0 - thickness.getValue() / 2, y - 1.0 - thickness.getValue() / 2, w + 2 + thickness.getValue(), h + 2 + thickness.getValue(), thickness.getValue(), 0xff000000, true, 0.5f);
                                RenderUtil.drawCornerRect(x - 0.5f - thickness.getValue() / 2, y - 0.5f - thickness.getValue() / 2, w + 1 + thickness.getValue(), h + 1 + thickness.getValue(), thickness.getValue() - 1, clr.getRGB(), false, 0);
                            } else {
                                RenderUtil.drawBorderedRect(x - thickness.getValue() / 2, y - thickness.getValue() / 2, w + thickness.getValue(), h + thickness.getValue(), thickness.getValue() - 1, 0xff000000, 0);
                                RenderUtil.drawBorderedRect(x - 0.5 - thickness.getValue() / 2, y - 0.5 - thickness.getValue() / 2, w + 1 + thickness.getValue(), h + 1 + thickness.getValue(), thickness.getValue() - 1, clr.getRGB(), 0);
                                RenderUtil.drawBorderedRect(x - 1 - thickness.getValue() / 2, y - 1 - thickness.getValue() / 2, w + 2 + thickness.getValue(), h + 2 + thickness.getValue(), 0.5f, 0xff000000, 0);
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    private boolean isValid(EntityLivingBase entity) {
        return mc.thePlayer != entity && entity.getEntityId() != -1488 && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.getValue() && entity instanceof EntityPlayer) || ((mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime)) || (passives.getValue() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.getValue() && entity instanceof EntityAnimal));
    }

    private int getColor(EntityLivingBase ent) {
        if (Client.getInstance().getFriendManager().isFriend(ent.getName())) return new Color(83, 174, 253).getRGB();
        else if (ent.getName().equals(mc.thePlayer.getName())) return new Color(0xFF99ff99).getRGB();
        return (int) brightness.getValue();
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0F, 1.0F, 0.8f) | 0xFF000000;
    }


    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }
}