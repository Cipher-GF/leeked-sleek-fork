package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.math.MathUtil
import today.sleek.client.utils.player.PlayerUtil

class WatchdogTest: FlightMode("Hypixel3") {

    var dontgo = true
    var waiting = false

    override fun onUpdate(event: UpdateEvent?) {
        if (event!!.isPre) {
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.42
                waiting = true
            }
            if (waiting && mc.thePlayer.onGround) {
                event.posY -= 0.0784F + MathUtil.getRandomInRange(0.0005f, 0.0154f)
                event.isOnGround = true;
                waiting = false
                dontgo = false
            }
            if (!waiting && !dontgo) {
                mc.thePlayer.motionY = 0.0;
            } else {
                mc.thePlayer.motionX = 0.0
            }
        }
    }

    override fun onMove(event: MoveEvent?) {
        if (!waiting && !dontgo) {
            PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble())
//            PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed() + (0.1 * if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier else 0))
        } else {
            event!!.motionX = 0.0.also { mc.thePlayer.motionX = it }
            event.motionZ = 0.0.also { mc.thePlayer.motionZ = it }
        }
    }

    override fun onPacket(event: PacketEvent?) {
        if (event!!.getPacket<Packet<*>>() is S08PacketPlayerPosLook) {
            waiting = false
            dontgo = false
            mc.timer.timerSpeed = flight.timer.value.toFloat()
            
        }
    }

    override fun onEnable() {
        if (!mc.thePlayer.onGround) {
            flight.toggle()
        }
        dontgo = true
        waiting = false
    }
}