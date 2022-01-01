package me.kansio.client.modules.impl.visuals;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;

public class DeathEffect extends Module {

    public ModeValue mode = new ModeValue("Effect Mode", this, "Blood", "Lightning");
    public BooleanValue soundon = new BooleanValue("Play Sound", this, false);

    public DeathEffect() {
        super("DeathEffect", ModuleCategory.VISUALS);
        register(mode, soundon);
    }
}