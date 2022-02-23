package me.kansio.client.modules.impl.movement.speed.misc

import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.event.impl.PacketEvent
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.modules.impl.movement.speed.SpeedMode
import me.kansio.client.utils.player.PlayerUtil

class Funcraft : SpeedMode("Funcraft") {

    private var speed = 0.3;
    // prob antidebug
    // its fixed now

    override fun onUpdate(event: UpdateEvent?) {
        super.onUpdate(event)
    }

    override fun onMove(event: MoveEvent?) {
        speed = PlayerUtil.getVerusBaseSpeed();
        if (mc.thePlayer.isMovingOnGround) {
            mc.timer.timerSpeed = 1.1f
            event!!.motionY = 0.4025; also { mc.thePlayer.motionY = 0.4025 }
            // bro my pfp is eviate LMFAO
            //speed += 0.02;
        }
        if (!mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 1.34f
//            speed -= speed / 152
            mc.thePlayer.motionX *= 0.9;
            mc.thePlayer.motionX *= 0.9;
        }
        PlayerUtil.setMotion(event!!, speed)
    }

    override fun onPacket(event: PacketEvent?) {
        super.onPacket(event)
    }

    override fun onEnable() {
        super.onEnable()
    }

    override fun onDisable() {
        speed = PlayerUtil.getBaseSpeed().toDouble();
    }
}