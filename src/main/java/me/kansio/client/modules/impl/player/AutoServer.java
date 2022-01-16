package me.kansio.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.network.PacketUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleData(
        name = "Auto Server",
        category = ModuleCategory.PLAYER,
        description = "Automatically does actions on certain servers"
)
public class AutoServer extends Module {

    private boolean hasClickedAutoPlay;

    private ModeValue modeValue = new ModeValue("Server", this, "BlocksMC");
    private ModeValue kitValue = new ModeValue("Kit", this, "Armorer", "Knight");

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (modeValue.getValueAsString()) {
            case "BlocksMC": {
                Packet packet = event.getPacket();

                if (event.getPacket() instanceof S2FPacketSetSlot) {
                    ItemStack item = ((S2FPacketSetSlot) event.getPacket()).func_149174_e();
                    int slot = ((S2FPacketSetSlot) event.getPacket()).func_149173_d();

                    //Auto play
                    if (item == null) return;


                    if (item.getDisplayName() != null && item.getDisplayName().contains("Play Again")) {

                        //set the slot to the paper
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(7));

                        //right click on it
                        for (int i = 0; i < 2; i++) {
                            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(item));
                        }

                        //change the slot back to what it was.
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }

                    //Auto Kit Select
                    if (item.getDisplayName() != null && item.getDisplayName().contains("Kit Selector")) {

                        //if an inventory is open, just return
                        if (mc.currentScreen != null) return;

                        //set the slot to the bow
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(0));

                        //right click on it
                        for (int i = 0; i < 2; i++) {
                            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(item));
                        }

                        //change the slot back to what it was.
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }
                }

                if (event.getPacket() instanceof S2DPacketOpenWindow) {
                    S2DPacketOpenWindow packetData = event.getPacket();

                    //automatic kit selector
                    if (packetData.getWindowTitle().getFormattedText().contains("Kits")) {



                        switch (kitValue.getValue()) {
                            case "Armorer": {
                                mc.playerController.windowClick(packetData.getWindowId(), 0, 1, 0, mc.thePlayer);
                                break;
                            }
                            case "Knight": {
                                mc.playerController.windowClick(packetData.getWindowId(), 18, 1, 0, mc.thePlayer);
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        //set autoplay click to false since the world changed.
        if (mc.thePlayer.ticksExisted < 5) {
            hasClickedAutoPlay = false;
        }

        switch (modeValue.getValueAsString()) {
            case "BlocksMC": {
                /*/if (mc.thePlayer.capabilities.isFlying) {

                    if (!hasClickedAutoPlay) {
                        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                        hasClickedAutoPlay = true;
                    }
                }/*/
                break;
            }
        }
    }
}
