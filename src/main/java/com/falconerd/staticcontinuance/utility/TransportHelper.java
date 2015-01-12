package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.machine.TileEntityMachine;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.*;

/**
 * This class helps manage the fluid transport system in Static Continuance
 */
public class TransportHelper
{
    public static void lagTest(TileEntityPipe from, World world)
    {
        Set<TileEntityMachine> machines = new HashSet<TileEntityMachine>();
        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();
        Set<TileEntityPipe> visited = new HashSet<TileEntityPipe>();

        long timeStart = System.nanoTime();
        int count = 0;

        queue.add(from);

        while (!queue.isEmpty())
        {
            TileEntityPipe current = queue.poll();

            if (!visited.contains(current))
            {
                visited.add(current);

                for (EnumFacing side : current.machineConnections.keySet())
                {
                    count++;

                    TileEntityMachine machine = (TileEntityMachine) world.getTileEntity(current.getPos().offset(side));

                    if (machine != null)
                    {
                        if (!machines.contains(machine))
                        {
                            machines.add(machine);
                        }
                    }
                }

                for (EnumFacing side : current.pipeConnections.keySet())
                {
                    count++;

                    TileEntityPipe next = (TileEntityPipe) world.getTileEntity(current.getPos().offset(side));

                    if (next != null)
                    {
                        if (!visited.contains(next))
                        {
                            queue.add(next);
                        }
                    }
                }
            }
        }

        long timeEnd = System.nanoTime();
        long timeTaken = timeEnd - timeStart;

        LogHelper.info("Machines: " + machines.size() + " | Pipes: " + visited.size() + "  | Calculations: " + count + " | Time: " + timeTaken + " nanoseconds");

    }

    public static List<TileEntityPipe> getConnectedPipes(TileEntityPipe from)
    {
        List<TileEntityPipe> pipes = new ArrayList<TileEntityPipe>();

        for (EnumFacing side : from.pipeConnections.keySet())
        {
            if (from.pipeConnections.get(side) != null)
            {
                pipes.add((TileEntityPipe) from.getWorld().getTileEntity(from.getPos().offset(side)));
            }
        }

        return pipes;
    }

}
