package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class TimeChanger extends Module {

    private ModeValue time = new ModeValue("Mode", this, "Day", "Noon", "Night", "Mid Night");

    public TimeChanger() {
        super("TimeChanger", ModuleCategory.VISUALS);
        register(time);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (time.getValue()){
            case "Day":
                mc.theWorld.setWorldTime(1000);
                break;
            case "Noon":
                mc.theWorld.setWorldTime(13200);
                break;
            case "Night":
                mc.theWorld.setWorldTime(13000);
                break;
            case "Mid Night":
                mc.theWorld.setWorldTime(18000);
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

}
