package me.kansio.client.modules.impl.visuals;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;
import org.lwjgl.input.Keyboard;

public class Configs extends Module {

    public Configs() {
        super("Configs", Keyboard.KEY_INSERT, ModuleCategory.HIDDEN);
    }

    @Override
    public void onEnable() {
        toggle();
    }
}
