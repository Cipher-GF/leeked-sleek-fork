package today.sleek.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.handshake.client.C00Handshake;
import today.sleek.base.event.impl.PacketEvent;
import today.sleek.base.event.impl.RenderOverlayEvent;
import today.sleek.base.modules.ModuleCategory;
import today.sleek.base.modules.ModuleData;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.utils.render.RenderUtils;

@ModuleData(
        name = "Playtime",
        category = ModuleCategory.VISUALS,
        description = "Shows how long you've been on for"
)
public class Playtime extends Module {

    public long currentTime;

    @Override
    public void onEnable() {
        currentTime = System.currentTimeMillis();
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C00Handshake) {
            currentTime = System.currentTimeMillis();
        }
    }

    @Subscribe
    public void onRender(RenderOverlayEvent event) {
        if (mc.currentScreen != null)
            return;

        long estimatedTime = System.currentTimeMillis() - currentTime;

        //totally not skidded from stackoverflow
        int seconds = (int) (estimatedTime / 1000) % 60 ;
        int minutes = (int) ((estimatedTime / (1000*60)) % 60);
        int hours   = (int) ((estimatedTime / (1000*60*60)) % 24);

        //draw on hud
        mc.fontRendererObj.drawStringWithShadow(hours + "h " + minutes + "m, " + seconds + "s", RenderUtils.getResolution().getScaledWidth() / 2 - 20, 20, -1);
    }
}
