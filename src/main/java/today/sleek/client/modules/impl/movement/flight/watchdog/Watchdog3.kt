package today.sleek.client.modules.impl.movement.flight.watchdog

import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.player.PlayerUtil

class Watchdog3 : FlightMode("Hypixel Test") {

    var stage = 0
    var uselessShit = false;

    override fun onEnable() {
        stage = 0
        uselessShit = false
    }

    override fun onUpdate(event: UpdateEvent?) {
        if (event!!.isPre()) {
            ChatUtil.log("$stage")
            stage++
            if (stage >= 2 || uselessShit) {
                mc.thePlayer.motionY = 0.0
                mc.thePlayer.cameraYaw = .1.toFloat()
                event.isOnGround = (true)
                PlayerUtil.setMotion(PlayerUtil.getBaseSpeed() * .9)
            } else {

                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            }

            if (!uselessShit) {
                when (stage) {
                    1 -> {
                        mc.thePlayer.motionY = 0.05;
                    }
                    3 -> {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.22, mc.thePlayer.posZ);
                    }
                    4 -> {
                        mc.thePlayer.motionY = -0.481009647894567;
                    }
                    5 -> {
                        mc.thePlayer.motionY = -0.481009647894567;
                    }
                }
            }
        }
    }
}