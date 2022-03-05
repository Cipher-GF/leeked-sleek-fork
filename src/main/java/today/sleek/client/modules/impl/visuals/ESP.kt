package today.sleek.client.modules.impl.visuals

import com.google.common.eventbus.Subscribe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityGolem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import org.lwjgl.opengl.GL11
import today.sleek.base.event.impl.RenderOverlayEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.modules.ModuleData
import today.sleek.base.value.value.BooleanValue
import today.sleek.base.value.value.NumberValue
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.render.RenderUtil
import java.awt.Color
import java.util.function.Consumer
import javax.vecmath.Vector3d
import javax.vecmath.Vector4d

@ModuleData(
    name = "ESP",
    category = ModuleCategory.VISUALS,
    description = "Shows player locations"
)
class ESP : Module() {

    private val players = BooleanValue("Players", this, true)
    private val animals = BooleanValue("Animals", this, true)
    private val mobs = BooleanValue("Mobs", this, false)
    private val invis = BooleanValue("Invisibles", this, false)
    private val passive = BooleanValue("Passive", this, true)

    private val filled = BooleanValue("Filled", this, false)

    private val r: NumberValue<*> = NumberValue("Red", this, 255, 1, 255, 1)
    private val g: NumberValue<*> = NumberValue("Green", this, 255, 1, 255, 1)
    private val b: NumberValue<*> = NumberValue("Blue", this, 255, 1, 255, 1)

    val collectedEntities: List<Entity> = ArrayList()

    @Subscribe
    fun onRenderOverlay(event: RenderOverlayEvent) {
        mc.theWorld.loadedEntityList.forEach(Consumer { entity: Entity ->
            if (entity is EntityLivingBase) {
                if (isValid(entity) && RenderUtil.isInViewFrustrum(entity)) {
                    val posX =
                        RenderUtil.interpolate(entity.posX, entity.lastTickPosX, mc.timer.renderPartialTicks.toDouble())
                    val posY =
                        RenderUtil.interpolate(entity.posY, entity.lastTickPosY, mc.timer.renderPartialTicks.toDouble())
                    val posZ =
                        RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, mc.timer.renderPartialTicks.toDouble())

                    val width = entity.width / 1.5
                    val height = entity.height + if (entity.isSneaking()) -0.3 else 0.2

                    val bb = AxisAlignedBB(
                        posX - width,
                        posY,
                        posZ - width,
                        posX + width,
                        posY + height + 0.05,
                        posZ + width
                    )

                    val vectors = listOf(
                        Vector3d(bb.minX, bb.minY, bb.minZ),
                        Vector3d(bb.minX, bb.maxY, bb.minZ),
                        Vector3d(bb.maxX, bb.minY, bb.minZ),
                        Vector3d(bb.maxX, bb.maxY, bb.minZ),
                        Vector3d(bb.minX, bb.minY, bb.maxZ),
                        Vector3d(bb.minX, bb.maxY, bb.maxZ),
                        Vector3d(bb.maxX, bb.minY, bb.maxZ),
                        Vector3d(bb.maxX, bb.maxY, bb.maxZ)
                    )

                    mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0)
                    var position: Vector4d? = null

                    for (vector in vectors) {
                        var nVector = vector
                        nVector = RenderUtil.project(
                            vector.x - mc.getRenderManager().viewerPosX,
                            vector.y - mc.getRenderManager().viewerPosY,
                            vector.z - mc.getRenderManager().viewerPosZ
                        )
                        if (nVector != null && nVector.z >= 0.0 && nVector.z < 1.0) {
                            if (position == null) {
                                position = Vector4d(nVector.x, nVector.y, nVector.z, 0.0)
                            }
                            position.x = nVector.x.coerceAtMost(position.x)
                            position.y = nVector.y.coerceAtMost(position.y)
                            position.z = nVector.x.coerceAtLeast(position.z)
                            position.w = nVector.y.coerceAtLeast(position.w)
                        }
                    }

                    mc.entityRenderer.setupOverlayRendering()

                    if (position == null) {
                        return@Consumer
                    }

                    GL11.glPushMatrix()

                    val boxX = position.x.toFloat()
                    val boxWidth = position.z.toFloat() - boxX
                    val boxY = position.y.toFloat() + 3
                    val boxHeight = position.w.toFloat() - boxY

                    RenderUtil.drawBorderedRect(
                        (boxX - 1).toDouble(),
                        (boxY - 1).toDouble(),
                        (boxWidth + 2).toDouble(),
                        (boxHeight + 2).toDouble(),
                        1.0,
                        Color(r.value.toInt(), g.value.toInt(), b.value.toInt()).rgb,
                        if (filled.value) Color(0, 0, 0, 90).rgb else 0
                    )

                    GL11.glPopMatrix()

                }
            }
        })
    }

    private fun isValid(entity: EntityLivingBase): Boolean {
        return mc.thePlayer !== entity && entity.entityId != -1488 && isValidType(entity) && entity.isEntityAlive && (!entity.isInvisible || invis.value)
    }

    private fun isValidType(entity: EntityLivingBase): Boolean {
        return players.value && entity is EntityPlayer || mobs.value && (entity is EntityMob || entity is EntitySlime) || passive.value && (entity is EntityVillager || entity is EntityGolem) || animals.value && entity is EntityAnimal
    }
}