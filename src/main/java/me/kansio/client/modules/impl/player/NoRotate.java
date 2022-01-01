package me.kansio.client.modules.impl.player;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;

public class NoRotate extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Packet", "Client-Side");

    public NoRotate() {
        super("No Rotate", ModuleCategory.PLAYER);
        register(modeValue);
    }

}
