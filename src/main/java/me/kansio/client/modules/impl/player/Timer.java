package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.chat.ChatUtil;

public class Timer extends Module {

    private final BooleanValue tick = new BooleanValue("Tick Timer", this, false);
    private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1.0, 0.05, 10.0, 0.1);
    private NumberValue<Double> tickspeed = new NumberValue<>("Ticks", this, 1.0, 1.0, 20.0, 1.0);

    public Timer() {
        super("Timer", ModuleCategory.PLAYER);
        register(tick, speed, tickspeed);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.tick.getValue()) {
            if (mc.thePlayer.ticksExisted % tickspeed.getValue() == 0) {
                ChatUtil.log("tick");
                mc.timer.timerSpeed = speed.getValue().floatValue();
            } else {
                mc.timer.timerSpeed = 1;
            }
        } else {
            ChatUtil.log("timer");
            mc.timer.timerSpeed = speed.getValue().floatValue();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }
}
