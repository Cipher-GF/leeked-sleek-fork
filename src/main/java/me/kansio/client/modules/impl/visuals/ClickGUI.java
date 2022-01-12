package me.kansio.client.modules.impl.visuals;

import me.kansio.client.gui.external.ExternalClickGui;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import org.lwjgl.input.Keyboard;

@ModuleData(
        name = "Click GUI",
        category = ModuleCategory.VISUALS,
        description = "The click gui... nothing special"
)
public class ClickGUI extends Module {

    private BooleanValue font = new BooleanValue("Font", this, false);

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new me.kansio.client.gui.clickgui.ui.clickgui.frame.ClickGUI());
        toggle();
    }


}
