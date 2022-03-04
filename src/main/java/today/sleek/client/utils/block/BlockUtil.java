package today.sleek.client.utils.block;

import today.sleek.client.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockUtil extends Util {

    public static Block getBlockAt(BlockPos bpos) {
        return mc.theWorld.getBlockState(bpos).getBlock();
    }

}
