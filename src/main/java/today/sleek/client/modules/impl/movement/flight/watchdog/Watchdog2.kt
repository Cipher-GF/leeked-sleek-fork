package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.client.C00PacketKeepAlive
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C0FPacketConfirmTransaction
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.math.MathUtil
import today.sleek.client.utils.network.PacketUtil
import today.sleek.client.utils.player.PlayerUtil

class Watchdog2 : FlightMode("Hypixel2") {

    var dontgo = true
    var waiting = false
    var blinking = false
    var list = mutableListOf<Packet<*>>()

    override fun onUpdate(event: UpdateEvent?) {

//        mc.thePlayer.posY = mc.thePlayer.prevPosY
        if (event!!.isPre) {
            if ((dontgo && !waiting) && mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                PacketUtil.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.234, mc.thePlayer.posZ, true))
                waiting = true
            }
            if (waiting && mc.thePlayer.onGround) {
                event.posY -= 0.0784F + MathUtil.getRandomInRange(0.001f, 0.025f)
                event.isOnGround = true;

            }
            if (!waiting && !dontgo) {
                mc.thePlayer.motionY = 0.0;
            } else {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
            }
        }
        if (mc.thePlayer.ticksExisted % 5 == 0) {
            for (packet in list) {
                PacketUtil.sendPacketNoEvent(packet)
            }
            ChatUtil.log("${list.size}")
            list.clear()
            blinking = false
        } else {
            if (!dontgo && !waiting) {
                blinking = true
            }
        }
    }

    override fun onMove(event: MoveEvent?) {
        if (!waiting && !dontgo) {
            PlayerUtil.setMotion(event, PlayerUtil.getBaseSpeed().toDouble())
        } else {
            event!!.motionX = 0.0.also { mc.thePlayer.motionX = it }
            event.motionZ = 0.0.also { mc.thePlayer.motionZ = it }
        }
    }

    override fun onPacket(event: PacketEvent?) {
        val packet = event!!.getPacket<Packet<*>>()
        if (packet is S08PacketPlayerPosLook) {
            dontgo = false
            waiting = false
            mc.thePlayer.performHurtAnimation()
        }
        if (blinking) {
            when (packet) {
                is C00PacketKeepAlive -> {
                    event.isCancelled = true
                    list.add(packet)
                }
                is C03PacketPlayer -> {
                    event.isCancelled = true
                    list.add(packet)

                }
                is C0FPacketConfirmTransaction -> {
                    event.isCancelled = true
                    list.add(packet)
                }
            }
        }
    }

    override fun onEnable() {
        if (!mc.thePlayer.onGround) {
            flight.toggle()
        }
        dontgo = true
        waiting = false
        blinking = false
        list.clear()
        mc.timer.timerSpeed = flight.timer.value.toFloat()
    }

    override fun onDisable() {
        for (packet in list) {
            PacketUtil.sendPacketNoEvent(packet)
        }
    }
}