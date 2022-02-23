package me.kansio.client.modules.impl.movement.speed.misc

import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.modules.impl.movement.speed.SpeedMode
import me.kansio.client.utils.player.PlayerUtil

class BedlessNoobServer: SpeedMode("BridgerLand") {
    override fun onMove(event: MoveEvent) {
        if (mc.thePlayer.isMoving) {
            if (mc.thePlayer.onGround) {
                event.motionY = PlayerUtil.getMotion(0.42f).also {
                    mc.thePlayer.motionY = it
                }
                mc.timer.timerSpeed = 0.1f
                PlayerUtil.setMotion(event, 0.9)
            }
        }
        mc.timer.timerSpeed = 1f
    }
}