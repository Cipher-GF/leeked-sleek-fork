package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.player.PlayerUtil

class Watchdog2 : FlightMode("Test") {

    var dontgo = true
    var waiting = false
    var speed = 0.6

    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.posY = mc.thePlayer.prevPosY
        if (event.isPre) {
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                waiting = true
            }
            if (waiting && mc.thePlayer.onGround) {
                event.posY -= 0.0784;
                event.isOnGround = true;
            }
            if (!waiting && !dontgo) {
                mc.thePlayer.motionY = 0.0;
                speed -= 0.05
            } else {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            }
        }
    }

    override fun onMove(event: MoveEvent) {
        if (!waiting && !dontgo) {
            PlayerUtil.setMotion(event, Math.max(PlayerUtil.getBaseSpeed().toDouble(), speed))
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
        speed = 0.5
        mc.timer.timerSpeed = flight.timer.value.toFloat()
    }
}