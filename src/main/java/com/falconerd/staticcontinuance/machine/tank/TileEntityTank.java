package com.falconerd.staticcontinuance.machine.tank;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.utility.NetworkHelper;
import com.falconerd.staticcontinuance.utility.TransportHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

public class TileEntityTank extends TileEntityFluidMachine implements IUpdatePlayerListBox
{
    public int fluidTransferRatePerTick = 1000;
    // I prefer to map connections like this rather than use an array with indexes 0-5
    private HashMap<EnumFacing, Boolean> connections = new HashMap<EnumFacing, Boolean>();
    /**
     * The amount of ticks in between each update. 20 ticks = ~1 second dependant on lag
     */
    private int updateRate = 20;
    /**
     * The timer which keeps track of the updates
     */
    private int updateTimer = updateRate;

    public TileEntityTank()
    {
        // Set all of the connections to false when initialized
        NetworkHelper.populateDirections(connections, false);
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

    public float getAdjustedVolume()
    {
        float amount = tank.getFluidAmount();
        float capacity = tank.getCapacity();
        return (amount / capacity) * .8F;
    }

    public int getFluidLightLevel()
    {
        return 0;
    }

    /**
     * This method is called every tick. Use doUpdate()
     */
    @Override
    public void update()
    {
        if (needsUpdate)
        {
            worldObj.markBlockForUpdate(pos);
            needsUpdate = false;
        }
        if (updateTimer == 0)
        {
            updateTimer = updateRate;
        } else
        {
            --updateTimer;
            if (updateTimer == 0)
            {
                doUpdate();
            }
        }
    }

    /**
     * This method is called every updateRate ticks. It's done this way so as not to cause logic updates every tick.
     * That could be very laggy if used incorrectly.
     */
    public void doUpdate()
    {
        // Server update
        if (worldObj.isRemote)
        {
            if (this.mode == 1) this.distributeFluid();
        }
        // Client update
        else
        {

        }
    }

    public void distributeFluid()
    {
        TransportHelper.transferFluid(this);
    }

    public int getMode()
    {
        return mode;
    }
}
