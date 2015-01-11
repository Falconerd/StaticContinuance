package com.falconerd.staticcontinuance.block;

import com.falconerd.staticcontinuance.creativetab.CreativeTabSC;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidSC extends BlockFluidClassic
{
    public BlockFluidSC(Fluid fluid, Material material)
    {
        super(fluid, material);
        this.setCreativeTab(CreativeTabSC.SC_TAB);
    }
}
