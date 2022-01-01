package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.PlayerUtil;

public class LongJump extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Verus");

    //verus boost stuff
    private NumberValue vertical = new NumberValue("Vertical Boost", this, 0.8, 0.05, 6.0, 0.1, mode, "Verus");
    private NumberValue boost = new NumberValue("Speed", this, 1.45, 0.05, 10.0, 0.1, mode, "Verus");

    public LongJump() {
        super("Long Jump", ModuleCategory.MOVEMENT);
        register(mode, vertical, boost);
    }

    //Verus Highjump Variables
    boolean launched = false;
    boolean wasLaunched = false;
    boolean jumped = false;

    @Override
    public void onEnable() {
        if (mode.getValue().equalsIgnoreCase("verus")) {
            if (!mc.thePlayer.onGround) {
                toggle();
                return;
            }
            mc.timer.timerSpeed = 0.3f;
            PlayerUtil.damageVerus();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        jumped = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case "Verus": {
                if (mc.thePlayer.hurtTime > 1 && !launched) {
                    launched = true;
                }
                if (launched) {
                    if (!jumped) {
                        mc.thePlayer.motionY = vertical.getValue().doubleValue();
                        jumped = true;
                    }
                    PlayerUtil.setMotion(boost.getValue().floatValue());
                    launched = false;
                    wasLaunched = true;
                    toggle();
                }
                break;
            }
        }
    }
}
