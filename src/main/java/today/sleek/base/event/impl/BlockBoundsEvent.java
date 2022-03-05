package today.sleek.base.event.impl;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import today.sleek.base.event.Event;


public class BlockBoundsEvent extends Event
{
    private Block block;
    private BlockPos pos;
    private AxisAlignedBB bounds;


    public AxisAlignedBB getBounds() {
        return this.bounds;
    }

    public void setBounds(final AxisAlignedBB bounds) {
        this.bounds = bounds;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Block getBlock() {
        return this.block;
    }
}