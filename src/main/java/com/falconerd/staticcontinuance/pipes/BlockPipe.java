package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.block.BlockContainerSC;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Pretty sure the only pipes I want to make are liquid ones.
 * However, just in case; I'll make it extendable
 */
public class BlockPipe extends BlockContainerSC
{
    /**
     * Maybe instead of pipes having their own
     * internal tanks, they just figure out from
     * where to where they are meant to be going.
     */
    public BlockPipe()
    {
        super(Material.iron);

        float pixel = 1F / 16F;
        this.setUnlocalizedName("pipe");
        this.setBlockBounds(11 * pixel / 2, 11 * pixel / 2, 11 * pixel / 2, 1 - 11 * pixel / 2, 1 - 11 * pixel / 2, 1 - 11 * pixel / 2);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        TileEntityPipe pipe = (TileEntityPipe) worldIn.getTileEntity(pos);
        float pixel = 1F / 16F;

        if (pipe != null)
        {
            float minX = pipe.connections.get(EnumFacing.WEST) ? 0 : 11 * pixel / 2;
            float maxX = pipe.connections.get(EnumFacing.EAST) ? 1 : 1 - 11 * pixel / 2;
            float minY = pipe.connections.get(EnumFacing.DOWN) ? 0 : 11 * pixel / 2;
            float maxY = pipe.connections.get(EnumFacing.UP) ? 1 : 1 - 11 * pixel / 2;
            float minZ = pipe.connections.get(EnumFacing.NORTH) ? 0 : 11 * pixel / 2;
            float maxZ = pipe.connections.get(EnumFacing.SOUTH) ? 1 : 1 - 11 * pixel / 2;

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityPipe pipe = (TileEntityPipe) worldIn.getTileEntity(pos);
        float pixel = 1F / 16F;

        if (pipe != null)
        {
            float minX = pipe.connections.get(EnumFacing.WEST) ? 0 : 11 * pixel / 2;
            float maxX = pipe.connections.get(EnumFacing.EAST) ? 1 : 1 - 11 * pixel / 2;
            float minY = pipe.connections.get(EnumFacing.DOWN) ? 0 : 11 * pixel / 2;
            float maxY = pipe.connections.get(EnumFacing.UP) ? 1 : 1 - 11 * pixel / 2;
            float minZ = pipe.connections.get(EnumFacing.NORTH) ? 0 : 11 * pixel / 2;
            float maxZ = pipe.connections.get(EnumFacing.SOUTH) ? 1 : 1 - 11 * pixel / 2;

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntityPipe tileEntityPipe = (TileEntityPipe) worldIn.getTileEntity(pos);

        if (tileEntityPipe != null)
        {
            tileEntityPipe.updateConnections(false);
            ValveFinder.updateNetwork(tileEntityPipe);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityPipe tileEntityPipe = (TileEntityPipe) worldIn.getTileEntity(pos);

        if (tileEntityPipe != null)
        {
            tileEntityPipe.updateConnections(false);
        }
    }

    /**
     * I am unsure which of these I need to make it render how I want,
     * please let me know if you know.
     * Essentially; no shadows, custom rendered pipe.
     */

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isNormalCube()
    {
        return false;
    }

    @Override
    public boolean isFullBlock()
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityPipe();
    }
}
