package me.kansio.client.modules.impl.visuals;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Animations extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "1.7", "Hide", "Slide", "Lucky", "DOWN", "Exhi", "oHare", "oHare2", "Wizzard", "Lennox", "ETB");
    public NumberValue<Double> slowdown = new NumberValue<>("Swing Speed", this, 1d, -4d, 12d, 1d);
    private float rotate;

    public Animations() {
        super("Animations", ModuleCategory.VISUALS);
        register(modeValue, slowdown);
    }

    public void render(ItemStack itemToRender, float partialTicks) {

        float f = 1.0F - (mc.getItemRenderer().prevEquippedProgress + (mc.getItemRenderer().equippedProgress - mc.getItemRenderer().prevEquippedProgress) * partialTicks);
        EntityPlayerSP entityplayersp = mc.thePlayer;
        float f1 = entityplayersp.getSwingProgress(partialTicks);
        float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * partialTicks;
        float f3 = entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * partialTicks;
        final float var = MathHelper.sin((float) (MathHelper.sqrt_float(f1) * Math.PI));
        switch (modeValue.getValue().toUpperCase()) {
            case "1.7":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(0, f1);
                    mc.getItemRenderer().func_178103_d();
                }
                break;
            case "NORMAL":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(0, 0.0f);
                    mc.getItemRenderer().func_178103_d();
                    float var8 = MathHelper.sin(f1 * f1 * 0.3215927f);
                    float var9 = MathHelper.sin(MathHelper.sqrt_float(0) * 0.3215927f);
                    GlStateManager.translate(-0.0f, -0f, 0.2f);
                }
                break;
            case "HIDE":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().func_178105_d(f1);
                    mc.getItemRenderer().transformFirstPersonItem(f, f1);
                }
                break;
            case "SLIDE":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(0, 0.0f);
                    mc.getItemRenderer().func_178103_d();
                    float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                    GlStateManager.translate(-0.05f, -0.0f, 0.35f);
                    GlStateManager.rotate(-var9 * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f);
                    GlStateManager.rotate(-var9 * (float) 70.0, 1.0f, -0.4f, -0.0f);
                }
                break;
            case "LUCKY":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(0, 0.0f);
                    mc.getItemRenderer().func_178103_d();
                    float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 0.3215927f);
                    GlStateManager.translate(-0.05f, -0.0f, 0.3f);
                    GlStateManager.rotate(-var9 * (float) 60.0 / 2.0f, -15.0f, -0.0f, 9.0f);
                    GlStateManager.rotate(-var9 * (float) 70.0, 1.0f, -0.4f, -0.0f);
                }
                break;
            case "DOWN":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(f1 - 0.125F, 0);
                    GlStateManager.rotate(-var * 55 / 2.0F, -8.0F, 0.4f, 9.0F);
                    GlStateManager.rotate(-var * 45, 1.0F, var / 2, -0.0F);
                    GlStateManager.translate(0.0f, 0.1F, 0.0f);
                    mc.getItemRenderer().func_178103_d();
                }
                break;
            case "EXHI":
                if (itemToRender.getItem() instanceof ItemSword) {
                    float f6 = MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
                    GL11.glTranslated(-0.1D, 0.1D, 0.0D);
                    // mc.getItemRenderer().transformFirstPersonItem(f / 3, 0.0f);
                    mc.getItemRenderer().transformFirstPersonItem(f / 2, 0.0f);
                    GlStateManager.rotate(-f6 * 40.0F / 2.0F, f6 / 2.0F, -0.0F, 9.0F);
                    GlStateManager.rotate(-f6 * 30.0F, 1.0F, f6 / 2.0F, -0.0F);
                    mc.getItemRenderer().func_178103_d();
                }
                break;
            case "OHARE2":
                if (itemToRender.getItem() instanceof ItemSword) {
                    mc.getItemRenderer().transformFirstPersonItem(f1, 0.0F);
                    mc.getItemRenderer().func_178103_d();
                    GlStateManager.translate(-0.05F, 0.6F, 0.3F);
                    GlStateManager.rotate(-var * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                    GlStateManager.rotate(-var * 70.0F, 1.5F, -0.4F, -0.0F);
                }
                break;
            case "OHARE":
                if (itemToRender.getItem() instanceof ItemSword) {
                    float f6 = MathHelper.sin((MathHelper.sqrt_float(f1) * 3.1415927f));
                    GL11.glTranslated(-0.05D, 0.0D, -0.25);
                    mc.getItemRenderer().transformFirstPersonItem(f / 2, 0.0f);
                    GlStateManager.rotate(-f6 * 60.0F, 2.0F, -f6 * 2, -0.0f);
                    mc.getItemRenderer().func_178103_d();
                }
                break;
            case "WIZZARD":
                if (itemToRender.getItem() instanceof ItemSword) {
                    float f6 = MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
                    mc.getItemRenderer().transformFirstPersonItem(f / 3, 0.0f);
                    GlStateManager.rotate(f6 * 30.0F / 1.0F, f6 / -1.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f6 * 10.0F / 10.0F, -f6 / -1.0F, 1.0F, 0.0F);
                    GL11.glTranslated(0.0D, 0.4D, 0.0D);
                    mc.getItemRenderer().func_178103_d();
                }
                break;
            case "LENNOX":
                if (itemToRender.getItem() instanceof ItemSword) {
                    float f6 = MathHelper.sin((float) (MathHelper.sqrt_float(f1) * 3.1));
                    GL11.glTranslated(0.0D, 0.125D, -0.1D);
                    mc.getItemRenderer().transformFirstPersonItem(f / 3, 0.0F);
                    GlStateManager.rotate(-f6 * 75.0F / 4.5F, f6 / 3.0F, -2.4F, 5.0F);
                    GlStateManager.rotate(-f6 * 75.0F, 1.5F, f6 / 3.0F, -0.0F);
                    GlStateManager.rotate(f6 * 72.5F / 2.25F, f6 / 3.0F, -2.7F, 5.0F);
                    mc.getItemRenderer().func_178103_d();

                }
                break;
            case "ETB":
                mc.getItemRenderer().transformFirstPersonItem(f, 0.0F);
                mc.getItemRenderer().func_178103_d();
                final float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                GlStateManager.rotate(-var9 * (float) 80.0 / 2.0f, -4.0f, -0.0f, 18.0f);
                GlStateManager.rotate(-var9 * (float) 70.0, 1.5f, -0.4f, -0.0f);
                break;
            case "SPIN":
                mc.getItemRenderer().transformFirstPersonItem(f, 0.0f);
                mc.getItemRenderer().func_178103_d();
                GL11.glRotatef(rotate, rotate, 0, rotate);
                GL11.glScalef(0.5f, 0.5f, 0.5F);
                //GL11.glTranslatef(0, 5, 0);
                rotate++;
                break;
        }
    }

    @Override
    public String getSuffix() {
        return " " + modeValue.getValue();
    }

}
