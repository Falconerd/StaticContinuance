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

        // I'm gonna hazard a guess that there's a better way to do this.
        compound.setBoolean("pipeUP", pipeConnections.get(EnumFacing.UP));
        compound.setBoolean("pipeDOWN", pipeConnections.get(EnumFacing.DOWN));
        compound.setBoolean("pipeNORTH", pipeConnections.get(EnumFacing.NORTH));
        compound.setBoolean("pipeEAST", pipeConnections.get(EnumFacing.EAST));
        compound.setBoolean("pipeSOUTH", pipeConnections.get(EnumFacing.SOUTH));
        compound.setBoolean("pipeWEST", pipeConnections.get(EnumFacing.WEST));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.pipeConnections.put(EnumFacing.UP, compound.getBoolean("pipeUP"));
        this.pipeConnections.put(EnumFacing.DOWN, compound.getBoolean("pipeDOWN"));
        this.pipeConnections.put(EnumFacing.NORTH, compound.getBoolean("pipeNORTH"));
        this.pipeConnections.put(EnumFacing.EAST, compound.getBoolean("pipeEAST"));
        this.pipeConnections.put(EnumFacing.SOUTH, compound.getBoolean("pipeSOUTH"));
        this.pipeConnections.put(EnumFacing.WEST, compound.getBoolean("pipeWEST"));
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
            } else if (tileEntity instanceof TileEntityFluidMachine)
            {
                machineConnections.put(side, true);
            } else
            {
                pipeConnections.put(side, false);
                machineConnections.put(side, false);
            }
        }
    }
}
