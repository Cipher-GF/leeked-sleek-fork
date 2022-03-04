package today.sleek.client.modules.impl.movement

import com.google.common.eventbus.Subscribe
import today.sleek.base.event.impl.MoveEvent
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.modules.ModuleData
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.math.Stopwatch
import today.sleek.client.utils.player.PlayerUtil
import today.sleek.client.utils.player.TimerUtil
import today.sleek.base.value.value.ModeValue
import today.sleek.base.value.value.NumberValue

@ModuleData(name = "Long Jump", category = ModuleCategory.MOVEMENT, description = "Jump further than normal")
class LongJump : Module() {
    //Verus Highjump Variables
    var launched = false
    var wasLaunched = false
    var jumped = false
    private val mode = ModeValue("Mode", this, "Verus", "Viper", "Vanilla", "FuncraftDev")

    //verus boost stuff
    private val vertical = NumberValue("Vertical Boost", this, 0.8, 0.05, 6.0, 0.1)
    private val boost = NumberValue("Speed", this, 1.45, 0.05, 10.0, 0.1)
    private val damageWaiterThing = Stopwatch()
    var speed = 0.8
    override fun onEnable() {
        launched = false
        wasLaunched = false
        jumped = false
        damageWaiterThing.resetTime()
        if ("Verus" == mode.value) {
            if (!mc.thePlayer.onGround) {
                toggle()
                return
            }
            TimerUtil.setTimer(0.3f)
            PlayerUtil.damageVerus()
        }
    }

    override fun onDisable() {
        TimerUtil.Reset()
        jumped = false
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        when (mode.value) {
            "Verus" -> {
                if (mc.thePlayer.hurtTime > 1 && !launched) {
                    launched = true
                }
                if (launched) {
                    if (!jumped) {
                        mc.thePlayer.motionY = vertical.value
                        jumped = true
                    }
                    PlayerUtil.setMotion(boost.value.toFloat().toDouble())
                    launched = false
                    wasLaunched = true
                    toggle()
                }
            }
            "Vanilla" -> {
                if (mc.thePlayer.isMoving) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = vertical.value
                    }
                    PlayerUtil.setMotion(boost.value.toFloat().toDouble())
                }
            }
            "Test" -> {
                mc.thePlayer.posY = mc.thePlayer.prevPosY
                if (mc.thePlayer.isMoving) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = vertical.value
                    }
                    PlayerUtil.setMotion(boost.value.toFloat().toDouble())
                }

            }
            "FuncraftDev" -> {

                if (mc.thePlayer.isMovingOnGround) {
                    speed = 0.8
                    repeat(4)
                    { mc.thePlayer.motionY = 0.4025 }
                    launched = true
                }
                if (!mc.thePlayer.onGround) {
//                    speed -= speed / 152
                    mc.thePlayer.motionX *= 0.9
                    mc.thePlayer.motionZ *= 0.9
                }
                if ((mc.theWorld.getCollidingBoundingBoxes(
                        mc.thePlayer,
                        mc.thePlayer.entityBoundingBox.offset(0.0, mc.thePlayer.motionY, 0.0)
                    ).size > 0
                            || mc.thePlayer.isCollidedVertically)
                    && (launched && event!!.isPre)
                ) {
                    toggle()
                }

            }
        }
    }

    //    @Subscribe
    //    public void render(RenderOverlayEvent event) {
    //        if (mode.getValue().equals("Test")) {
    //            RenderUtil.drawBar((float) event.getSr().getScaledWidth_double() / 2, (float) (event.getSr().getScaledHeight_double() / 2) - 25, 100, 20, 1000, (float) damageWaiterThing.getTimeRemaining(1000), 0xFF00FF00);
    //            mc.fontRendererObj.drawStringWithShadow(new DecimalFormat("0.#").format(((double) (damageWaiterThing.getTimeRemaining(1000)) / 1000)), (float) event.getSr().getScaledWidth_double() / 2, (float) event.getSr().getScaledHeight_double() / 2, -1);
    //        }
    //    }
    @Subscribe
    fun onMove(event: MoveEvent?) {
        when (mode.value) {
            "Viper" -> {
                if (!mc.thePlayer.onGround) return
                TimerUtil.setTimer(0.3f)
                if (mc.thePlayer.isMoving) {
                    var i = 0
                    while (i < 17) {
                        PlayerUtil.TPGROUND(event, 0.32, 0.0)
                        ++i
                    }
                }
            }
        }
    }

    override fun getSuffix(): String {
        return " " + mode.valueAsString
    }
}