package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.TickEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.value.value.ModeValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleData(
        name = "Time Changer",
        category = ModuleCategory.VISUALS,
        description = "Changes the world time"
)
public class TimeChanger extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Day", "Noon", "Night", "Mid Night", "Spin", "Autism");
    private NumberValue<Integer> speed = new NumberValue("Speed", this, 1, 0, 10, 1, mode, "Spin");

    @Subscribe
    public void onTick(TickEvent event) {
        switch (mode.getValue()){
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
            case "Spin":
                long i = mc.theWorld.getWorldTime();
                if (i < 6050) {
                    i *= (speed.getValue() / 2F);
                } else if (i < 12100) {
                    i *= (speed.getValue() / 3F);
                } else {
                    i *= (speed.getValue() / 5F);
                }
                mc.theWorld.setWorldTime(i);
//                if (i > 24100) {
//                    mc.theWorld.setWorldTime(100);
//                }
                break;
            case "Autism":
                int d = (int) mc.theWorld.getWorldTime();
                d *= 5;
                System.out.println(d);
                mc.theWorld.setWorldTime(d);
                if (d > 24000) {
                    mc.theWorld.setWorldTime(0);
                }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }

}
