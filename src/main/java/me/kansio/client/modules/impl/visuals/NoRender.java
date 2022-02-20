package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings;

import java.util.Random;

@ModuleData(
        name = "NoRender",
        category = ModuleCategory.VISUALS,
        description = "Items Dropped Have Physics"
)
public class NoRender extends Module {
    private final BooleanValue noWeather  = new BooleanValue("No Weather", this, false);
    private final BooleanValue items  = new BooleanValue("Items", this, false);


    @Subscribe
    public void onUpdate() {
        if (this.items.getValue()) {
            NoRender.mc.theWorld.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::setDead);
        }
        if (this.noWeather.getValue() && NoRender.mc.theWorld.isRaining()) {
            NoRender.mc.theWorld.setRainStrength(0.0f);
        }
    }


}
