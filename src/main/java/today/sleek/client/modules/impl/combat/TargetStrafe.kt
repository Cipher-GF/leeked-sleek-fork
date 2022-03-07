package today.sleek.client.modules.impl.combat

import today.sleek.base.modules.ModuleData
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.value.value.NumberValue
import today.sleek.client.modules.impl.combat.TargetStrafe
import com.google.common.eventbus.Subscribe
import net.minecraft.entity.Entity
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.event.impl.Render3DEvent
import today.sleek.client.modules.impl.combat.KillAura
import org.lwjgl.opengl.GL11
import today.sleek.Sleek
import today.sleek.base.value.value.BooleanValue
import today.sleek.client.modules.impl.Module

@ModuleData(
    name = "Target Strafe",
    category = ModuleCategory.COMBAT,
    description = "Automatically strafes around the killaura target"
)
class TargetStrafe : Module() {

    var autoF5 = BooleanValue("Auto F5", this, false)
    var jump = BooleanValue("On Jump", this, false)
    var control = BooleanValue("Controllable", this, false)
    var render = BooleanValue("Render", this, false)

    @JvmField
    var radius = NumberValue("Radius", this, 3.0, 0.1, 5.0, 0.1)
    var width = NumberValue("Width", this, 1.0, 0.1, 5.0, 0.1, render)
    var red = NumberValue("Red", this, 100.0, 0.0, 255.0, 1.0, render)
    var green = NumberValue("Green", this, 100.0, 0.0, 255.0, 1.0, render)
    var blue = NumberValue("Blue", this, 100.0, 0.0, 255.0, 1.0, render)

    override fun onDisable() {
        dir = -1.0
    }

    @Subscribe
    fun onMotion(event: UpdateEvent) {
        if (canStrafe() && autoF5.value) {
            mc.gameSettings.thirdPersonView = 1
        } else if (!canStrafe() && autoF5.value) {
            mc.gameSettings.thirdPersonView = 0
        }
        if (event.isPre) {
            if (control.value) {
                if (mc.gameSettings.keyBindLeft.isPressed) {
                    dir = 1.0
                } else if (mc.gameSettings.keyBindRight.isPressed) {
                    dir = -1.0
                }
            }
            if (mc.thePlayer.isCollidedHorizontally) {
                invertStrafe()
            }
        }
    }

    private fun invertStrafe() {
        dir = -dir
    }

    @Subscribe
    fun onRender(event: Render3DEvent?) {
        if (canStrafe() && render.value) {
            drawCircle(KillAura.target, mc.timer.renderPartialTicks)
        }
    }

    fun canStrafe(): Boolean {
        if (!Sleek.getInstance().moduleManager.getModuleByName("KillAura")!!.isToggled) {
            return false
        }
        if (!this.isToggled) {
            return false
        }
        return if (jump.value) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) KillAura.target != null else false
        } else {
            KillAura.target != null
        }
    }

    private fun drawCircle(entity: Entity, partialTicks: Float) {
        GL11.glPushMatrix()
        GL11.glColor3d(red.value, green.value, blue.value)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)
        GL11.glLineWidth(width.value.toFloat())
        GL11.glBegin(GL11.GL_LINE_STRIP)
        val x =
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX
        val y =
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY
        val z =
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ
        val pix2 = Math.PI * 2.0
        for (i in 0..90) {
            GL11.glVertex3d(
                x + (radius.value - 0.5) * Math.cos(i * pix2 / 45),
                y,
                z + (radius.value - 0.5) * Math.sin(i * pix2 / 45)
            )
        }
        GL11.glEnd()
        GL11.glDepthMask(true)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
    }

    companion object {
        @JvmField
        var dir = -1.0
    }
}