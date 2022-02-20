package me.kansio.client.event.impl;

import me.kansio.client.event.Event;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;


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