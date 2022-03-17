package today.sleek.client.modules.impl.movement.flight.watchdog

import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.player.PlayerUtil

class WatchdogTest : FlightMode("Hypixel3") {

    var thing = 0;
    override fun onUpdate(event: UpdateEvent?) {
        if (event!!.isPre) {
            if (thing < 4) {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            } else {
                PlayerUtil.setMotion(PlayerUtil.getBaseSpeed().toDouble())
            }
            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ
            ChatUtil.log("$thing")
            when (thing) {
                1 -> {
                    ++thing
                    mc.thePlayer.motionY = 0.05
                }
                3 -> {
                    event.posY -= 0.22
                    thing = 4

                }
                4,5 -> {
                    ++thing
                    mc.thePlayer.motionY = -0.481
                }
                else -> {
                    ++thing
                    mc.thePlayer.motionY = 0.0
                    event.isOnGround = true

                    if (thing == 0)
                        thing = 1;
                    }
                }
            }
    }

    override fun onMove(event: MoveEvent?) {


    }

    override fun onPacket(event: PacketEvent?) {

    }

    override fun onEnable() {
        if (!mc.thePlayer.onGround) {
            flight.toggle()
        }
        thing = 0
    }
}