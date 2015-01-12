package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.machine.TileEntityMachine;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * This class helps manage the fluid transport system in Static Continuance
 */
public class TransportHelper
{
    /**
     * This method is used to scan a pipe network. It uses BFS (Breadth-first search) to find all nodes and mark them.
     *
     * @param from  The starting point.
     * @param world The Minecraft world.
     */
    public static void lagTest(TileEntityPipe from, World world)
    {
        // This is the set of machines that are found
        Set<TileEntityMachine> machines = new HashSet<TileEntityMachine>();

        // This is the set of pipes we have already visited
        Set<TileEntityPipe> visited = new HashSet<TileEntityPipe>();

        // This is the queue of nodes to traverse
        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

        // Time starting this operation
        long timeStart = System.nanoTime();

        // Counter used for number of "calculations"
        int count = 0;

        // Add the starting point to the queue
        queue.add(from);

        // While the queue is not empty
        while (!queue.isEmpty())
        {
            // Remove a node from the queue and set it as the current node
            TileEntityPipe current = queue.poll();

            // If we have not already been here
            if (!visited.contains(current))
            {
                // Mark this place as somewhere we have been
                visited.add(current);

                // Get the direction of machines connected to this node
                for (EnumFacing side : current.machineConnections.keySet())
                {
                    // Increase the count
                    count++;

                    // Get the machine in the direction specified
                    TileEntityMachine machine = (TileEntityMachine) world.getTileEntity(current.getPos().offset(side));

                    // If we indeed have a machine here
                    if (machine != null)
                    {
                        // If the machine is not already in the list of machines
                        if (!machines.contains(machine))
                        {
                            // Add the machine to the list of machines
                            machines.add(machine);
                        }
                    }
                }

                // Get the direction of pipes connected to this pipe
                for (EnumFacing side : current.pipeConnections.keySet())
                {
                    // Increase the count
                    count++;

                    // Get the pipe in the direction specified
                    TileEntityPipe pipe = (TileEntityPipe) world.getTileEntity(current.getPos().offset(side));

                    // If we do have a pipe here
                    if (pipe != null)
                    {
                        // If we haven't been to this node
                        if (!visited.contains(pipe))
                        {
                            // Add this node to the queue
                            queue.add(pipe);
                        }
                    }
                }
            }
        }

        // The end time for this operation
        long timeEnd = System.nanoTime();

        // The total time taken in nanoseconds
        long timeTaken = timeEnd - timeStart;

        LogHelper.info("Machines: " + machines.size() + " | Pipes: " + visited.size() + "  | Calculations: " + count + " | Time: " + timeTaken + " nanoseconds");

    }

}
