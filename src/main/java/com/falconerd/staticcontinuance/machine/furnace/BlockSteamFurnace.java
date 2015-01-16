package com.falconerd.staticcontinuance.machine.furnace;

import com.falconerd.staticcontinuance.machine.BlockFluidMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSteamFurnace extends BlockFluidMachine
{
    public BlockSteamFurnace(Material material)
    {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySteamFurnace(8000);
    }
}
