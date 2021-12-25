package me.kansio.client.modules.impl.movement;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.NumberValue;

public class Timer extends Module {

    private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1.0, 0.05, 10.0, 0.1);

    public Timer() {
        super("Timer", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = speed.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }
}
