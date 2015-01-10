package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.block.BlockContainerSC;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPipe extends BlockContainerSC
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockPipe()
    {
        super(Material.iron);
        this.setUnlocalizedName("pipe");
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(UP, Boolean.valueOf(false))
                .withProperty(DOWN, Boolean.valueOf(false))
                .withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false))
                .withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false)));
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        float bbEdge = 11 * (1 / 16F) / 2;
        float minX = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(WEST).equals(true)) ? 0 : bbEdge;
        float maxX = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(EAST).equals(true)) ? 1 : 1 - bbEdge;
        float minY = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(DOWN).equals(true)) ? 0 : bbEdge;
        float maxY = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(UP).equals(true)) ? 1 : 1 - bbEdge;
        float minZ = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(NORTH).equals(true)) ? 0 : bbEdge;
        float maxZ = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(SOUTH).equals(true)) ? 1 : 1 - bbEdge;

        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);

        return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        float bbEdge = 11 * (1 / 16F) / 2;
        float minX = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(WEST).equals(true)) ? 0 : bbEdge;
        float maxX = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(EAST).equals(true)) ? 1 : 1 - bbEdge;
        float minY = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(DOWN).equals(true)) ? 0 : bbEdge;
        float maxY = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(UP).equals(true)) ? 1 : 1 - bbEdge;
        float minZ = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(NORTH).equals(true)) ? 0 : bbEdge;
        float maxZ = (getActualState(this.blockState.getBaseState(), worldIn, pos).getValue(SOUTH).equals(true)) ? 1 : 1 - bbEdge;

        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);

        return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
    }

    @Override
    public boolean isOpaqueCube()
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
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity instanceof IPipeInteractor;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state
                .withProperty(UP, Boolean.valueOf(this.canConnectTo(worldIn, pos.up())))
                .withProperty(DOWN, Boolean.valueOf(this.canConnectTo(worldIn, pos.down())))
                .withProperty(NORTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.north())))
                .withProperty(EAST, Boolean.valueOf(this.canConnectTo(worldIn, pos.east())))
                .withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.south())))
                .withProperty(WEST, Boolean.valueOf(this.canConnectTo(worldIn, pos.west())));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityPipe();
    }
}
