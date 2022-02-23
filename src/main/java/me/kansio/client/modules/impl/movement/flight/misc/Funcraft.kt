package me.kansio.client.modules.impl.movement.flight.misc

import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.modules.impl.movement.flight.FlightMode
import me.kansio.client.utils.chat.ChatUtil
import me.kansio.client.utils.player.PlayerUtil
import net.minecraft.potion.Potion

// speed -= speed / 152
class Funcraft : FlightMode("Funcraft") {
    var thingy = 1;
    var speed = 2.5
    var boosted = false

    override fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.isMoving) {
            if (boosted) {
//                speed -= speed / 152
                mc.timer.timerSpeed = 1.34f
                ChatUtil.log((speed / 130).toString())
                speed /= 1.0093f
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
        if (thingy == 2) {
            speed *= if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) flight.speed.value - 0.3 else flight.speed.value
            thingy = 3;
        }
        if (mc.thePlayer.isMoving) {
            if (mc.thePlayer.onGround) {
                event.motionY = 0.42; also { mc.thePlayer.motionY = 0.42; }
                boosted = true;
                if (thingy == 1) {
                    val boost: Double = if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 1.5 else 1.62
                    speed = boost * PlayerUtil.getBaseSpeed().toDouble() - 0.01
                    thingy = 2
                }
            }
        }
        // PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed) = Math.max(PlayerUtil.getBaseSpeed(), speed)
        PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed))

    }

    override fun onEnable() {
        boosted = false;
    }
}