package com.falconerd.staticcontinuance.machine.tank;

import net.minecraftforge.fluids.FluidTank;

public class FluidTankSC extends FluidTank
{
    public FluidTankSC(int capacity)
    {
        super(capacity);
    }

    public boolean isEmpty()
    {
        return this.getFluidAmount() == 0;
    }

    public int getCapacityRemaining()
    {
        return this.getCapacity() - this.getFluidAmount();
    }
}
