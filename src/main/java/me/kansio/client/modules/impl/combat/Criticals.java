package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.network.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Packet");

    public final double[] packetValues = new double[]{0.0625D, 0.0D, 0.05D, 0.0D};

    public Criticals() {
        super("Criticals", ModuleCategory.COMBAT);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
            final C02PacketUseEntity packetUseEntity = event.getPacket();
            final Entity entity = packetUseEntity.getEntityFromWorld(mc.theWorld);
            if (mc.thePlayer.onGround && entity.hurtResistantTime != -1) {
                doCritical();
                entity.hurtResistantTime = -1;
            }

        }
    }

    public void doCritical() {
        switch (mode.getValueAsString()) {
            case "Packet": {
                for (double d : packetValues) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, false));
                }
                break;
            }
        }
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }
}
