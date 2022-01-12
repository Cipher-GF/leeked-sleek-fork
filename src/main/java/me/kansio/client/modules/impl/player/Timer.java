package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.NumberValue;

@ModuleData(
        name = "Timer",
        category = ModuleCategory.PLAYER,
        description = "Changes your game speed"
)
public class Timer extends Module {

    private final BooleanValue tick = new BooleanValue("Tick Timer", this, false);
    private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1.0, 0.05, 10.0, 0.1);
    private NumberValue<Integer> tickspeed = new NumberValue("Ticks", this,1.0, 1.0, 20.0, 1.0, tick);

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = speed.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }
}
