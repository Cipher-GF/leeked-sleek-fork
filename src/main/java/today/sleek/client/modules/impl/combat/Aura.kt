package today.sleek.client.modules.impl.combat

import com.google.common.eventbus.Subscribe
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.apache.commons.lang3.RandomUtils
import today.sleek.Sleek
import today.sleek.base.event.impl.UpdateEvent
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.modules.ModuleData
import today.sleek.base.value.value.BooleanValue
import today.sleek.base.value.value.ModeValue
import today.sleek.base.value.value.NumberValue
import today.sleek.client.gui.notification.Notification
import today.sleek.client.gui.notification.NotificationManager
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.combat.FightUtil
import today.sleek.client.utils.math.Stopwatch
import today.sleek.client.utils.network.PacketUtil
import today.sleek.client.utils.pathfinding.DortPathFinder
import today.sleek.client.utils.pathfinding.Vec3
import today.sleek.client.utils.rotations.AimUtil
import today.sleek.client.utils.rotations.Rotation
import today.sleek.client.utils.rotations.RotationUtil
import java.util.*
import java.util.function.Consumer
import javax.vecmath.Vector2f

@ModuleData(name = "NewAura", description = "Recode", category = ModuleCategory.COMBAT)
class Aura : Module() {

    val attackTimer = Stopwatch()
    var mode = ModeValue("Mode", this,  /*"Switch",*/"Smart")
    var targetPriority = ModeValue("Target Priority", this, "None", "Distance", "Armor", "HurtTime", "Health")
    var rotatemode = ModeValue("Rotation Mode", this, "None", "Default", "Down", "NCP", "AAC", "GWEN")
    var swingrage = NumberValue("Swing Range", this, 3.0, 1.0, 9.0, 0.1)
    var autoblockRange = NumberValue("Block Range", this, 3.0, 1.0, 12.0, 0.1)
    var cps = NumberValue("CPS", this, 12.0, 1.0, 20.0, 1.0)
    var cprandom = NumberValue("Randomize CPS", this, 3.0, 0.0, 10.0, 1.0)
    var fov = NumberValue("FOV", this, 360.0, 1.0, 360.0, 1.0)
    var drawFOV = BooleanValue("Draw FOV", this, false)
    var teleportAura = BooleanValue("TP Hit", this, false)
    var tprange = NumberValue("Teleport Range", this, 25.0, 0.0, 120.0, 1.0, teleportAura)
    var chance: NumberValue<*> = NumberValue("Hit Chance", this, 100, 0, 100, 1)
    var swingmode = ModeValue("Swing Mode", this, "Client", "Server")
    var attackMethod = ModeValue("Attack Method", this, "Packet", "Legit")
    var autoblockmode = ModeValue("Autoblock Mode", this, "None", "Real", "Verus", "Hold", "Fake")
    var gcd = BooleanValue("GCD", this, false)
    var targethud = BooleanValue("TargetHud", this, false)
    var targethudmode = ModeValue("TargetHud Mode", this, targethud, "Sleek", "Exhi", "Flux")
    var targethud3d = ModeValue("Preview Mode", this, targethud, "Face", "Model")
    var hold = BooleanValue("Hold", this, false)
    var players = BooleanValue("Players", this, true)
    var friends = BooleanValue("Friends", this, true)
    var animals = BooleanValue("Animals", this, true)
    var monsters = BooleanValue("Monsters", this, true)
    var invisible = BooleanValue("Invisibles", this, true)
    var walls = BooleanValue("Walls", this, true)
    var currentRotation: Vector2f? = null
    private var index = 0
    private var canBlock = false
    private var lastRotation: Rotation? = null

    override fun onEnable() {
        index = 0
        lastRotation = null
        target = null
        attackTimer.resetTime()
    }

