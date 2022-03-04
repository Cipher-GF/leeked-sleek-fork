package today.sleek.base.event.impl;
import today.sleek.base.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RenderOverlayEvent extends Event {

    private ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    public ScaledResolution getSr() {
        return sr;
    }
}