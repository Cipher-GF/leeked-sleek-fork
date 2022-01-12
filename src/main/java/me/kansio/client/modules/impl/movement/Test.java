package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.chat.ChatUtil;

@ModuleData(
        name = "test",
        category = ModuleCategory.MOVEMENT,
        description = "Test Module..."
)
public class Test extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "test", "test2");
    private BooleanValue booleanValue = new BooleanValue("A boolean", this, true);

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        ChatUtil.log("Test - onUpdate");
    }
}
