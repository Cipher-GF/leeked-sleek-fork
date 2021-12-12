package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.chat.ChatUtil;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

    BooleanValue shit = new BooleanValue("Test", this, true);
    StringValue customString = new StringValue("sex", this, "this is funny thing");

    public Velocity() {
        super("Velocity", Keyboard.KEY_R, ModuleCategory.COMBAT);
        register(shit, customString);
    }

    @Override
    public void onEnable() {
        System.out.println("on enable got called lol");

        ChatUtil.log(shit.getValue().toString());
        ChatUtil.log(customString.getValue().toString());
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {

    }
}
