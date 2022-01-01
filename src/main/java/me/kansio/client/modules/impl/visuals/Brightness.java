package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;

public class Brightness extends Module {

    private float oldGamma;

    private ModeValue mode = new ModeValue("Mode", this, "Gamma", "Potion");

    public Brightness() {
        super("Brightness", ModuleCategory.VISUALS);
        register(mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValueAsString()) {
            case "Gamma":
                this.oldGamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 2000f;
                break;
            case "Potion":
                break;
        }
    }

    @Override
    public void onDisable() {
        switch (mode.getValueAsString()) {
            case "Gamma":
                mc.gameSettings.gammaSetting = this.oldGamma;
                break;
            case "Potion":
                break;
        }
    }

}
