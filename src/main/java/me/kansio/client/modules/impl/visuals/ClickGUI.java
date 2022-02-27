package me.kansio.client.modules.impl.visuals;

import me.kansio.client.Client;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.ModeValue;
import me.kansio.client.value.value.NumberValue;
import me.kansio.client.value.value.StringValue;
import org.lwjgl.input.Keyboard;

@ModuleData(
        name = "Click GUI",
        category = ModuleCategory.VISUALS,
        description = "The click gui... nothing special (Credit: Wykt)",
        bind = Keyboard.KEY_RSHIFT
)
public class ClickGUI extends Module {

    public BooleanValue fonttoggle = new BooleanValue("Font", this, true);
    public ModeValue fontmode = new ModeValue("Mode", this, fonttoggle, "Verdana", "Lucida Sans", "Roobert");
    public NumberValue<Integer> animspeed = new NumberValue("Animation Speed", this, 50, 1, 100, 1);
    public BooleanValue rainbow = new BooleanValue("RGB OMG", this, false);


    @Override
    public void onEnable() {
        try {
            mc.displayGuiScreen(new me.kansio.client.gui.clickgui.frame.ClickGUI());
            toggle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
