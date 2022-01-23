package me.kansio.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.HashMap;

@ModuleData(
        name = "Hacker Detect",
        category = ModuleCategory.PLAYER,
        description = "Detects Cheaters Useing Client Side AC"
)
public class HackerDetect extends Module {

    public ModeValue theme = new ModeValue("Theme", this, "Sleek", "Verus", "AGC", "Sparky");

    @Getter
    private static HackerDetect instance;

    @Getter @Setter
    private double cageYValue;

    @Getter
    private HashMap<EntityPlayer, Integer> violations = new HashMap<>();

    public HackerDetect() {
        instance = this;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.ticksExisted < 5)
            violations.clear();

        for (Check c : Client.getInstance().getCheckManager().getChecks()) {
            c.onUpdate();
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        for (Check c : Client.getInstance().getCheckManager().getChecks()) {
            c.onPacket();
        }
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = event.getPacket();
            String msg = packet.getChatComponent().getFormattedText();

            if (msg.contains("Cages open in:")) {
                for (Check c : Client.getInstance().getCheckManager().getChecks()) {
                    c.onBlocksMCGameStartTick();
                }
            }
        }
    }

}
