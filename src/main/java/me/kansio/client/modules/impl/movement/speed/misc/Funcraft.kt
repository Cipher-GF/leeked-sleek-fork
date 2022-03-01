package me.kansio.client.modules.impl.movement.speed.misc

import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.event.impl.PacketEvent
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.modules.impl.movement.speed.SpeedMode
import me.kansio.client.utils.player.PlayerUtil

class Funcraft : SpeedMode("Funcraft") {

    private var speed = 0.3

    override fun onUpdate(event: UpdateEvent) {
        super.onUpdate(event)
    }

    override fun onMove(event: MoveEvent) {
        speed = PlayerUtil.getVerusBaseSpeed();
        if (mc.thePlayer.isMovingOnGround) {
            mc.timer.timerSpeed = 1.1f
            event.motionY = 0.4025; also { mc.thePlayer.motionY = 0.4025 }
        }
        if (!mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 1.2f
            //speed -= speed / 152
            mc.thePlayer.motionX *= 0.9
            mc.thePlayer.motionX *= 0.9
        }
        PlayerUtil.setMotion(event, speed)
    }
}