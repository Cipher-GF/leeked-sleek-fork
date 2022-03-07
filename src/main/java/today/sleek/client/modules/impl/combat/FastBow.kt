package today.sleek.client.modules.impl.combat

import today.sleek.base.modules.ModuleData
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.value.value.NumberValue
import today.sleek.base.value.value.ModeValue
import com.google.common.eventbus.Subscribe
import today.sleek.base.event.impl.UpdateEvent
import net.minecraft.item.ItemBow
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.network.play.client.C09PacketHeldItemChange
import today.sleek.base.value.value.BooleanValue
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.chat.ChatUtil
import today.sleek.client.utils.network.PacketUtil

@ModuleData(name = "Fast Bow", category = ModuleCategory.COMBAT, description = "Shoots your bow faster")
class FastBow : Module() {

    private var wasShooting = false
    private val lastSlot = 0
    private var serverSideSlot = 0

    private val packets = NumberValue("Packets", this, 20, 0, 1000, 1)
    private val value = BooleanValue("Hold Bow", this, true)
    private val mode = ModeValue("Mode", this, "Ghostly", "Vanilla")

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                if (value.value) {
                    if (mc.thePlayer.onGround && mc.thePlayer.currentEquippedItem != null && mc.thePlayer.currentEquippedItem.item is ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                        PacketUtil.sendPacketNoEvent(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                        when (mode.value) {
                            "Vanilla" -> {

                                //send the funny packets
                                var i = 0
                                while (i < packets.value) {
                                    PacketUtil.sendPacketNoEvent(C03PacketPlayer())
                                    i++
                                }
                            }
                            "Ghostly" -> {
                                if (mc.thePlayer.ticksExisted % 6 == 0) {
                                    val d = mc.thePlayer.posX
                                    val d2 = mc.thePlayer.posY + 1.0E-9
                                    val d3 = mc.thePlayer.posZ
                                    var i = 0
                                    while (i < packets.value) {
                                        mc.thePlayer.sendQueue.addToSendQueue(
                                            C06PacketPlayerPosLook(
                                                d,
                                                d2,
                                                d3,
                                                mc.thePlayer.rotationYaw,
                                                mc.thePlayer.rotationPitch,
                                                true
                                            )
                                        )
                                        i++
                                    }
                                }
                            }
                        }
                        PacketUtil.sendPacketNoEvent(
                            C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                BlockPos.ORIGIN,
                                EnumFacing.DOWN
                            )
                        )
                    }
                } else {
                    //check if they have a bow
                    if (!hasBow()) {
                        return
                    }
                    val slotWithBow = bowSlot

                    //this shouldn't happen
                    if (slotWithBow == -1) {
                        return
                    }
                    serverSideSlot = slotWithBow
                    serverSideSlot = slotWithBow
                    PacketUtil.sendPacketNoEvent(C09PacketHeldItemChange(slotWithBow))
                    PacketUtil.sendPacketNoEvent(
                        C08PacketPlayerBlockPlacement(
                            mc.thePlayer.inventory.getStackInSlot(
                                slotWithBow
                            )
                        )
                    )
                    when (mode.value) {
                        "Vanilla" -> {

                            //send the funny packets
                            var i = 0
                            while (i < packets.value.toInt()) {
                                PacketUtil.sendPacketNoEvent(C03PacketPlayer())
                                i++
                            }
                        }
                        "Ghostly" -> {
                            if (mc.thePlayer.ticksExisted % 5 == 0) {
                                val d = mc.thePlayer.posX
                                val d2 = mc.thePlayer.posY + 1.0E-9
                                val d3 = mc.thePlayer.posZ
                                var i = 0
                                while (i < packets.value) {
                                    mc.thePlayer.sendQueue.addToSendQueue(
                                        C06PacketPlayerPosLook(
                                            d,
                                            d2,
                                            d3,
                                            mc.thePlayer.rotationYaw,
                                            mc.thePlayer.rotationPitch,
                                            true
                                        )
                                    )
                                    i++
                                }
                            }
                        }
                    }
                    PacketUtil.sendPacketNoEvent(
                        C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN,
                            EnumFacing.DOWN
                        )
                    )
                    wasShooting = true
                }
            } else if (wasShooting) { //revert to the last itemslot
                PacketUtil.sendPacketNoEvent(C09PacketHeldItemChange(lastSlot))
                serverSideSlot = lastSlot
                wasShooting = false
                ChatUtil.log("revert")
            }
        }
    }

    fun hasBow(): Boolean {
        for (i in 0..7) {
            if (mc.thePlayer.inventory == null) continue
            if (mc.thePlayer.inventory.getStackInSlot(i) == null) continue
            if (mc.thePlayer.inventory.getStackInSlot(i).item == null) continue
            if (mc.thePlayer.inventory.getStackInSlot(i).item is ItemBow) {
                return true
            }
        }
        return false
    }

    //returns -1 if it can't find a bow
    val bowSlot: Int
        get() {
            for (i in 0..7) {
                if (mc.thePlayer.inventory.getStackInSlot(i) != null) {
                    if (mc.thePlayer.inventory.getStackInSlot(i).item is ItemBow) {
                        return i
                    }
                }
            }
            return -1
        }
}