package today.sleek.client.modules.impl.movement.speed.watchdog

import today.sleek.base.event.impl.UpdateEvent
import today.sleek.client.modules.impl.movement.speed.SpeedMode
import today.sleek.client.utils.math.MathUtil
import today.sleek.client.utils.player.PlayerUtil

class TestDog : SpeedMode("Test 3") {

    var speed = 0.8

    override fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.isMovingOnGround) {
            mc.thePlayer.motionY = MathUtil.getRandomInRange(0.032, 0.0556)
        }
        PlayerUtil.setMotion(PlayerUtil.getBaseSpeed().toDouble())
        mc.thePlayer.motionX *= 0.9
        mc.thePlayer.motionZ *= 0.9
    }


    override fun onEnable() {
    }
}