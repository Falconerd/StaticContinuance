package com.falconerd.staticcontinuance.init;

import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids
{
    public static Fluid fluidSteam;

    public static void init()
    {
        fluidSteam = FluidRegistry.getFluid("steam");
        if (fluidSteam == null)
        {
            fluidSteam = new Fluid("steam");
            fluidSteam.setUnlocalizedName("steam");
            fluidSteam.setTemperature(1000);
            fluidSteam.setGaseous(true);
            fluidSteam.setLuminosity(0);
            fluidSteam.setRarity(EnumRarity.COMMON);
            fluidSteam.setDensity(6);

            FluidRegistry.registerFluid(fluidSteam);
        }
    }
}
