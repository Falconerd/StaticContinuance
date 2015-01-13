package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

/**
 * Everything to do with pipes may need to be cleaned up and/or rewritten
 */
public class TileEntityPipe extends TileEntitySC
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
            if (compound.getBoolean("pipe" + side.getName2()))
            {
                this.machineConnections.put(side, true);
            }
        }
    }

    public void updateConnections(boolean once)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos.offset(side));

            if (tileEntity instanceof TileEntityPipe)
            {
                if (!once) ((TileEntityPipe) tileEntity).updateConnections(true);
                pipeConnections.put(side, true);
                machineConnections.remove(side);
            } else if (tileEntity instanceof TileEntityFluidMachine)
            {
                if (!once) ((TileEntityFluidMachine) tileEntity).updateConnections(true);
                machineConnections.put(side, true);
                pipeConnections.remove(side);
            } else
            {
                pipeConnections.remove(side);
                machineConnections.remove(side);
            }
        }
    }

    public HashMap<EnumFacing, Boolean> getPipeConnections()
    {
        return pipeConnections;
    }

    public HashMap<EnumFacing, Boolean> getMachineConnections()
    {
        return machineConnections;
    }
}
