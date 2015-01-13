package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

/**
 * Everything to do with pipes may need to be cleaned up and/or rewritten
 */
public class TileEntityPipe extends TileEntitySC implements IPipeInteractor
{
    private HashMap<EnumFacing, Boolean> pipeConnections = new HashMap<EnumFacing, Boolean>();
    private HashMap<EnumFacing, Boolean> machineConnections = new HashMap<EnumFacing, Boolean>();

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (EnumFacing side : pipeConnections.keySet())
        {
            compound.setBoolean("pipe" + side.getName2(), true);
        }

        for (EnumFacing side : machineConnections.keySet())
        {
            compound.setBoolean("machine" + side.getName2(), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("pipe" + side.getName2()))
            {
                this.pipeConnections.put(side, true);
            }
        }

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("machine" + side.getName2()))
            {
                this.machineConnections.put(side, true);
            }
        }
    }

    public HashMap<EnumFacing, Boolean> getPipeConnections()
    {
        return pipeConnections;
    }

    @Override
    public void setPipeConnections(HashMap<EnumFacing, Boolean> pipeConnections)
    {
        this.pipeConnections = pipeConnections;
    }

    public HashMap<EnumFacing, Boolean> getMachineConnections()
    {
        return machineConnections;
    }

    @Override
    public void setMachineConnections(HashMap<EnumFacing, Boolean> machineConnections)
    {
        this.machineConnections = machineConnections;
    }
}