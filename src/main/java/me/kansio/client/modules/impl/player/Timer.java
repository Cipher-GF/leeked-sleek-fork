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
    private NumberValue<Float> speed = new NumberValue<>("Speed", this, 1.0F, 0.05F, 10.0F, 0.1F);
    private NumberValue<Integer> tickspeed = new NumberValue("Ticks", this,1.0, 1.0, 20.0, 1.0, tick);

    @Override
    public void onEnable() {
        if (tick.getValue()) {
            TimerUtil.setTimer(speed.getValue(), tickspeed.getValue());
        } else {
            TimerUtil.setTimer(speed.getValue());
        }
    }

    @Override
    public void onDisable() {
        TimerUtil.Reset();
    }
}
