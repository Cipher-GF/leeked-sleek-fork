package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.PlayerUtil;

@ModuleData(
        name = "Long Jump",
        category = ModuleCategory.MOVEMENT,
        description = "Jump further than normal"
)
public class LongJump extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Verus", "Viper");

    //verus boost stuff
    private NumberValue vertical = new NumberValue("Vertical Boost", this, 0.8, 0.05, 6.0, 0.1, mode, "Verus");
    private NumberValue boost = new NumberValue("Speed", this, 1.45, 0.05, 10.0, 0.1, mode, "Verus");

    //Verus Highjump Variables
    boolean launched = false;
    boolean wasLaunched = false;
    boolean jumped = false;

    @Override
    public void onEnable() {
        switch (mode.getValue()) {
            case "Verus":
                if (!mc.thePlayer.onGround) {
                    toggle();
                    return;
                }

                mc.timer.timerSpeed = 0.3f;
                PlayerUtil.damageVerus();
                break;
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

    @Subscribe
    public void onMove(MoveEvent event) {
        switch (mode.getValue()) {
            case "Viper": {
                if (!mc.thePlayer.onGround) return;

                mc.timer.timerSpeed = 0.3f;
                if (mc.thePlayer.isMoving()) {
                    for (int i = 0; i < 17; ++i) {
                        PlayerUtil.TP(event, 0.37, 0);
                    }
                }
                break;
            }
        }
    }

}
