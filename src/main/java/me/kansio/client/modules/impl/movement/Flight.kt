package me.kansio.client.modules.impl.movement

import me.kansio.client.modules.api.ModuleData
import me.kansio.client.modules.api.ModuleCategory
import me.kansio.client.modules.impl.movement.flight.FlightMode
import me.kansio.client.utils.java.ReflectUtils
import java.util.stream.Collectors
import me.kansio.client.value.value.ModeValue
import me.kansio.client.value.value.NumberValue
import com.google.common.eventbus.Subscribe
import lombok.Getter
import me.kansio.client.event.impl.UpdateEvent
import me.kansio.client.gui.notification.NotificationManager
import me.kansio.client.event.impl.MoveEvent
import me.kansio.client.event.impl.PacketEvent
import me.kansio.client.event.impl.BlockCollisionEvent
import me.kansio.client.gui.notification.Notification
import me.kansio.client.modules.impl.Module
import me.kansio.client.utils.math.Stopwatch
import me.kansio.client.utils.player.TimerUtil
import me.kansio.client.value.value.BooleanValue
import net.minecraft.potion.Potion
import java.lang.Exception
import java.util.Comparator

@Getter
@ModuleData(name = "Flight", category = ModuleCategory.MOVEMENT, description = "Allows you to fly")
class Flight : Module() {
    private val modes = ReflectUtils.getReflects("${this.javaClass.`package`.name}.flight", FlightMode::class.java).map { it.newInstance() as FlightMode }.sortedBy { it.name }
    val currentMode: FlightMode get() = modes.find { mode.equals(it.name) } ?: throw NullPointerException() // this should not happen
    private val mode = ModeValue(
        "Mode",
        this,
        *modes.map { it.name }.toTypedArray()
    )

    val speed = NumberValue("Speed", this, 1.0, 0.0, 10.0, 0.1)
    val antikick = BooleanValue("AntiKick", this, true, mode, "BridgerLand (TP)")
    val timer = NumberValue("Timer", this, 1.0, 1.0, 5.0, 0.1, mode, "Mush")
    val blink = BooleanValue("Blink", this, true, mode, "Mush")
    val viewbob = BooleanValue("View Bobbing", this, true)
    private val stopwatch = Stopwatch()
    var ticks = 0f
    var prevFOV = 0f
    override fun onEnable() {
        currentMode.onEnable()
    }

    override fun onDisable() {
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0
        TimerUtil.Reset()
        currentMode.onDisable()
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (viewbob.value && mc.thePlayer.isMoving) {
            mc.thePlayer.cameraYaw = 0.05f
        } else {
            mc.thePlayer.cameraYaw = 0f
        }
        currentMode.onUpdate(event)
        if (mc.thePlayer.ticksExisted < 5) {
            if (isToggled) {
                toggle()
            }
        }
    }

    @Subscribe
    fun onMove(event: MoveEvent?) {
        currentMode.onMove(event)
    }

    @Subscribe
    fun onPacket(event: PacketEvent?) {
        currentMode.onPacket(event)
    }

    @Subscribe
    fun onCollide(event: BlockCollisionEvent?) {
        currentMode.onCollide(event)
    }

    private val baseMoveSpeed: Double
        private get() {
            var n = 0.2873
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1)
            }
            return n
        }

    override fun getSuffix(): String {
        return " " + mode.valueAsString
    }
}