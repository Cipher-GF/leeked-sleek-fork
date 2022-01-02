package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.network.PacketUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {

    private boolean wasShooting = false;
    private int lastSlot;

    private int serverSideSlot;

    private NumberValue packets = new NumberValue("Packets", this, 20, 0, 1000, 1);

    public FastBow() {
        super("Fast Bow", ModuleCategory.COMBAT);
        register(packets);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {

            System.out.println("using");
            //check if they have a bow
            if (!hasBow()) {
                ChatUtil.log("§cYou do not have a bow in your inventory.");
                return;
            }

            System.out.println("y");

            int slotWithBow = getBowSlot();

            //this shouldn't happen
            if (slotWithBow == -1) {
                return;
            }

            System.out.println("y2");

            //if the server side slot isn't the slot with the bow, then set it to
            if (serverSideSlot != slotWithBow) {
                PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(slotWithBow));
                System.out.println("set slot to " + slotWithBow);
            }
            serverSideSlot = slotWithBow;

            System.out.println("y3");

            PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));

            //send the funny packets
            for (int i = 0; i < packets.getValue().intValue(); i++) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer());
            }

            PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

            wasShooting = true;

        } else if (wasShooting) { //revert to the last itemslot
            PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(lastSlot));
            wasShooting = false;
        }
    }

    public boolean hasBow() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory == null) continue;
            if (mc.thePlayer.inventory.getStackInSlot(i) == null) continue;
            if (mc.thePlayer.inventory.getStackInSlot(i).getItem() == null) continue;

            if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
                return true;
            }
        }

        return false;
    }

    //returns -1 if it can't find a bow
    public int getBowSlot() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
                return i;
            }
        }

        return -1;
    }
}
