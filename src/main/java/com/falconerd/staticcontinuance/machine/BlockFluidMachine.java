package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.utility.FluidHelper;
import com.falconerd.staticcontinuance.utility.ItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class BlockFluidMachine extends BlockMachine
{
    public BlockFluidMachine(Material material)
    {
        super(material);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityFluidMachine))
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
        TileEntityFluidMachine fluidMachine = (TileEntityFluidMachine) te;

//        fluidMachine.updateConnections(true);
//        LogHelper.info(fluidMachine.getConnections());

        ItemStack item = playerIn.inventory.getCurrentItem();
        if (item == null)
        {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }

        FluidStack fluid = FluidHelper.getFluidFromItem(item);
        if (fluid != null)
        {
            int filled = fluidMachine.fill(EnumFacing.UP, fluid, false);
            if (filled >= fluid.amount)
            {
                fluidMachine.fill(EnumFacing.UP, fluid, true);
                if (!playerIn.capabilities.isCreativeMode)
                {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemHelper.consumeItem(item));
                }
                return true;
            }
        }

        FluidStack available = fluidMachine.tank.getFluid();
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
                fluidMachine.drain(EnumFacing.DOWN, filled, true);
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
        if (!worldIn.isRemote)
        {
            wrenchInteraction(playerIn, worldIn, pos);
        }

        return false;
    }
}
