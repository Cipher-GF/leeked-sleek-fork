package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.network.PacketSleepThread
import today.sleek.client.utils.network.PacketUtil
import today.sleek.client.utils.player.PlayerUtil

class Watchdog3 : FlightMode("Watchdog3") {

    var dontgo = true
    var waiting = false
    var speed = 0.6

    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.posY = mc.thePlayer.prevPosY
        if (event.isPre) {
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                PacketUtil.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.234, mc.thePlayer.posZ, true))
                waiting = true
            }
            if (waiting && mc.thePlayer.onGround) {
                PacketUtil.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.534, mc.thePlayer.posZ, false))
                PacketSleepThread.delayPacket(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.9432, mc.thePlayer.posZ, false), 300)
                event.isOnGround = true
                mc.thePlayer.motionY = 0.434123
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
            //mc.thePlayer.performHurtAnimation()
            ChatUtil.log("s08")
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