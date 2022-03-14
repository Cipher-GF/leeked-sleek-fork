package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.math.MathUtil
import today.sleek.client.utils.network.PacketSleepThread
import today.sleek.client.utils.network.PacketUtil
import today.sleek.client.utils.player.PlayerUtil

class Watchdog4: FlightMode("HypixelTest") {

    var dontgo = true
    var waiting = false

    override fun onUpdate(event: UpdateEvent?) {
        mc.thePlayer.posY = mc.thePlayer.prevPosY
        if (event!!.isPre) {
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
                PacketUtil.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.534, mc.thePlayer.posZ, false))
                PacketSleepThread.delayPacket(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.9432, mc.thePlayer.posZ, false), 300)
                mc.thePlayer.jump()
                waiting = true
            }
            if (waiting && mc.thePlayer.onGround) {
                event.posY
                event.isOnGround = true;
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
            mc.thePlayer.performHurtAnimation()
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