package com.falconerd.staticcontinuance.machine.tank;

import com.falconerd.staticcontinuance.machine.TileEntityMachine;
import com.falconerd.staticcontinuance.pipes.IPipeInteractor;
import com.falconerd.staticcontinuance.utility.NetworkHelper;
import com.falconerd.staticcontinuance.utility.PacketHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

import java.util.HashMap;

public class TileEntityTank extends TileEntityMachine implements IFluidHandler, IPipeInteractor
{
    public int mode = 0;
    public FluidTank tank = new FluidTank(16000);
    private boolean needsUpdate = false;

    /////////////////////////////// ^ MODE STUFF ^ /////////////////////////////////////////////////////////////////////
    private int updateTimer = 0;
    // I prefer to map connections like this rather than use an array with indexes 0-5
    private HashMap<EnumFacing, Boolean> connections = new HashMap<EnumFacing, Boolean>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TileEntityTank()
    {
        // Set all of the connections to false when initialized
        NetworkHelper.populateDirections(connections, false);
    }

    public void switchMode()
    {
        this.mode++;
        PacketHelper.setTankMode(this, this.mode);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public double getFluidRatio()
    {
        return (double) tank.getFluidAmount() / (double) tank.getCapacity();
    }

    public double getFluidRenderHeight()
    {
        double renderHeight = getFluidRatio();
        if (renderHeight <= 0.02) return 0.02;
        if (renderHeight > 0.98) return 1.0;

        return Math.max(0, renderHeight);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setInteger("mode", this.mode);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.mode = compound.getInteger("mode");
    }

    public HashMap getConnections()
    {
        return connections;
    }

    public void updateConnections(boolean once)
    {
        for (EnumFacing facing : connections.keySet())
        {
            // Get the tile entity of possible connection
            TileEntity tileEntity = worldObj.getTileEntity(getPos().offset(facing));

            // If the te next to us is a tank, then we put true, else false
            if (tileEntity instanceof TileEntityTank)
            {
                connections.put(facing, true);
            } else
            {
                connections.put(facing, false);
            }
        }
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

    public int getFluidLightLevel()
    {
        return 0;
    }


}
