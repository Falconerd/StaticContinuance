package com.falconerd.staticcontinuance.block;

import com.falconerd.staticcontinuance.tileentity.TileEntityTank;
import com.falconerd.staticcontinuance.utility.FluidHelper;
import com.falconerd.staticcontinuance.utility.ItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTank extends BlockContainerSC
{
    public BlockTank()
    {
        super(Material.glass);
        this.setUnlocalizedName("tank");
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityTank))
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
        TileEntityTank tank = (TileEntityTank) te;
        ItemStack item = playerIn.inventory.getCurrentItem();
        if (item == null)
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }

        FluidStack fluid = FluidHelper.getFluidFromItem(item);
        if (fluid != null)
        {
            int filled = tank.fill(EnumFacing.UP, fluid, false);
            if (filled >= fluid.amount)
            {
                tank.fill(EnumFacing.UP, fluid, true);
                if (!playerIn.capabilities.isCreativeMode)
                {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemHelper.consumeItem(item));
                }
                return true;
            }
        }

        FluidStack available = tank.tank.getFluid();
        if (available != null)
        {
            ItemStack res = FluidContainerRegistry.fillFluidContainer(available.copy(), item);
            FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(res);

            if (filled == null)
            {
                FluidContainerRegistry.FluidContainerData[] datas = FluidContainerRegistry.getRegisteredFluidContainerData();
                for (FluidContainerRegistry.FluidContainerData data : datas)
                {
                    if (data != null && data.fluid != null && data.fluid.getFluid() != null &&
                            data.fluid.getFluid().getName() != null && data.emptyContainer != null &&
                            data.fluid.getFluid().getName().equals(available.getFluid().getName()) &&
                            data.emptyContainer.isItemEqual(item))
                    {
                        res = data.filledContainer.copy();
                        // TODO: filled never used?
                        //filled = FluidContainerRegistry.getFluidForFilledItem(res);
                    }
                }
            } else
            {
                tank.drain(EnumFacing.DOWN, filled, true);
                if (item.stackSize > 1)
                {
                    item.stackSize--;
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, item);
                    for (int i = 0; i < playerIn.inventory.mainInventory.length; i++)
                    {
                        if (playerIn.inventory.mainInventory[i] == null)
                        {
                            playerIn.inventory.setInventorySlotContents(i, res);
                            return true;
                        }
                    }
                    if (!worldIn.isRemote)
                    {
                        ItemHelper.dropItems(worldIn, res, pos, true);
                    }
                } else
                {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, res);
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTank();
    }
}
