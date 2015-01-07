package com.falconerd.staticcontinuance.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

/**
 * Use this class for machines and other blocks which need TileEntity functionality.
 */
public abstract class BlockContainerSC extends BlockContainer
{
    public BlockContainerSC()
    {
        super(Material.iron);
    }

    public BlockContainerSC(Material material)
    {
        super(material);
    }
}
