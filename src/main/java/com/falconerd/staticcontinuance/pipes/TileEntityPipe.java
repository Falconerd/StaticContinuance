package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

/**
 * Everything to do with pipes may need to be cleaned up and/or rewritten
 */
public class TileEntityPipe extends TileEntitySC implements IPipeInteractor
{
    public HashMap<EnumFacing, Boolean> pipeConnections = new HashMap<EnumFacing, Boolean>();
    public HashMap<EnumFacing, Boolean> machineConnections = new HashMap<EnumFacing, Boolean>();

    public TileEntityPipe()
    {
        for (EnumFacing side : EnumFacing.values())
        {
            pipeConnections.put(side, false);
        }
    }

    public void updateConnections(boolean once)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            // Get the tile entity on this side
            TileEntity tileEntity = worldObj.getTileEntity(pos.offset(side));

            // Check to see if it's a pipe
            if (tileEntity instanceof TileEntityPipe)
            {
                if (!once)
                {
                    ((TileEntityPipe) tileEntity).updateConnections(true);
                }

                // Set this as a valid pipe connection
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

//        LogHelper.info("Pipe connections: " + pipeConnections);
//        LogHelper.info("Machine connections: " + machineConnections);
    }
}
