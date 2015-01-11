package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.*;

/**
 * This class helps manage the fluid transport system in Static Continuance
 */
public class TransportHelper
{
    /**
     * This method updates the maps for each connected machine.
     * @param from The tile entity which triggered the change.
     */
    public static void updateNetworkMap(TileEntity from)
    {
        LogHelper.info("UPDATING NETWORK");

        if (from instanceof TileEntityPipe || from instanceof TileEntityFluidMachine)
        {

            LogHelper.info("THIS IS A PIPE OR MACHINE");

            HashMap<TileEntityFluidMachine, Boolean> machines = new HashMap<TileEntityFluidMachine, Boolean>();

            Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

            HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

            for (TileEntityPipe pipe : getConnectedPipes(from))
            {
                queue.add(pipe);
            }

            LogHelper.info(queue);

            while (!queue.isEmpty())
            {
                TileEntityPipe current = queue.poll();

                visited.put(current, true);

                for (TileEntityFluidMachine machine : getConnectedMachines(current))
                {
                    if (!machines.containsKey(machine))
                    {
                        machines.put(machine, true);
                    }
                }

                LogHelper.info(current.getPos() + " : " + getConnectedPipes(current));

                for (TileEntityPipe pipe : getConnectedPipes(current))
                {
                    if (!visited.containsKey(pipe))
                    {
                        queue.add(pipe);
                    }
                }
            }


            for (TileEntityFluidMachine machine : machines.keySet())
            {
                machine.networkedMachines = getMachineMap(machine);
            }
        } else
        {
            LogHelper.warn("The TileEntity at position " + from.getPos() + " shoud not be trying to update the fluid network.");
        }
    }

    public static TreeMap<Integer, TileEntityFluidMachine> getMachineMap(TileEntityFluidMachine from)
    {
        LogHelper.info("Getting machine map");

        TreeMap<Integer, TileEntityFluidMachine> machines = new TreeMap<Integer, TileEntityFluidMachine>();

        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

        HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

        HashMap<TileEntityFluidMachine, Boolean> visitedMachines = new HashMap<TileEntityFluidMachine, Boolean>();

        int distance = 0;

        for (TileEntityPipe pipe : getConnectedPipes(from))
        {
            queue.add(pipe);
        }

        while (!queue.isEmpty())
        {
            TileEntityPipe current = queue.poll();

            visited.put(current, true);

            distance++;

            for (TileEntityFluidMachine machine : getConnectedMachines(current))
            {
                if (!visitedMachines.containsKey(machine))
                {
                    visitedMachines.put(machine, true);
                    if (machine != from)
                    {
                        machines.put(distance, machine);
                    }
                }
            }

            for (TileEntityPipe pipe : getConnectedPipes(current))
            {
                if (!visited.containsKey(pipe))
                {
                    queue.add(pipe);
                }
            }
        }

        LogHelper.info("Visited this many pipes: " + visited.size());

        return machines;
    }

    /**
     * This method returns a list of connected machines.
     *
     * @param from Pipe which the machines are connected to.
     * @return A list of machines. Will be empty if none are found.
     */
    public static List<TileEntityFluidMachine> getConnectedMachines(TileEntityPipe from)
    {
        List<TileEntityFluidMachine> machines = new ArrayList<TileEntityFluidMachine>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity pipe = from.getWorld().getTileEntity(from.getPos().offset(side));

            if (pipe != null)
            {
                if (pipe instanceof TileEntityFluidMachine)
                {
                    machines.add((TileEntityFluidMachine) pipe);
                }
            }
        }

        return machines;
    }

    /**
     * This method returns a list of connected pipes.
     * @param from What the pipes are connected to.
     * @return A list of pipes. Will be empty if none are found.
     */
    public static List<TileEntityPipe> getConnectedPipes(TileEntity from)
    {
        List<TileEntityPipe> pipes = new ArrayList<TileEntityPipe>();

        if (from instanceof TileEntityPipe)
        {
            TileEntityPipe pipe = (TileEntityPipe) from;

            for (EnumFacing side : EnumFacing.values())
            {
                if (pipe.pipeConnections.get(side))
                {
                    pipes.add((TileEntityPipe) pipe.getWorld().getTileEntity(pipe.getPos().offset(side)));
                }
            }
        } else if (from instanceof TileEntityFluidMachine)
        {
            for (EnumFacing side : EnumFacing.values())
            {
                TileEntity pipe = from.getWorld().getTileEntity(from.getPos().offset(side));

                if (pipe instanceof TileEntityPipe)
                {
                    pipes.add((TileEntityPipe) pipe);
                }
            }
        }

        return pipes;
    }
}
