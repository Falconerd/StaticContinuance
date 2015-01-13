package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.block.BlockContainerSC;
import com.falconerd.staticcontinuance.utility.TransportHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
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
                .withProperty(UP, false)
                .withProperty(DOWN, false)
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false));
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TransportHelper.mapNode(pos, worldIn, false);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!worldIn.isRemote)
        {
            TransportHelper.mapNode(pos, worldIn, true);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            TransportHelper.mapNode(pos, worldIn, true);
        }
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
        return tileEntity instanceof IFluidHandler || tileEntity instanceof TileEntityPipe;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state
                .withProperty(UP, this.canConnectTo(worldIn, pos.up()))
                .withProperty(DOWN, this.canConnectTo(worldIn, pos.down()))
                .withProperty(NORTH, this.canConnectTo(worldIn, pos.north()))
                .withProperty(EAST, this.canConnectTo(worldIn, pos.east()))
                .withProperty(SOUTH, this.canConnectTo(worldIn, pos.south()))
                .withProperty(WEST, this.canConnectTo(worldIn, pos.west()));
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
