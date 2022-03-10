package today.sleek.client.modules.impl.combat

import com.google.common.eventbus.Subscribe
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSword
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.modules.ModuleData
import today.sleek.base.value.value.BooleanValue
import today.sleek.base.value.value.NumberValue
import today.sleek.client.modules.impl.Module
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.sqrt


/**
 * @author Kansio
 */
@ModuleData(
    name = "Aim Assist",
    description = "Assists you with aiming",
    category = ModuleCategory.COMBAT
)
class AimAssist : Module() {

    private val speed = NumberValue("Rotate Speed", this, 25.0, 1.0, 100.0, 1.0)
    private val range = NumberValue("Range", this, 4.0, 1.0, 10.0, 0.1)
    private val fov = NumberValue("FOV", this, 25.0, 1.0, 360.0, 1.0)

    private val players = BooleanValue("Only Players", this, true)
    private val click = BooleanValue("Click Aim", this, true)
    private val weapon = BooleanValue("Weapon Check", this, true)

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        if (mc.theWorld == null) {
            return
        }

        if (weapon.value) {
            if (mc.thePlayer.getCurrentEquippedItem() == null) {
                return
            }
            if (mc.thePlayer.getCurrentEquippedItem().getItem() !is ItemSword) {
                return
            }
        }

        if (this.click.value && !mc.gameSettings.keyBindAttack.isKeyDown) {
            return
        }

        val nearestTarget = getClosestTarget()
        if (nearestTarget != null && (isInFov(nearestTarget) > 1.0 || isInFov(nearestTarget) < -1.0)) {
            val b = isInFov(nearestTarget) > 0.0
            val thePlayer: EntityPlayerSP = mc.thePlayer
            thePlayer.rotationYaw += (if (b) -(Math.abs(isInFov(nearestTarget)) / this.speed.getValue() as Int) else Math.abs(
                isInFov(nearestTarget)
            ) / this.speed.getValue() as Int).toFloat()
        }

    }

    private fun isInFov(entityLivingBase: EntityLivingBase): Double {
        return ((mc.thePlayer.rotationYaw - lookAtTarget(entityLivingBase)) % 360.0 + 540.0) % 360.0 - 180.0
    }

    private fun getClosestTarget(): EntityLivingBase? {
        var o: EntityLivingBase? = null
        for (next in mc.theWorld.loadedEntityList) {
            o = if (next is EntityPlayer && this.players.value) {
                val entityPlayer: EntityPlayer = next
                if (entityPlayer === mc.thePlayer || !entityPlayer.isEntityAlive || entityPlayer.isInvisible || mc.thePlayer.getDistanceToEntity(
                        entityPlayer as Entity
                    ) >= range.value || !mc.thePlayer.canEntityBeSeen(entityPlayer as Entity) || !inFov(
                        entityPlayer as EntityLivingBase,
                        fov.value.toFloat()
                    )
                ) {
                    continue
                }
                entityPlayer
            } else {
                if (next !is EntityLivingBase || this.players.value) {
                    continue
                }
                val entityLivingBase = next
                if (entityLivingBase === mc.thePlayer || !entityLivingBase.isEntityAlive || entityLivingBase.isInvisible || mc.thePlayer.getDistanceToEntity(
                        entityLivingBase as Entity
                    ) >= range.getValue() || !mc.thePlayer.canEntityBeSeen(entityLivingBase as Entity) || !inFov(
                        entityLivingBase,
                        fov.value.toFloat()
                    )
                ) {
                    continue
                }
                entityLivingBase
            }
        }
        return o
    }

    fun inFov(entityLivingBase: EntityLivingBase, fov: Float): Boolean {
        var fovValue = fov
        fovValue *= 0.5.toFloat()
        val fovDouble: Double =
            ((mc.thePlayer.rotationYaw - lookAtTarget(entityLivingBase)) % 360.0 + 540.0) % 360.0 - 180.0
        return fovDouble > 0.0 && fovDouble < fovValue || -fovValue < fovDouble && fovDouble < 0.0
    }

    fun lookAtTarget(entityLivingBase: EntityLivingBase): Float {
        val n: Double = entityLivingBase.posX - mc.thePlayer.posX
        val n2: Double = entityLivingBase.posY - mc.thePlayer.posY
        val n3: Double = entityLivingBase.posZ - mc.thePlayer.posZ
        val n4 = -(atan2(n, n3) * 57.29577951308232)
        val n5 = -(asin(n2 / sqrt(n * n + n2 * n2 + n3 * n3)) * 57.29577951308232)
        return n4.toFloat()
    }

}