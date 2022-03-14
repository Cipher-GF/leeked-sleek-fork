package today.sleek.client.modules.impl.player

import today.sleek.base.modules.ModuleData
import today.sleek.base.modules.ModuleCategory
import today.sleek.base.value.value.NumberValue
import com.google.common.eventbus.Subscribe
import today.sleek.base.event.impl.UpdateEvent
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Items
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemArmor
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.item.ItemGlassBottle
import net.minecraft.item.ItemEgg
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemSword
import net.minecraft.util.DamageSource
import net.minecraft.item.ItemBook
import net.minecraft.item.ItemEnderPearl
import net.minecraft.item.ItemTool
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemBlock
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import today.sleek.base.value.value.BooleanValue
import today.sleek.client.modules.impl.Module
import today.sleek.client.utils.math.Stopwatch
import today.sleek.client.utils.network.PacketUtil
import java.util.*
import kotlin.collections.ArrayList

@ModuleData(
    name = "Inventory Manager",
    category = ModuleCategory.PLAYER,
    description = "Automatically manages your inventory"
)
class InvManager : Module() {
    private val autoSword = BooleanValue("AutoSword", this, false)
    private val aSwordInInv = BooleanValue("Sword Only in Inv", this, false, autoSword)
    private val aSwordDelay = NumberValue("AutoSword Delay", this, 25.0, 0.0, 1000.0, 1.0, autoSword)
    private val invCleaner = BooleanValue("Inv Cleaner", this, false)
    private val invCleanerInInv = BooleanValue("Clean Only in Inv", this, false, invCleaner)
    private val invCleanerDelay = NumberValue("InvCleaner Delay", this, 25.0, 0.0, 1000.0, 1.0, invCleaner)
    private val autoArmor = BooleanValue("Auto Armor", this, false)
    private val autoArmorInInv = BooleanValue("Armor Only in Inv", this, false, autoArmor)
    private val autoArmorDelay = NumberValue("AutoArmor Delay", this, 25.0, 0.0, 1000.0, 1.0, autoArmor)
    private val disableIfSlimeBall = BooleanValue("Disable if compass", this, true)
    private val ifNotMoving = BooleanValue("Stop when moving", this, true)
    private val armorStop = Stopwatch()
    private val invStop = Stopwatch()
    private val swordStop = Stopwatch()
    private val stopwatch = Stopwatch()
    private val allSwords: MutableList<Int> = ArrayList()
    private val allArmor: Array<ArrayList<Int>> = emptyArray()
    private val trash: MutableList<Int> = ArrayList()
    private val cleaning = false
    private var bestArmorSlot: IntArray = IntArray(4)
    private var bestSwordSlot = 0
    override fun onEnable() {}
    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        if (!event.isPre) return
        if (mc.currentScreen is GuiChest) return
        if (ifNotMoving.value && (mc.thePlayer.isMoving || !mc.thePlayer.onGround)) {
            return
        }
        if (disableIfSlimeBall.value && (mc.thePlayer.inventory.hasItem(Items.compass) || mc.thePlayer.inventory.hasItem(
                Items.slime_ball
            ))
        ) {
            return
        }
        collectItems()
        collectBestArmor()
        collectTrash()
        val trashSize = trash.size
        val trashPresent = trashSize > 0
        val windowId = mc.thePlayer.inventoryContainer.windowId
        val bestSwordSlot = bestSwordSlot
        if (autoSword.value) {
            if (!aSwordInInv.value) {
                autoSwordThing(bestSwordSlot, swordStop, windowId)
            } else if (mc.currentScreen is GuiInventory) {
                autoSwordThing(bestSwordSlot, swordStop, windowId)
            }
        }
        if (autoArmor.value) {
            if (!autoArmorInInv.value) {
                equipArmor(armorStop, autoArmorDelay.value.toInt())
            } else if (mc.currentScreen is GuiInventory) {
                equipArmor(armorStop, autoArmorDelay.value.toInt())
            }
        }
        if (invCleaner.value) {
            if (!invCleanerInInv.value) {
                invCleanerThing(invStop, invCleanerDelay.value.toInt(), trash, windowId)
            } else if (mc.currentScreen is GuiInventory) {
                invCleanerThing(invStop, invCleanerDelay.value.toInt(), trash, windowId)
            }
        }
    }

    private fun equipArmor(stopwatch: Stopwatch, delay: Int) {
        for (i in 9..44) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).hasStack) continue
            val stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).stack
            if (stackInSlot.item !is ItemArmor) continue
            if (getArmorItemsEquipSlot(stackInSlot, false) == -1) continue
            if (mc.thePlayer.getEquipmentInSlot(getArmorItemsEquipSlot(stackInSlot, true)) == null) {
                if (stopwatch.timeElapsed(delay.toLong())) {
                    mc.thePlayer.sendQueue.addToSendQueue(
                        C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY)
                    )
                    mc.playerController.windowClick(
                        mc.thePlayer.inventoryContainer.windowId, i, 0, 0,
                        mc.thePlayer
                    )
                    mc.playerController.windowClick(
                        mc.thePlayer.inventoryContainer.windowId,
                        getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer
                    )
                    stopwatch.resetTime()
                    return
                }
            } else {
                val stackInEquipmentSlot = mc.thePlayer
                    .getEquipmentInSlot(getArmorItemsEquipSlot(stackInSlot, true))
                if (compareProtection(stackInSlot, stackInEquipmentSlot) == stackInSlot) {
                    println("Stack in slot : " + stackInSlot.unlocalizedName)
                    if (stopwatch.timeElapsed(delay.toLong())) {
                        mc.thePlayer.sendQueue.addToSendQueue(
                            C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY)
                        )
                        mc.playerController.windowClick(
                            mc.thePlayer.inventoryContainer.windowId, i, 0, 0,
                            mc.thePlayer
                        )
                        mc.playerController.windowClick(
                            mc.thePlayer.inventoryContainer.windowId,
                            getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer
                        )
                        mc.playerController.windowClick(
                            mc.thePlayer.inventoryContainer.windowId, i, 0, 0,
                            mc.thePlayer
                        )
                        mc.thePlayer.sendQueue.addToSendQueue(C0DPacketCloseWindow(0))
                        stopwatch.resetTime()
                        return
                    }
                }
            }
        }
    }

    private fun invCleanerThing(stopwatch: Stopwatch, delay: Int, trash: List<Int>, windowId: Int) {
        PacketUtil.sendPacketNoEvent(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY))
        for (slot in trash) {
            if (stopwatch.timeElapsed(delay.toLong())) {
                mc.playerController.windowClick(windowId, if (slot < 9) slot + 36 else slot, 1, 4, mc.thePlayer)
                stopwatch.resetTime()
            }
        }
        if (mc.currentScreen !is GuiInventory) {
            PacketUtil.sendPacketNoEvent(C0DPacketCloseWindow(windowId))
        }
    }

    private fun autoSwordThing(bestSwordSlot: Int, stopwatch: Stopwatch, windowId: Int) {
        if (bestSwordSlot != -1 && stopwatch.timeElapsed(aSwordDelay.value.toInt().toLong())) {
            mc.playerController.windowClick(
                windowId,
                if (bestSwordSlot < 9) bestSwordSlot + 36 else bestSwordSlot,
                0,
                2,
                mc.thePlayer
            )
            stopwatch.resetTime()
        }
    }

    private fun isTrash(item: ItemStack): Boolean {
        return item.item.unlocalizedName.contains("tnt") || item.displayName.contains("frog") ||
                item.item.unlocalizedName.contains("stick") ||
                item.item.unlocalizedName.contains("string") || item.item.unlocalizedName.contains("flint") ||
                item.item.unlocalizedName.contains("feather") || item.item.unlocalizedName.contains("bucket") ||
                item.item.unlocalizedName.contains("snow") || item.item.unlocalizedName.contains("enchant") ||
                item.item.unlocalizedName.contains("exp") || item.item.unlocalizedName.contains("shears") ||
                item.item.unlocalizedName.contains("arrow") || item.item.unlocalizedName.contains("anvil") ||
                item.item.unlocalizedName.contains("torch") || item.item.unlocalizedName.contains("seeds") ||
                item.item.unlocalizedName.contains("leather") || item.item.unlocalizedName.contains("boat") ||
                item.item.unlocalizedName.contains("fishing") || item.item.unlocalizedName.contains("wheat") ||
                item.item.unlocalizedName.contains("flower") || item.item.unlocalizedName.contains("record") ||
                item.item.unlocalizedName.contains("note") || item.item.unlocalizedName.contains("sugar") ||
                item.item.unlocalizedName.contains("wire") || item.item.unlocalizedName.contains("trip") ||
                item.item.unlocalizedName.contains("slime") || item.item.unlocalizedName.contains("web") ||
                item.item is ItemGlassBottle || item.item.unlocalizedName.contains("piston") ||
                item.item.unlocalizedName.contains("potion") && isBadPotion(item) ||  //   ((item.getItem() instanceof ItemArmor) && isBestArmor(item)) ||
                item.item is ItemEgg || item.item.unlocalizedName.contains("bow") && !item.displayName.contains("Kit") ||  //   ((item.getItem() instanceof ItemSword) && !isBestSword(item)) ||
                item.item.unlocalizedName.contains("Raw")
    }

    private fun getArmorItemsEquipSlot(stack: ItemStack, equipmentSlot: Boolean): Int {
        if (stack.unlocalizedName.contains("helmet")) return if (equipmentSlot) 4 else 5
        if (stack.unlocalizedName.contains("chestplate")) return if (equipmentSlot) 3 else 6
        if (stack.unlocalizedName.contains("leggings")) return if (equipmentSlot) 2 else 7
        return if (stack.unlocalizedName.contains("boots")) if (equipmentSlot) 1 else 8 else -1
    }

    private fun compareProtection(item1: ItemStack, item2: ItemStack): ItemStack? {
        if (item1.item !is ItemArmor && item2.item !is ItemArmor) return null
        if (item1.item !is ItemArmor) return item2
        if (item2.item !is ItemArmor) return item1
        if (getArmorProtection(item1) > getArmorProtection(item2)) {
            return item1
        } else if (getArmorProtection(item2) > getArmorProtection(item1)) {
            return item2
        }
        return null
    }

    private fun getArmorProtection(armorStack: ItemStack): Double {
        if (armorStack.item !is ItemArmor) return 0.0
        val armorItem = armorStack.item as ItemArmor
        val protectionLevel = EnchantmentHelper.getEnchantmentLevel(
            Enchantment.protection.effectId,
            armorStack
        ).toDouble()
        return armorItem.damageReduceAmount + (6 + protectionLevel * protectionLevel) * 0.75 / 3
    }

    fun collectItems() {
        bestSwordSlot = -1
        allSwords.clear()
        var bestSwordDamage = -1.0f
        for (i in 0..35) {
            val itemStack = mc.thePlayer.inventory.getStackInSlot(i)
            if (itemStack != null && itemStack.item != null) {
                if (itemStack.item is ItemSword) {
                    val damageLevel = getDamageLevel(itemStack)
                    allSwords.add(i)
                    if (bestSwordDamage < damageLevel) {
                        bestSwordDamage = damageLevel
                        bestSwordSlot = i
                    }
                }
            }
        }
    }

    private fun collectBestArmor() {
        val bestArmorDamageReduction = IntArray(4)
        bestArmorSlot = IntArray(4)
        Arrays.fill(bestArmorDamageReduction, -1)
        Arrays.fill(bestArmorSlot, -1)
        var itemStack: ItemStack?
        var armor: ItemArmor
        var armorType: Int
        var i: Int = 0
        while (i < bestArmorSlot.size) {
            itemStack = mc.thePlayer.inventory.armorItemInSlot(i)
            allArmor[i] = ArrayList<Int>()
            if (itemStack != null && itemStack.item != null && itemStack.item is ItemArmor) {
                armor = itemStack.item as ItemArmor
                armorType = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(
                    arrayOf(itemStack),
                    DamageSource.generic
                )
                bestArmorDamageReduction[i] = armorType
            }
            ++i
        }
        i = 0
        while (i < 36) {
            itemStack = mc.thePlayer.inventory.getStackInSlot(i)
            if (itemStack != null && itemStack.item != null && itemStack.item is ItemArmor) {
                armor = itemStack.item as ItemArmor
                armorType = 3 - armor.armorType
                allArmor[armorType].add(i)
                val slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(
                    arrayOf(itemStack),
                    DamageSource.generic
                )
                if (bestArmorDamageReduction[armorType] < slotProtectionLevel) {
                    bestArmorDamageReduction[armorType] = slotProtectionLevel
                    bestArmorSlot[armorType] = i
                }
            }
            ++i
        }
    }

    private fun collectTrash() {
        trash.clear()
        var i: Int
        i = 0
        while (i < 36) {
            val itemStack = mc.thePlayer.inventory.getStackInSlot(i)
            if (itemStack != null) if (itemStack.item != null && itemStack.item !is ItemBook && !itemStack.displayName.contains(
                    "Hype"
                ) && !itemStack.displayName.contains("Jogadores") && !itemStack.displayName.startsWith("\u00a76") && !itemStack.displayName.contains(
                    "Emerald"
                ) && !itemStack.displayName.contains("Gadget") && !isValidItem(itemStack)
            ) {
                trash.add(i)
            }
            ++i
        }
        i = 0
        while (i < allArmor.size) {
            val armorItem: List<*> = allArmor[i]
            if (armorItem != null) {
                var i1 = 0
                val armorItemSize = armorItem.size
                while (i1 < armorItemSize) {
                    val slot = armorItem[i1] as Int
                    if (slot != bestArmorSlot[i]) {
                        trash.add(slot)
                    }
                    ++i1
                }
            }
            ++i
        }
        i = 0
        while (i < allSwords.size) {
            val slot = allSwords[i]
            if (slot != bestSwordSlot) {
                trash.add(slot)
            }
            ++i
        }
    }

    private fun isValidItem(itemStack: ItemStack): Boolean {
        return if (itemStack.displayName.startsWith("\u00a7a")) {
            true
        } else {
            itemStack.item is ItemArmor || itemStack.item is ItemEnderPearl || itemStack.item is ItemSword || itemStack.item is ItemTool || itemStack.item is ItemFood || itemStack.item is ItemPotion && !isBadPotion(
                itemStack
            ) || itemStack.item is ItemBlock || itemStack.displayName.contains("Play") || itemStack.displayName.contains(
                "Game"
            ) || itemStack.displayName.contains(
                "Right Click"
            )
        }
    }

    private fun isBadPotion(stack: ItemStack?): Boolean {
        if (stack != null && stack.item is ItemPotion) {
            val potion = stack.item as ItemPotion
            if (ItemPotion.isSplash(stack.itemDamage)) {
                for (o in potion.getEffects(stack)) {
                    val effect = o as PotionEffect
                    if (effect.potionID == Potion.poison.getId() || effect.potionID == Potion.harm.getId() || effect.potionID == Potion.moveSlowdown.getId() || effect.potionID == Potion.weakness.getId()) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getDamageLevel(stack: ItemStack): Float {
        return if (stack.item is ItemSword) {
            val sword = stack.item as ItemSword
            val sharpness =
                EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack).toFloat() * 1.25f
            val fireAspect =
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack).toFloat() * 1.5f
            sword.damageVsEntity + sharpness + fireAspect
        } else {
            0.0f
        }
    }
}