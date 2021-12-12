package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Vanilla", "Test");
    private NumberValue speed = new NumberValue("Speed", this, 0.5, 0, 8, 0.1, false);

    public Speed() {
        super("Speed", Keyboard.KEY_G, ModuleCategory.MOVEMENT);
        register(mode, speed);
    }

    @Override
    public void onDisable() {
        PlayerUtil.setMotion(0);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValueAsString()) {
            case "Vanilla": {
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}
