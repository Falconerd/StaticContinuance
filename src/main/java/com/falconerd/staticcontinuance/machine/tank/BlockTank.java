package com.falconerd.staticcontinuance.machine.tank;

import com.falconerd.staticcontinuance.machine.BlockFluidMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTank extends BlockFluidMachine
{
    public BlockTank()
    {
        super(Material.glass);
        this.setUnlocalizedName("tank");
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        TileEntityTank tank = (TileEntityTank) world.getTileEntity(pos);
        return tank != null ? tank.getFluidLightLevel() : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTank();
    }
}
