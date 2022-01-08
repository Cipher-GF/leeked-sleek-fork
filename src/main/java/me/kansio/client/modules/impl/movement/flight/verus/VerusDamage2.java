package me.kansio.client.modules.impl.movement.flight.verus;

import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;

public class VerusDamage2 extends FlightMode {

    public VerusDamage2() {
        super("Verus Damage 2");
    }

    public void onEnable() {

    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.hurtResistantTime < 1){
            if (mc.thePlayer.onGround) {
                PlayerUtil.damageVerus();
            }
        }
        if (mc.thePlayer.hurtResistantTime > 1) {
            double motionY;
            if (mc.gameSettings.keyBindJump.isPressed()) {
                motionY = getFlight().getSpeed().getValue() / 2;
            } else if (mc.gameSettings.keyBindSneak.isPressed()) {
                motionY = -getFlight().getSpeed().getValue() / 2;
            } else {
                motionY = 0;
            }

            event.setMotionY(motionY);

            PlayerUtil.setMotion(event, getFlight().getSpeed().getValue());
        }
    }
}
