package me.kansio.client.modules.impl.player;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.TimerUtil;

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
        TimerUtil.Timer(speed.getValue().floatValue());
    }

    @Override
    public void onDisable() {
        TimerUtil.Timer(1.0F);
    }
}
