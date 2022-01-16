package me.kansio.client.modules.impl.combat;

import com.google.common.eventbus.Subscribe;
import javafx.scene.paint.Stop;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.math.Stopwatch;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

@ModuleData(
        name = "AutoPot",
        category = ModuleCategory.COMBAT,
        description = "Pots for you"
)
public class AutoPot extends Module {

    private static boolean isPotting;
    private final Object[] badPotionArray = {false};

    Stopwatch stopwatch;


    public AutoPot() {
        stopwatch = new Stopwatch();
    }

    @Subscribe
    public void onPlayerUpdate(UpdateEvent playerUpdateEvent) {

        if (KillAura.target != null || mc.thePlayer.fallDistance < 2 || updateCounter() == 0 || mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (playerUpdateEvent.isPre()) {
            if (mc.thePlayer.getHealth() <= 7 && stopwatch.timeElapsed(MathUtil.getRandomInRange(150, 300))) {
                if (doesHotbarHavePots()) {
                    float pitch = playerUpdateEvent.getRotationPitch();
                    playerUpdateEvent.setRotationPitch(90);
                    for (int index = 36; index < 45; index++) {
                        final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                        if (stack == null) {
                            continue;
                        }

                        if (isStackSplashHealthPot(stack)) {
                            final int oldslot = mc.thePlayer.inventory.currentItem;
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, playerUpdateEvent.getRotationYaw(), 87, mc.thePlayer.onGround));
                            mc.getNetHandler().addToSendQueue(
                                    new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, stack, 0, 0, 0));
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldslot));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, playerUpdateEvent.getRotationYaw(), playerUpdateEvent.getRotationPitch(), mc.thePlayer.onGround));
                            break;
                        }
                    }
                    stopwatch.resetTime();
                    playerUpdateEvent.setRotationPitch(90);
                } else {
                    getPotsFromInventory();
                }
            }
        }
    }

    private int updateCounter() {
        int counter = 0;
        for (int index = 9; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                counter += stack.stackSize;
            }
        }
        return counter;
    }

    private boolean doesHotbarHavePots() {
        for (int index = 36; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                return true;
            }
        }
        return false;
    }

    private void getPotsFromInventory() {
        if (mc.currentScreen instanceof GuiChest) {
            return;
        }
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isStackSplashHealthPot(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.heal.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
