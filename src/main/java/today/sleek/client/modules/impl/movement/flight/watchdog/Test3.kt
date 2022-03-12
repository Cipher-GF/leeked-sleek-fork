package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.player.PlayerUtil

class Test3 : FlightMode("Test 2") {

    var dontgo = true
    var waiting = false

    override fun onUpdate(event: UpdateEvent) {
//        mc.thePlayer.posY = mc.thePlayer.prevPosY
        if (event.isPre) {
            if (waiting && mc.thePlayer.onGround) {
                waiting = false
                dontgo = false
            }
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
//                mc.thePlayer.jump()
                mc.thePlayer.motionY = 0.1
                waiting = true
            }

            if (!waiting && !dontgo) {
                mc.thePlayer.motionY = 0.0;
            } else {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            }
        }
    }

    override fun onMove(event: MoveEvent) {
        if (!waiting && !dontgo) {
            PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble())
        } else {
            event.motionX = 0.0.also { mc.thePlayer.motionX = it }
            event.motionZ = 0.0.also { mc.thePlayer.motionZ = it }
        }
    }

    override fun onPacket(event: PacketEvent) {
        if (event.getPacket<Packet<*>>() is S08PacketPlayerPosLook) {
            waiting = false
            dontgo = false
            mc.thePlayer.performHurtAnimation()
        }
    }

    override fun onEnable() {
        if (!mc.thePlayer.onGround) {
            flight.toggle()
        }
        dontgo = true
        waiting = false
        mc.timer.timerSpeed = flight.timer.value.toFloat()
    }
}