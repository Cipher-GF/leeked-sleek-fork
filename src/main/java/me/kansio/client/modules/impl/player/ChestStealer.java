package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.Stopwatch;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;

public class ChestStealer extends Module {

    private BooleanValue checkChest = new BooleanValue("Check Chest", this, true);
    private NumberValue<Integer> delay = new NumberValue<>("Delay", this, 25, 0, 1000, 1);
    private Stopwatch delayCounter = new Stopwatch();

    public ChestStealer() {
        super("ChestStealer", ModuleCategory.PLAYER);
        register(checkChest, delay);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            if (delayCounter.timeElapsed(delay.getValue().longValue())) {
                GuiChest chest = (GuiChest) mc.currentScreen;

                if (checkChest.getValue() && !chest.lowerChestInventory.getDisplayName().getUnformattedText().contains("Chest")) {
                    delayCounter.resetTime();
                    return;
                }



                delayCounter.resetTime();
            }
        }
    }

}
