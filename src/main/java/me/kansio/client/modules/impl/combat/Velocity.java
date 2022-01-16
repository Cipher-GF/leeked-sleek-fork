package me.kansio.client.modules.impl.combat;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@ModuleData(
        name = "Velocity",
        category = ModuleCategory.COMBAT,
        description = "Allows you to modify your knockback"
)
public class Velocity extends Module {

    private NumberValue<Double> v = new NumberValue<>("Vertical", this, 100.0, 0.0, 100.0, 1.0);
    private NumberValue<Double> h = new NumberValue<>("Horizontal", this, 100.0, 0.0, 100.0, 1.0);
    private ModeValue modeValue = new ModeValue("Mode", this, "Packet");
    public BooleanValue explotion = new BooleanValue("Explosion", this, true);

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        switch (modeValue.getValueAsString()) {
            case "Packet": {
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = event.getPacket();
                    event.setCancelled(mc.theWorld != null && mc.thePlayer != null && mc.theWorld.getEntityByID(packet.getEntityID()) == mc.thePlayer);
                } else if (event.getPacket() instanceof S27PacketExplosion) {
                    if (explotion.getValue()) {
                        event.setCancelled(true);
                    }
                }
                break;
            }
        }

    }

    @Override
    public String getSuffix() {
        return " " + modeValue.getValueAsString();
    }
}
