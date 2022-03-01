package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.EntityLivingRenderEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@ModuleData(name = "Chams", category = ModuleCategory.VISUALS, description = "Shows players behind walls")
public class Chams extends Module {
    public BooleanValue fill = new BooleanValue("Fill", this, true);
    public NumberValue<Integer> alpha = new NumberValue<Integer>("Alpha", this, 255, 0, 255, 1);
    public NumberValue<Integer> r = new NumberValue<>("Red", this, 255, 0, 255, 1);
    public NumberValue<Integer> g = new NumberValue<Integer>("Green", this, 255, 0, 255, 1);
    public NumberValue<Integer> b = new NumberValue<Integer>("Blue", this, 255, 0, 255, 1);

    @Subscribe
    public void onRender(EntityLivingRenderEvent event) {
        if (event.isPre() && event.getEntity() instanceof EntityPlayer) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0F, -1100000.0F);
        } else if (event.isPost() && event.getEntity() instanceof EntityPlayer) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0F, 1100000.0F);
        }
    }

    @SuppressWarnings("all")
    public BooleanValue getFill() {
        return this.fill;
    }

    @SuppressWarnings("all")
    public NumberValue<Integer> getAlpha() {
        return this.alpha;
    }

    @SuppressWarnings("all")
    public NumberValue<Integer> getR() {
        return this.r;
    }

    @SuppressWarnings("all")
    public NumberValue<Integer> getG() {
        return this.g;
    }

    @SuppressWarnings("all")
    public NumberValue<Integer> getB() {
        return this.b;
    }
}
