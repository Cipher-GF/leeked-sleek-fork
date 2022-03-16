package today.sleek.client.modules.impl.movement.flight.watchdog

import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.player.PlayerUtil

class Watchdog3 : FlightMode("Hypixel Test") {

    var stage = 0
    var uselessShit = false;

    override fun onUpdate(event: UpdateEvent?) {
        if (event!!.isPre()) {
            stage++
            if (stage >= 2) {
                mc.thePlayer.motionY = 0.0
                mc.thePlayer.cameraYaw = .1.toFloat()
                event.isOnGround = (true)
                PlayerUtil.setMotion(PlayerUtil.getBaseSpeed() * .9)
            }

            if (!uselessShit) {
                when (stage) {
                    1 -> {
                        mc.thePlayer.motionY = 0.05;
                    }
                    3 -> {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.22, mc.thePlayer.posZ);
                        mc.thePlayer.posY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                        mc.thePlayer.lastTickPosY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                    }
                    4 -> {
                        mc.thePlayer.motionY = -0.481009647894567;
                        mc.thePlayer.posY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                        mc.thePlayer.lastTickPosY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                    }
                    5 -> {
                        mc.thePlayer.motionY = -0.481009647894567;
                        mc.thePlayer.posY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                        mc.thePlayer.lastTickPosY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
                    }
                }
            }
        }
    }
}