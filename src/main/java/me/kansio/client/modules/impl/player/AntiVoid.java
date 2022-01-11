package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.block.BlockUtil;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

import java.text.MessageFormat;

public class AntiVoid extends Module {

    //values for storing the previous pos (when they weren't in the void)
    double prevX = 0;
    double prevY = 0;
    double prevZ = 0;

    private final NumberValue<Integer> fallDist = new NumberValue<Integer>("Fall Distance", this, 7, 0, 30, 1);
    private final NumberValue<Integer> compareBelow = new NumberValue<Integer>("Compared Y", this, 10, 0, 100, 1);

    public AntiVoid() {
        super("Anti Void", ModuleCategory.PLAYER);
        register(fallDist, compareBelow);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        //save a safe position to teleport to
        if (!(BlockUtil.getBlockAt(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)) instanceof BlockAir)) {
            prevX = mc.thePlayer.posX;
            prevY = mc.thePlayer.posY;
            prevZ = mc.thePlayer.posZ;
        }

        //check if they should be teleported back to the safe position
        if (shouldTeleportBack()) {
            mc.thePlayer.setPositionAndUpdate(prevX, prevY, prevZ);
        }
    }

    public boolean shouldTeleportBack() {
        //check if fall distance is greater than fall distance required to tp back
        if (mc.thePlayer.fallDistance >= fallDist.getValue()) {
            //get the block 10 blocks under
            Block below = BlockUtil.getBlockAt(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - compareBelow.getValue(), mc.thePlayer.posZ));

            //if it's air this returns true.
            return below instanceof BlockAir;
        }
        return false;
    }

}
