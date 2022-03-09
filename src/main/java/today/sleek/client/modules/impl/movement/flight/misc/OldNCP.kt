package today.sleek.client.modules.impl.movement.flight.misc

import today.sleek.base.event.impl.MoveEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode

class OldNCP: FlightMode("OldNCP") {

    override fun onMove(event: MoveEvent?) {
        event!!.motionY= 0.0.also {
            mc.thePlayer.motionY = it
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1E-9, mc.thePlayer.posZ)
        }
    }
}