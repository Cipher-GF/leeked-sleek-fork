package today.sleek.client.modules.impl.movement.flight.bridgerland;

import today.sleek.base.event.impl.PacketEvent;
import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.client.modules.impl.movement.flight.FlightMode;
import today.sleek.client.utils.player.PlayerUtil;

/**
 * @author Moshi
 * he made this after I told him how epic worked
 */


public class Teleport extends FlightMode {

    public Teleport() {
        super("BridgerLand (TP)");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            double motionY = 0;
            double speed = getFlight().getSpeed().getValue();

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                motionY = speed / 5;
            }

            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                motionY = -speed / 5;
            }

            if (!getFlight().getAntikick().getValue() && !mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0;
            }

            if (mc.thePlayer.ticksExisted % 3 == 0) {
                PlayerUtil.setMotion(speed);
                mc.thePlayer.motionY = motionY;
            } else {
                PlayerUtil.setMotion(0);
            }
        }
    }

    @Override
    public void onPacket(PacketEvent event) {
//        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
//            event.setCancelled(true);
//        }
    }
}
