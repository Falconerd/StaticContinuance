package com.falconerd.staticcontinuance.tileentity;

import com.falconerd.staticcontinuance.pipes.IPipeInteractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class TileEntityTank extends TileEntity implements IFluidHandler, IPipeInteractor
{
    public FluidTank tank = new FluidTank(16000);
    private boolean needsUpdate = false;
    private int updateTimer = 0;

    public TileEntityTank()
    {
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        this.needsUpdate = true;
        return this.tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        this.needsUpdate = true;
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    public float getAdjustedVolume()
    {
        float amount = tank.getFluidAmount();
        float capacity = tank.getCapacity();
        return (amount / capacity) * .8F;
    }

    public void updateEntity()
    {
        if (needsUpdate)
        {
            if (updateTimer == 0)
            {
                updateTimer = 10;
            } else
            {
                --updateTimer;
                if (updateTimer == 0)
                {
                    worldObj.markBlockForUpdate(this.getPos());
                    needsUpdate = false;
                }
            }
        }
    }
}
