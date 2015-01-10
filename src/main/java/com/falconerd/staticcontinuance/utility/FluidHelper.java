package com.falconerd.staticcontinuance.utility;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;

public class FluidHelper
{
    public static FluidStack getFluidFromItem(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            FluidStack fluidStack = null;
            if (itemStack.getItem() instanceof IFluidContainerItem)
            {
                fluidStack = ((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack);
            }
            if (fluidStack == null)
            {
                fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            }
            if (fluidStack == null && Block.getBlockFromItem(itemStack.getItem()) instanceof IFluidBlock)
            {
                Fluid fluid = ((IFluidBlock) Block.getBlockFromItem(itemStack.getItem())).getFluid();
                if (fluid != null)
                {
                    return new FluidStack(fluid, 1000);
                }
            }
            return fluidStack;
        }
        return null;
    }

    public static ResourceLocation getFluidResourceLocation(FluidStack fluidStack)
    {
        String location = "";

        if (fluidStack.getFluid().equals(FluidRegistry.WATER))
        {
            location = "minecraft:textures/blocks/water_still.png";
        } else if (fluidStack.getFluid().equals(FluidRegistry.LAVA))
        {
            location = "minecraft:textures/blocks/lava_still.png";
        } else
        {
            LogHelper.info("Neither water nor lava... Not sure what to do!");
        }

        return new ResourceLocation(location);
    }
}
