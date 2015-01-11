package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.*;

public class MachineFinder
{
    public static List<TileEntityMachine> getConnectedMachines(TileEntityMachine tileEntityMachine)
    {
        // This is the map we will eventually return
        List<TileEntityMachine> machines = new ArrayList<TileEntityMachine>();

        // Ths is the queue which we add pipes to, to search the network
        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

        // This is a list of visited places
        HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

        // Make sure we are connected to a pipe
        if (isConnectedToPipe(tileEntityMachine))
        {
            // Add the pipes we are connected to into the queue
            for (TileEntityPipe pipe : getConnectedPipes(tileEntityMachine))
            {
                queue.add(pipe);
            }

            while (!queue.isEmpty())
            {
                // Remove and set the first element as a variable
                TileEntityPipe current = queue.poll();
                if (current != null)
                {
                    // Add this to the list of places we have visited
                    visited.put(current, true);

                    // Check to see if this pipe is connected to any machines
                    if (isConnectedToMachine(current))
                    {
                        // Iterate through connected machines
                        for (TileEntityMachine machine : getConnectedMachines(current))
                        {
                            // Check if this machine takes fluid
                            if (machine instanceof IFluidHandler)
                            {
                                // Add the machine to the list
                                machines.add(machine);
                            }
                        }
                    }

                    // Search for pipes that are connected to this one
                    for (TileEntityPipe next : getConnectedPipes(current))
                    {
                        // If we haven't been there before
                        if (!visited.containsKey(next))
                        {
                            // Add it to the queue
                            queue.add(next);
                        }
                    }
                }
            }

        }

//        LogHelper.info("Visited this many places: " + visited.size());
//
//        for (TileEntityPipe pipe : visited.keySet())
//        {
//            LogHelper.info("Pipe @: " + pipe.getPos());
//        }
//
//        for (TileEntityMachine machine : machines)
//        {
//            LogHelper.info("Machine @: " + machine.getPos());
//        }

        return machines;
    }

    public static boolean isConnectedToPipe(TileEntityMachine tileEntityMachine)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = tileEntityMachine.getWorld().getTileEntity(tileEntityMachine.getPos().offset(side));
            if (tileEntity instanceof TileEntityPipe)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isConnectedToMachine(TileEntityPipe tileEntityPipe)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = tileEntityPipe.getWorld().getTileEntity(tileEntityPipe.getPos().offset(side));
            if (tileEntity instanceof TileEntityMachine)
            {
                return true;
            }
        }
        return false;
    }

    public static List<TileEntityPipe> getConnectedPipes(TileEntityMachine tileEntityMachine)
    {
        List<TileEntityPipe> pipes = new ArrayList<TileEntityPipe>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = tileEntityMachine.getWorld().getTileEntity(tileEntityMachine.getPos().offset(side));
            if (tileEntity instanceof TileEntityPipe)
            {
                pipes.add((TileEntityPipe) tileEntity);
            }
        }

        return pipes;
    }

    public static List<TileEntityPipe> getConnectedPipes(TileEntityPipe tileEntityPipe)
    {
        List<TileEntityPipe> pipes = new ArrayList<TileEntityPipe>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = tileEntityPipe.getWorld().getTileEntity(tileEntityPipe.getPos().offset(side));
            if (tileEntity instanceof TileEntityPipe)
            {
                pipes.add((TileEntityPipe) tileEntity);
            }
        }

        return pipes;
    }

    public static List<TileEntityMachine> getConnectedMachines(TileEntityPipe tileEntityPipe)
    {
        List<TileEntityMachine> machines = new ArrayList<TileEntityMachine>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = tileEntityPipe.getWorld().getTileEntity(tileEntityPipe.getPos().offset(side));
            if (tileEntity instanceof TileEntityMachine)
            {
                machines.add((TileEntityMachine) tileEntity);
            }
        }

        return machines;
    }
}
