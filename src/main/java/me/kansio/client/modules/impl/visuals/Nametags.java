package me.kansio.client.modules.impl.visuals;


import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.Render3DEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.font.Fonts;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import me.kansio.client.utils.render.RenderUtil;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.awt.*;
import java.util.Objects;

@ModuleData(
        name = "Nametags",
        category = ModuleCategory.VISUALS,
        description = "Shows the nametags of the player"
)

public class Nametags extends Module {
    public BooleanValue health = new BooleanValue("Health", this, true);
    private NumberValue<Double> scaling = new NumberValue<>("Size", this, 2d, 2d, 10d, 0.25);
//    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    public BooleanValue invisibles = new BooleanValue("Invisibles",this,  false);
    public BooleanValue ping = new BooleanValue("Ping", this,true);
    public BooleanValue rect = new BooleanValue("Rectangle",this, true);
    public BooleanValue outline = new BooleanValue("Outline", this, false);
    public NumberValue<Float> lineWidth = new NumberValue("Line Width", this, 1.5f, 1f, 5f, 0.1);
    public BooleanValue sneak = new BooleanValue("SneakColor",this, false);
    public BooleanValue whiter = new BooleanValue("White",this,  false);
    public NumberValue<Double> scaleing = new NumberValue("Scaling", this, 0.1d, 0.1d, 5d, 0.1d);
    public NumberValue<Double> factor = new NumberValue("Factor", this, 1d, 1d, 10d, 0.1d);
    public BooleanValue smartScale = new BooleanValue("SmartScale", this, true);



    @Subscribe
    public void onRender3D(Render3DEvent event) {
        try {
            for (EntityPlayer player : Nametags.mc.theWorld.playerEntities) {
                if (player.equals(Nametags.mc.thePlayer) || !player.isEntityAlive() || player.isInvisible() && !this.invisibles.getValue()) {
                    continue;
                }
                double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX;
                double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY;
                double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y, z, event.getPartialTicks());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + Nametags.mc.getRenderManager().viewerPosX, y + Nametags.mc.getRenderManager().viewerPosY, z + Nametags.mc.getRenderManager().viewerPosZ);
        int width = Fonts.Verdana.getStringWidth(displayTag) /2;
//        System.out.println((0.0018d + this.scaling.getValue() * (distance * this.factor.getValue())) / 1000.0d);
        double scale = (this.scaling.getValue() * (distance * this.factor.getValue())) / 900d;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245d;
        }
//        if (this.scaling.getValue() == null) {
//            scale = this.scaling.getValue() / 100.0;
//        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) tempY + 1.4f, (float) z);
        GlStateManager.rotate(-Nametags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Nametags.mc.getRenderManager().playerViewX, Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue()) {
            RenderUtil.drawRect(-width - 2, -(15 + 1), (float) width + 2.0f * 2, 10f, 0x55000000);
            if (this.outline.getValue()) {
                final int color = new Color(255, 255, 255, 255).getRGB();
//                RenderUtil.drawBorderedRect((float) (-width - 2), (float) (-(mc.fontRendererObj.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, color);
                RenderUtil.drawBorderedRect(-width - 2, -(10), width + 2d, 1.5d, 2,color, color);
            }
        }
        GlStateManager.disableBlend();
        Fonts.Verdana.drawStringWithShadow(displayTag, -width, -(15 - 1), this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }




    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue()) {
            return name;
        }
        float health = player.getHealth();
        String color = health > 18.0f ? "\u00a7a" : (health > 16.0f ? "\u00a72" : (health > 12.0f ? "\u00a7e" : (health > 8.0f ? "\u00a76" : (health > 5.0f ? "\u00a7c" : "\u00a74"))));
        String pingStr = "";
        if (this.ping.getValue()) {
            try {
                NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(player.getUniqueID());
                int ping = (Objects.isNull(networkPlayerInfo) ? 0 : networkPlayerInfo.getResponseTime());
                pingStr = pingStr + ping + "ms ";
            } catch (Exception responseTime) {
                // empty catch block
            }
        }
        String popStr = " ";
        String idString = "";
        String gameModeStr = "";
        name = Math.floor(health) == (double) health ? name + color + " " + (health > 0.0f ? Integer.valueOf((int) Math.floor(health)) : "dead") : name + color + " " + (health > 0.0f ? Integer.valueOf((int) health) : "dead");
        return pingStr + idString + gameModeStr + name + popStr;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;
        if (this.whiter.getValue()) {
            colour = -1;
        }
        if (Client.getInstance().getFriendManager().isFriend(String.valueOf(player))) {
            return -11157267;
        }
        if (player.isInvisible()) {
            colour = -1113785;
        } else if (player.isSneaking() && this.sneak.getValue()) {
            colour = -6481515;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }
}

