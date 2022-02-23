package me.kansio.client.modules.impl.movement.flight.misc

import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.modules.impl.movement.flight.FlightMode
import me.kansio.client.utils.chat.ChatUtil
import me.kansio.client.utils.player.PlayerUtil

// speed -= speed / 152
class Funcraft : FlightMode("Funcraft") {
    var speed = 2.5
    var boosted = false

    override fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.isMoving) {
            if (boosted) {
//                speed -= speed / 152
                mc.timer.timerSpeed = 1.34f
                speed /= 1.0098f
            }
            if (!boosted) {
                speed = 0.0;
            }
        } else {
            speed = 0.0;
        }
    }

    override fun onMove(event: MoveEvent?) {

        event!!.motionY = 0.0; also {
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.setPosition(
                mc.thePlayer.posX, mc.thePlayer.posY - 8E-6, mc.thePlayer.posZ
            )
//            ChatUtil.log("" + (mc.thePlayer.posY - 8E-6))
        }
        if (mc.thePlayer.isMoving) {
            if (mc.thePlayer.onGround) {
                event.motionY = 0.42; also { mc.thePlayer.motionY = 0.42; }
                boosted = true;
                speed = flight.speed.value * 0.90;
            }
        }
        PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed))

    }

    override fun onEnable() {
        boosted = false;
    }
}