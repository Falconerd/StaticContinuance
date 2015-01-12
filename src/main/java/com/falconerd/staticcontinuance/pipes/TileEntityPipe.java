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
    public HashMap<EnumFacing, Boolean> pipeConnections = new HashMap<EnumFacing, Boolean>();
    public HashMap<EnumFacing, Boolean> machineConnections = new HashMap<EnumFacing, Boolean>();

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (EnumFacing side : pipeConnections.keySet())
        {
            compound.setBoolean("pipe" + side.getName2(), true);
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
    }

    public void updateConnections(boolean once)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos.offset(side));

            if (tileEntity instanceof TileEntityPipe)
            {
                pipeConnections.put(side, true);
                if (!once) ((TileEntityPipe) tileEntity).updateConnections(true);
            } else if (tileEntity instanceof TileEntityFluidMachine)
            {
                machineConnections.put(side, true);
            } else
            {
                pipeConnections.remove(side);
                machineConnections.remove(side);
            }
        }
    }

}
