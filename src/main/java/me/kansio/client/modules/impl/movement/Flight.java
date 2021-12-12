package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Vanilla");
    private NumberValue speed = new NumberValue("Mode", this, 1, 0, 7, 0.1, false);

    public Flight() {
        super("Flight", Keyboard.KEY_F, ModuleCategory.MOVEMENT);
        register(modeValue, speed);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (modeValue.getValueAsString()) {
            case "Vanilla": {
                double motionY = 0;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    motionY = 0.4;
                }

                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    motionY = -0.4;
                }

                mc.thePlayer.motionY = motionY;
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
        }
    }

}