    override fun onDisable() {
        isBlocking = false
        mc.gameSettings.keyBindUseItem.pressed = false
        swinging = false
        currentRotation = null
        target = null

        if (!mc.thePlayer.isBlocking) {
            isBlocking = false
            mc.thePlayer.sendQueue.addToSendQueue(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN
                )
            )
        }
    }

    @Subscribe
    fun doHoldBlock(event: UpdateEvent) {
        if (autoblockmode.value == "Hold") {
            mc.gameSettings.keyBindUseItem.pressed = target != null
        }
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {

        if (isBlocking && target == null) {
            isBlocking = false
            mc.thePlayer.sendQueue.addToSendQueue(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN
                )
            )
        }

        if (mc.thePlayer.ticksExisted < 5) {
            if (isToggled) {
                NotificationManager.getNotificationManager()
                    .show(Notification(Notification.NotificationType.INFO, "World Change!", "Killaura disabled", 1))
                toggle()
            }
        }


        //Return if the player isn't holding attack & hold is on
        if (hold.value && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            return
        }

        val entities = FightUtil.getMultipleTargets(
            if (teleportAura.value) tprange.value else swingrage.value,
            players.value,
            friends.value,
            animals.value,
            false,
            monsters.value,
            invisible.value
        )
        val blockRangeEntites = FightUtil.getMultipleTargets(
            autoblockRange.value,
            players.value,
            friends.value,
            animals.value,
            walls.value,
            monsters.value,
            invisible.value
        )

        entities.removeIf { e: EntityLivingBase ->
            e.name.contains("[NPC]")
        }

        if (fov.value != 360.0) {
            entities.removeIf { e: EntityLivingBase? ->
                !RotationUtil.isVisibleFOV(
                    e, fov.value.toFloat() / 2
                )
            }
        }

        val heldItem = mc.thePlayer.heldItem

        canBlock = (!blockRangeEntites.isEmpty() && heldItem != null && heldItem.item is ItemSword)

        if (event.isPre) {
            target = null
        }

        if (entities.isEmpty()) {
            index = 0
            isBlocking = false
        } else {
            if (index >= entities.size) index = 0

            if (canBlock) {
                when (autoblockmode.value) {
                    "Real" -> {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.heldItem)
                        isBlocking = true
                    }
                    "Fake" -> {
                        isBlocking = true
                    }
                }
            }

            if (event.isPre) {
                when (targetPriority.value.lowercase(Locale.getDefault())) {
                    "distance" -> {
                        entities.sortWith(Comparator.comparingInt { e: EntityLivingBase ->
                            -e.getDistanceToEntity(
                                mc.thePlayer
                            ).toInt()
                        })
                    }
                    "armor" -> {
                        entities.sortWith(Comparator.comparingInt { e: EntityLivingBase -> -e.totalArmorValue })
                    }
                    "hurttime" -> {
                        entities.sortWith(Comparator.comparingInt { e: EntityLivingBase -> -e.hurtResistantTime })
                    }
                    "health" -> {
                        entities.sortWith(Comparator.comparingInt { e: EntityLivingBase ->
                            -e.health.toInt()
                        })
                    }
                }
                entities.reverse()
                target = entities[0]

                //set the targetted players as main targets.

                //set the targetted players as main targets.
                entities.forEach(Consumer { entityLivingBase: EntityLivingBase? ->
                    if (entityLivingBase is EntityPlayer && Sleek.getInstance().targetManager.isTarget(target as EntityPlayer)) {
                        target = entities[entities.indexOf(entityLivingBase)]
                    }
                })
                if (!teleportAura.value) {
                    aimAtTarget(event, rotatemode.value, target)
                }

                val canIAttack = attackTimer.timeElapsed((1000L / cps.value).toLong())

                if (canIAttack) {
                    if (cps.value > 9) {
                        cps.setValue(cps.value - RandomUtils.nextInt(0, cprandom.value.toInt()))
                    } else {
                        cps.setValue(cps.value + RandomUtils.nextInt(0, cprandom.value.toInt()))
                    }
                    when (mode.value) {
                        "Smart" -> {
                            if (attack(target!!, chance.value.toInt().toDouble())) {
                                attackTimer.resetTime()
                            }
                        }
                    }
                }
            }
        }
    }
    

    private fun attack(entity: EntityLivingBase, chance: Double): Boolean {
        if (FightUtil.canHit(chance / 100)) {
            if (swingmode.value.equals("client", ignoreCase = true)) {
                mc.thePlayer.swingItem()
            } else if (swingmode.value.equals("server", ignoreCase = true)) {
                mc.netHandler.addToSendQueue(C0APacketAnimation())
            }
            when (teleportAura.value) {
                true -> {
                    val path = DortPathFinder.computePath(
                        Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        Vec3(entity.posX, entity.posY, entity.posZ)
                    )
                    for (vec in path) {
                        PacketUtil.sendPacketNoEvent(C04PacketPlayerPosition(vec!!.x, vec.y, vec.z, true))
                    }
                    if (attackMethod.value.equals("Packet", ignoreCase = true)) PacketUtil.sendPacket(
                        C02PacketUseEntity(
                            target, C02PacketUseEntity.Action.ATTACK
                        )
                    ) else mc.playerController.attackEntity(
                        mc.thePlayer, entity
                    )
                    path.reverse()
                    for (vector in path) {
                        PacketUtil.sendPacketNoEvent(C04PacketPlayerPosition(vector!!.x, vector.y, vector.z, true))
                    }
                }
                false -> {
                    if (attackMethod.value.equals("Packet", ignoreCase = true)) PacketUtil.sendPacket(
                        C02PacketUseEntity(
                            target, C02PacketUseEntity.Action.ATTACK
                        )
                    ) else mc.playerController.attackEntity(
                        mc.thePlayer, entity
                    )
                    if (!isBlocking && autoblockmode.value.equals("verus", ignoreCase = true)) {
                        PacketUtil.sendPacket(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                        isBlocking = true
                    }
                }
            }
            return true
        } else {
            mc.thePlayer.swingItem()
        }
        return false
    }

    fun aimAtTarget(event: UpdateEvent, mode: String, target: Entity?) {
        var rotation = AimUtil.getRotationsRandom(target as EntityLivingBase?)
        if (lastRotation == null) {
            lastRotation = rotation
            attackTimer.resetTime()
            return
        }
        var temp = rotation
        rotation = lastRotation
        when (mode.uppercase(Locale.getDefault())) {
            "DEFAULT" -> {
                event.rotationYaw = rotation.rotationYaw
                event.rotationPitch = rotation.rotationPitch
            }
            "DOWN" -> {
                temp = Rotation(mc.thePlayer.rotationYaw, 90.0f)
                event.rotationPitch = 90.0f
            }
            "NCP" -> {
                run {
                    rotation = Rotation.fromFacing(target)
                    temp = rotation
                    lastRotation = temp
                }
                event.rotationYaw = rotation.rotationYaw
            }
            "AAC" -> {
                rotation = Rotation(mc.thePlayer.rotationYaw, temp.rotationPitch)
                event.rotationPitch = rotation.rotationPitch
            }
            "GWEN" -> {
                temp = if (mc.thePlayer.ticksExisted % 5 == 0) AimUtil.getRotationsRandom(
                    target
                ) else lastRotation!!
                event.rotationYaw = temp.rotationYaw
                event.rotationPitch = temp.rotationPitch
            }
        }
        lastRotation = temp
    }

    companion object {
        @JvmStatic
        var target: EntityLivingBase? = null

        @JvmStatic
        var isBlocking = false

        @JvmStatic
        var swinging = false
    }
}