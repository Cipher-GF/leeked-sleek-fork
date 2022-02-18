package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.modules.api.ModuleCategory;
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
        if (NoWeather.mc.theWorld.isRaining()) {
            NoWeather.mc.theWorld.setRainStrength(0.0f);
        }
    }
}
