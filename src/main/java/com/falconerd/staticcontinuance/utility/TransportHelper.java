package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * This class helps manage the fluid transport system in Static Continuance
 */
public class TransportHelper
{
    public static void transferFluid(TileEntityFluidMachine from)
    {
        TileEntityFluidMachine to = getNearestFluidAcceptor(from);

        if (to != null)
        {
            int transferAmount = ConfigurationHandler.pipeFluidTransferRate * (20 / ConfigurationHandler.fluidNetworkTickDelay);

            int capacity = to.getTank().getRemainingCapacity();
            int fluidAmount = from.getTank().getFluidAmount();

            int requiredCapacity = transferAmount > fluidAmount ? fluidAmount : transferAmount;

            if (capacity < requiredCapacity) requiredCapacity = capacity;

            FluidStack fluidStack = from.getTank().drain(requiredCapacity, true);
            to.getTank().fill(fluidStack, true);
        }
    }

    public static TileEntityFluidMachine getNearestFluidAcceptor(TileEntityFluidMachine from)
    {
        Set<TileEntityFluidMachine> machines = from.getNetworkedMachines();
        Iterator iterator = machines.iterator();
        LogHelper.info("Trying to get nearest fluid acceptor from list of size: " + machines.size());

        while (iterator.hasNext())
        {
            TileEntityFluidMachine machine = (TileEntityFluidMachine) iterator.next();

            if (machine != from)
            {
                if (!machine.getTank().isFull())
                {
                    if (machine.getMode() == Reference.MACHINE_MODE_IN)
                    {
                        if (machine.getTank().isEmpty())
                        {
                            LogHelper.info("... This candidate has an empty tank ... Match found.");
                            return machine;
                        }
                        if (machine.getTank().getFluid() != null)
                        {
                            if (machine.getTank().getFluid().isFluidEqual(from.getTank().getFluid()))
                            {
                                LogHelper.info("... This candidate has the same type of fluid ... Match found!");
                                return machine;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static void updateNetwork(BlockPos pos, World world)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te != null)
        {
            if (te instanceof TileEntityPipe || te instanceof TileEntityFluidMachine)
            {
                Set<TileEntityFluidMachine> machines = getNetworkedMachines(pos, world);

                for (TileEntityFluidMachine machine : machines)
                {
                    machine.updateNetworkedMachines();
                }
            }
        }
    }

    /**
     * This method is used to scan a pipe network. It uses BFS (Breadth-first search) to find all nodes and mark them.
     *
     * @param pos  The starting point.
     * @param world The Minecraft world.
     */
    public static Set<TileEntityFluidMachine> getNetworkedMachines(BlockPos pos, World world)
    {
        // This is the set of machines that are found
        Set<TileEntityFluidMachine> machines = new HashSet<TileEntityFluidMachine>();

        // This is the set of pipes we have already visited
        Set<TileEntityPipe> visited = new HashSet<TileEntityPipe>();

        // This is the queue of nodes to traverse
        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

        // Time starting this operation
        long timeStart = System.nanoTime();

        // Counter used for number of "calculations"
        int count = 0;

        // Get the starting point
        TileEntity te = world.getTileEntity(pos);

        // If there is a tile entity here
        if (te != null)
        {
            // Get the connected pipes and add them to the queue
            Set<TileEntityPipe> pipes = getConnectedPipes(pos, world);

            // Add all the pipes to the queue
            queue.addAll(pipes);
        }

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
                for (EnumFacing side : current.getMachineConnections().keySet())
                {
                    // Increase the count
                    count++;

                    // Get the machine in the direction specified
                    TileEntityFluidMachine machine = (TileEntityFluidMachine) world.getTileEntity(current.getPos().offset(side));

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
                for (EnumFacing side : current.getPipeConnections().keySet())
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

        return machines;
    }

    public static Set<TileEntityPipe> getConnectedPipes(BlockPos pos, World world)
    {
        HashMap<EnumFacing, Boolean> pipeMap = new HashMap<EnumFacing, Boolean>();

        Set<TileEntityPipe> pipes = new HashSet<TileEntityPipe>();

        TileEntity te = world.getTileEntity(pos);

        if (te != null)
        {
            if (te instanceof TileEntityFluidMachine)
            {
                pipeMap = ((TileEntityFluidMachine) te).getPipeConnections();
            } else if (te instanceof TileEntityPipe)
            {
                pipeMap = ((TileEntityPipe) te).getPipeConnections();
            }

            for (EnumFacing side : pipeMap.keySet())
            {
                TileEntity te2 = world.getTileEntity(pos.offset(side));

                if (te2 != null)
                {
                    if (te2 instanceof TileEntityPipe)
                    {
                        pipes.add((TileEntityPipe) te2);
                    }
                }
            }
        }

        return pipes;
    }

}
