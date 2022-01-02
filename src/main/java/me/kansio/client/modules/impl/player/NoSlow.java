package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.NoSlowEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;

public class NoSlow extends Module {

    public ModeValue mode = new ModeValue("Mode", this, "Vanilla", "IDk");
    public BooleanValue item = new BooleanValue("Item", this, true);
    //public BooleanValue sneak = new BooleanValue("Sneak", this, false);
    public BooleanValue water = new BooleanValue("Water", this, false);
    public BooleanValue soulsand = new BooleanValue("SoulSand", this, false);

    public NoSlow() {
        super("No Slow", ModuleCategory.PLAYER);
        register(mode, item, water, soulsand);
    }

    @Subscribe
    public void NoSlowEvent(NoSlowEvent event) {
        switch (event.getType()) {
            case ITEM:
                event.setCancelled(item.getValue());
                break;
            case WATER:
                event.setCancelled(water.getValue());
                break;
            case SOULSAND:
                event.setCancelled(soulsand.getValue());
                break;
        }
    }

}
