package me.kansio.client.modules.impl.player.hackerdetect.checks.movement;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.BPSUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class FlightA extends Check {

    private Minecraft mc = Minecraft.getMinecraft();

    private HashMap<EntityPlayer, Integer> airTicks = new HashMap<>();

    @Override
    public String name() {
        return "Flight (Check 1)";
    }

    @Override
    public void onUpdate() {
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p == mc.thePlayer) {
                return;
            }

            double yDiff = p.posY - p.prevPosY;

            if (p.onGround) {
                airTicks.put(p, 0);
                return;
            }

            if (yDiff < -0.45) {
                airTicks.put(p, 0);
                return;
            }

            int ticks = airTicks.getOrDefault(p, 0);
            airTicks.put(p, ticks + 1);

            if (ticks > 35) {
                if (!Client.getInstance().getTargetManager().isTarget(p)) {
                    ChatUtil.logCheater(p + " §7might be using §aFlight (Check 1) §4§l(Flagged as cheater!)");
                    Client.getInstance().getTargetManager().getTarget().add(p.getName());
                }
            }
        }
    }
}
