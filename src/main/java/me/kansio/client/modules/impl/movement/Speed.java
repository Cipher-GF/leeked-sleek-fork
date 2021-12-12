package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.player.PlayerUtil;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Vanilla", "Test");

    public Speed() {
        super("Speed", Keyboard.KEY_G, ModuleCategory.MOVEMENT);
        register(mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValueAsString()) {
            case "Vanilla": {
                PlayerUtil.setMotion(3);
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}
