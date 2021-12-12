package me.kansio.client.modules.impl.visuals;

import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.NumberValue;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    private NumberValue numberValue = new NumberValue("Test", this, 2, 1, 10);

    public ClickGUI() {
        super("Click GUI", Keyboard.KEY_RSHIFT, ModuleCategory.VISUALS);
        register(numberValue);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new me.kansio.client.clickgui.ui.clickgui.frame2.ClickGUI());
        toggle();
    }


}
