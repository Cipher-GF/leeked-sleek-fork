package today.sleek.client.modules.impl.movement.flight.watchdog

import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.network.play.server.S12PacketEntityVelocity
import today.sleek.base.event.impl.BlockCollisionEvent
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.PacketEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.network.PacketUtil
import today.sleek.client.utils.player.PlayerUtil

class Test4: FlightMode("TEXTFLY") {
    var varTest1 = 0;
    var varTest2 = 0;
    var varTest3 = 0;
    var varTest4 = 0;
    override fun onUpdate(event: UpdateEvent?) {
        super.onUpdate(event)
    }

    override fun onMove(event: MoveEvent?) {
        super.onMove(event)
    }

    override fun onPacket(event: PacketEvent?) {
        super.onPacket(event)
    }

    override fun onCollide(event: BlockCollisionEvent?) {
        super.onCollide(event)
    }
}