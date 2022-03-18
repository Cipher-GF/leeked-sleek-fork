package today.sleek.client.modules.impl.movement.flight.misc

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.scripting.base.lib.Player
import today.sleek.client.modules.impl.movement.Flight
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.player.PlayerUtil
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.sqrt

// speed -= speed / 152
class OldNCP : FlightMode("test") {
    var thing = 0
    var speed = 2.5
    var boosted = false
    var lastDist = 0.0;

    override fun onUpdate(event: UpdateEvent?) {
        if (!event!!.isPre) {
            lastDist = sqrt((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX) + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ))
            return
        }
    }

    override fun onMove(event: MoveEvent?) {
        //PlayerUtil.setMotion(event, if (mc.thePlayer.hurtResistantTime != 19 && thing == 1) 0.011 else PlayerUtil.getBaseSpeed().toDouble().coerceAtLeast(speed))
        PlayerUtil.setMotion(event, ThreadLocalRandom.current().nextDouble(0.125,5.0))
    }

    override fun onEnable() {
        boosted = false
        lastDist = 0.0
        speed = 1.3
        thing = 0
    }
}