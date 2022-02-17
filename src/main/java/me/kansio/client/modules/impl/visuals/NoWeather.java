package me.kansio.client.modules.impl.visuals;

import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;

@ModuleData(
        name = "NoWeather",
        category = ModuleCategory.VISUALS,
        description = "Dont render weather"
)
public class NoWeather extends Module {
    
    @Subscribe
    public void onUpdate() {
        if (this.Null()) {
            return;
        }
        if (NoWeather.mc.world.isRaining()) {
            NoWeather.mc.world.setRainStrength(0.0f);
        }
    }
}
