package today.sleek.client.modules.impl.movement.flight.verus

import net.minecraft.block.BlockAir
import net.minecraft.util.AxisAlignedBB
import today.sleek.base.event.impl.BlockCollisionEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.flight.FlightMode
import today.sleek.client.utils.player.PlayerUtil

class VerusDamage3 : FlightMode("VerusBoost") {

    var speed = 0.0
    var boosted = false

    override fun onEnable() {
        boosted = false
    }

    override fun onUpdate(event: UpdateEvent?) {
        PlayerUtil.setMotion(Math.max(speed, PlayerUtil.getVerusBaseSpeed()))
        if (!mc.thePlayer.onGround && mc.thePlayer.hurtTime > 0) {
            boosted = true
            speed = flight.speed.value
        } else if (mc.thePlayer.onGround && !boosted) {
            PlayerUtil.damageVerus()
        }
    }

    override fun onCollide(event: BlockCollisionEvent?) {
        if (boosted) {
            if (event!!.block is BlockAir) {
                if (mc.thePlayer.isSneaking) return
                val x = event!!.x.toDouble()
                val y = event!!.y.toDouble()
                val z = event!!.z.toDouble()
                if (y < mc.thePlayer.posY) {
                    event!!.axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(x, y, z)
                }
            }
        }
    }
}