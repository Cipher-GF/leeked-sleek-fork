package me.kansio.client.modules.impl.player

import me.kansio.client.modules.api.ModuleData
import me.kansio.client.modules.api.ModuleCategory
import me.kansio.client.value.value.ModeValue
import me.kansio.client.value.value.NumberValue
import com.google.common.eventbus.Subscribe
import me.kansio.client.Client
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.modules.impl.Module
import me.kansio.client.utils.block.BlockUtil
import net.minecraft.util.BlockPos
import net.minecraft.block.BlockAir
import me.kansio.client.modules.impl.movement.Flight

@ModuleData(
    name = "Anti Void",
    category = ModuleCategory.PLAYER,
    description = "Prevents you from falling into the void"
)
class AntiVoid : Module() {
    //values for storing the previous pos (when they weren't in the void)
    var prevX = 0.0
    var prevY = 0.0
    var prevZ = 0.0
    private val modeValue = ModeValue("Mode", this, "Basic", "Blink")
    private val fallDist: NumberValue<*> = NumberValue("Fall Distance", this, 7, 0, 30, 1)
    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        //save a safe position to teleport to
        if (BlockUtil.getBlockAt(
                BlockPos(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 1,
                    mc.thePlayer.posZ
                )
            ) !is BlockAir
        ) {
            prevX = mc.thePlayer.posX
            prevY = mc.thePlayer.posY
            prevZ = mc.thePlayer.posZ
        }

        //check if they should be teleported back to the safe position
        if (shouldTeleportBack()) {
            mc.thePlayer.setPositionAndUpdate(prevX, prevY, prevZ)

            //set the motion to 0
            mc.thePlayer.motionZ = 0.0
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionY = 0.0
        }
    }

    fun shouldTeleportBack(): Boolean {
        val flight = Client.instance().moduleManager.getModuleByName("Flight") as Flight

        //don't antivoid while using flight
        if (flight.isToggled) return false

        //don't teleport them if they're on the ground
        if (mc.thePlayer.onGround) return false

        //don't teleport them if they're collided vertically
        if (mc.thePlayer.isCollidedVertically) return false



        //check if fall distance is greater than fall distance required to tp back
        if (mc.thePlayer.fallDistance >= fallDist.value.toDouble()) {
            //loop through all the blocks below the player
            val valr = (mc.thePlayer.posY + 1).toInt();
            for (i in valr downTo 1) {
                val below = BlockUtil.getBlockAt(
                    BlockPos(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY - i,
                        mc.thePlayer.posZ
                    )
                ) as? BlockAir
                    ?: return false

                //if it's air this returns true.
            }
            //return true because there aren't any blocks under the player
            return true
        }
        return false
    }
}