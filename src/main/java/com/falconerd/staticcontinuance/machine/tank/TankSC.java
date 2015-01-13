package com.falconerd.staticcontinuance.machine.tank;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class TankSC extends FluidTank
{
    String name = "";

    public TankSC(int capacity)
    {
        super(capacity);
    }

    public boolean isEmpty()
    {
        return getFluid() == null || getFluid().amount <= 0;
    }

    public boolean isFull()
    {
        return getFluid() != null && getFluid().amount >= capacity;
    }

    public Fluid getFluidType()
    {
        return getFluid() != null ? getFluid().getFluid() : null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagCompound tankData = new NBTTagCompound();
        super.writeToNBT(tankData);
        writeTankToNBT(tankData);
        nbt.setTag(name, tankData);
        return nbt;
    }

    @Override
    public final FluidTank readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey(name))
        {
            setFluid(null);
            NBTTagCompound tankData = nbt.getCompoundTag(name);
            super.readFromNBT(tankData);
            readTankFromNBT(tankData);
        }
        return this;
    }

    public void writeTankToNBT(NBTTagCompound nbt)
    {
    }

    public void readTankFromNBT(NBTTagCompound nbt)
    {
    }

    public int getRemainingCapacity()
    {
        return this.getCapacity() - this.getFluidAmount();
    }
}
