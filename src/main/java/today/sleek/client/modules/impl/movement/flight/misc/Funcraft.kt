package today.sleek.client.modules.impl.movement.flight.misc

import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.player.PlayerUtil
import net.minecraft.potion.Potion
import kotlin.math.sqrt

// speed -= speed / 152
class Funcraft : FlightMode("Funcraft") {
    var thing = 0
    var speed = 2.5
    var boosted = false
    var lastDist = 0.0;

    override fun onUpdate(event: UpdateEvent?) {
        if (!event!!.isPre) {
            lastDist = sqrt((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX) + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ))
        }
        mc.thePlayer.motionY = 0.0.also {
            if (thing > 1) {
                mc.thePlayer.setPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY - 8E-6, mc.thePlayer.posZ
                )
            }
        }
        if (mc.thePlayer.isMoving && !mc.thePlayer.isCollidedHorizontally) {
            if (boosted || speed > 0.25) {
//                speed -= speed / 152
                mc.timer.timerSpeed = 1.8f

            }
            if (speed < 0.36) {
                mc.timer.timerSpeed = 1.34f

            }
            if (!boosted) {
                speed = 0.0
            }
        } else {
            speed = 0.0
        }
    }

    override fun onMove(event: MoveEvent?) {


//        ChatUtil.log("Before: $thing | $speed")
        ++thing
        when (thing) {
            1 -> {

                boosted = true
                event!!.motionY = 0.42.also { mc.thePlayer.motionY = it }
                val boost = if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 1.65 else 1.78
                speed = boost * PlayerUtil.getBaseSpeed()

            }
            2 -> {
                speed *= flight.speed.value
            }
            3 -> {
                val diff = 0.1 * (lastDist - PlayerUtil.getBaseSpeed())
                speed = lastDist - diff
            }
            else -> {//
                if (mc.thePlayer.isCollidedVertically || mc.theWorld.getCollidingBoundingBoxes(
                        mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, mc.thePlayer.motionY, 0.0)
                    ).size > 0
                )
                    thing = 1;

                speed -= speed / 152
            }
        }
//        ChatUtil.log("After: $thing | $speed")
//        event!!.motionY = 0.0; also {
//            mc.thePlayer.motionY = 0.0
//            mc.thePlayer.setPosition(
//                mc.thePlayer.posX, mc.thePlayer.posY - 8E-6, mc.thePlayer.posZ
//            )
////            ChatUtil.log("" + (mc.thePlayer.posY - 8E-6))
//        }
//        if (thingy == 2) {
//            speed *= if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) flight.speed.value - 0.3 else flight.speed.value
//            thingy = 3
//        }
//        if (mc.thePlayer.isMoving) {
//            if (mc.thePlayer.onGround) {
//                event.motionY = 0.42; also {
//                    mc.thePlayer.motionY = 0.42
//                    speed = 0.8
//                }
//                boosted = true
//
//            }
//        }
        // PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed) = Math.max(PlayerUtil.getBaseSpeed(), speed)
        PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed))


    }

    override fun onEnable() {
        boosted = false
        lastDist = 0.0
        thing = 0
    }
}