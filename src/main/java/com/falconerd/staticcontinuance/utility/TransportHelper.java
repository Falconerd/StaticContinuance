package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.machine.TileEntityMachine;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

/**
 * This class helps manage the fluid transport system in Static Continunace
 * TODO: create methods to manage network's valid inputs and outputs so that they don't have to be searched for unless the network changes
 */
public class TransportHelper
{
    public static void updateNetwork(TileEntity from)
    {
        if (from instanceof TileEntityPipe || from instanceof TileEntityFluidMachine)
        {
            // Get all of the machines in the network
            List<TileEntityFluidMachine> machines = getAllMachinesInNetwork(from);

            // Iterate through the machines and assign each their own map of the others
            for (TileEntityFluidMachine machine : machines)
            {
                machine.networkedFluidMachines = getMachineMap(machine);
            }
        } else
        {
            LogHelper.warn("The TileEntity at position " + from.getPos() + " should not be trying to update a fluid network.");
        }
    }

    private static List<TileEntityFluidMachine> getAllMachinesInNetwork(TileEntity from)
    {
        List<TileEntityFluidMachine> result = new ArrayList<TileEntityFluidMachine>();

        HashMap<TileEntityFluidMachine, Boolean> found = new HashMap<TileEntityFluidMachine, Boolean>();

        Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

        HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

        if (isConnectedToPipe(from))
        {
            for (TileEntityPipe pipe : getConnectedPipes(from))
            {
                queue.add(pipe);
            }

            while (!queue.isEmpty())
            {
                TileEntityPipe current = queue.poll();

                visited.put(current, true);

                //if (isConnectedToMachine(current))
                {
                    for (TileEntityFluidMachine machine : getConnectedMachines(current))
                    {
                        if (!found.containsKey(machine))
                        {
                            found.put(machine, true);
                        }
                    }
                }

                //if (isConnectedToPipe(current))
                {
                    for (TileEntityPipe next : getConnectedPipes(current))
                    {
                        if (!visited.containsKey(next))
                        {
                            queue.add(next);
                        }
                    }
                }
            }
        }

        for (TileEntityFluidMachine machine : found.keySet())
        {
            result.add(machine);
        }

        return result;
    }

    public static TreeMap<Integer, TileEntityFluidMachine> getMachineMap(TileEntityFluidMachine from)
    {
        TreeMap<Integer, TileEntityFluidMachine> machineMap = new TreeMap<Integer, TileEntityFluidMachine>();

        // Make sure we actually have a network to traverse
        if (isConnectedToPipe(from))
        {
            // Create a queue to iterate
            Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

            // Keep track of places we've been
            HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

            // Create an integer to represent distance
            int distance = 0;

            // Loop through all connected pipes
            for (TileEntityPipe pipe : getConnectedPipes(from))
            {
                // Add them to the queue
                queue.add(pipe);
            }

            while (!queue.isEmpty())
            {
                // Remove the first pipe and store it as current
                TileEntityPipe current = queue.poll();

                // Add the current pipe to the visited locations
                visited.put(current, true);

                // Increase the distance
                distance++;

                // Check if we are connected to a machine
                if (isConnectedToMachine(current))
                {
                    for (TileEntityMachine machine : getConnectedMachines(current))
                    {
                        TileEntityFluidMachine fluidMachine = (TileEntityFluidMachine) machine;

                        if (fluidMachine != null)
                        {
                            if (!machineMap.containsValue(fluidMachine))
                            {
                                if (fluidMachine != from)
                                {
                                    machineMap.put(distance, fluidMachine);
                                }
                            }
                        }

                    }
                }

                // Add more pipes to the queue if we are connected to some aand haven't been there
                for (TileEntityPipe pipe : getConnectedPipes(current))
                {
                    if (!visited.containsKey(pipe))
                    {
                        queue.add(pipe);
                    }
                }
            }
        }

        return machineMap;
    }

    /**
     * This method transfers fluids from an output into a valid input on the same network
     * @param from The {@link TileEntityFluidMachine} to transfer from
     */
    public static void transferFluid(TileEntityFluidMachine from)
    {
        if (from.networkedFluidMachines.isEmpty())
        {
            LogHelper.info("TransportHelper.transferFluid: This machine has no valid outputs on the network.");
        } else
        {
            // Get the closest networked fluid acceptor which fits the criteria
            TileEntityFluidMachine to = from.networkedFluidMachines.firstEntry().getValue();

            LogHelper.info("Closest fluid machine: " + to + " | Fluid: " + to.tank.getFluid() + " | Capacity: " + to.tank.getCapacity());

            // Load the transfer amount from config
            int transferAmount = ConfigurationHandler.pipeFluidTransferRate * (20 / ConfigurationHandler.fluidNetworkTickRate);

            // This much space left in the tank
            int availableTo = to.tank.getCapacityRemaining();

            // This much fluid in this tank
            int availabeFrom = from.tank.getFluidAmount();

            // Set the transfer amount to the amount left in the tank
            if (availabeFrom < transferAmount) transferAmount = availabeFrom;

            FluidStack fluidStack = from.tank.drain(transferAmount, true);

            if (fluidStack != null)
            {
                LogHelper.info("FluidStack: " + fluidStack.getFluid().getUnlocalizedName() + " | " + fluidStack.amount);

                to.tank.fill(fluidStack, true);

                to.needsUpdate = true;
                from.needsUpdate = true;
            }
        }
    }

    /**
     * This method will return the nearest non-full fluid accepting machine
     *
     * @param from The block to start the search from
     * @return The first block which meets the criteria
     */
    public static TileEntityFluidMachine getNearestFluidAcceptor(TileEntityFluidMachine from)
    {
        // Get the fluid type we are searching for
        FluidStack fluidStack = from.tank.getFluid();

        // If this machine has fluid
        if (fluidStack != null)
        {
            // Create a queue of pipes to search
            Queue<TileEntityPipe> queue = new LinkedList<TileEntityPipe>();

            List<TileEntityPipe> visited2 = new ArrayList<TileEntityPipe>();

            // Created a list of visited places
            HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();

            // Check if we are connected to any pipes
            if (isConnectedToPipe(from))
            {
                // Add the connected pipes as places to be  searched
                for (TileEntityPipe pipe : getConnectedPipes(from))
                {
                    queue.add(pipe);
                }

                // While we have places to search
                while (!queue.isEmpty())
                {
                    // Grab the next place to search and remove it from the queue
                    TileEntityPipe current = queue.poll();

                    // Add it to the list of visited places
                    visited.put(current, true);

                    // We are connected to a machine
                    if (isConnectedToMachine(current))
                    {
                        // Find the first fluid handling machine which is not full and is using the same fluid
                        for (TileEntityMachine machine : getConnectedMachines(current))
                        {
                            // If the machine is not the starting machine
                            if (!machine.equals(from))
                            {
                                // If the machine accepts fluids
                                if (machine instanceof TileEntityFluidMachine)
                                {
                                    TileEntityFluidMachine fluidMachine = (TileEntityFluidMachine) machine;

                                    if (fluidMachine.canFill(EnumFacing.UP, fluidStack.getFluid()))
                                    {
                                        // If the machine is empty
                                        if (fluidMachine.tank.isEmpty())
                                        {
                                            return fluidMachine;
                                        } else
                                        {
                                            // If the machine has the same fluid type
                                            if (fluidMachine.tank.getFluid().isFluidEqual(fluidStack))
                                            {
                                                // If the machine is not full
                                                if (fluidMachine.tank.getFluidAmount() < fluidMachine.tank.getCapacity())
                                                {
                                                    return fluidMachine;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Search for pipes connected to this one
                    for (TileEntityPipe next : getConnectedPipes(current))
                    {
                        // If we haven't been there
                        if (!visited.containsKey(next))
                        {
                            // Add it to the queue
                            queue.add(next);
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * This method is used to get all of the pipes connected to this
     *
     * @param tileEntity The tile entity we're checking
     * @return returns a {@link List} of connected pipes
     */
    public static List<TileEntityPipe> getConnectedPipes(TileEntity tileEntity)
    {
        List<TileEntityPipe> pipes = new ArrayList<TileEntityPipe>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity te = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));

            if (te != null)
            {
                if (te instanceof TileEntityPipe)
                {
                    pipes.add((TileEntityPipe) te);
                }
            }
        }

        return pipes;
    }

    /**
     * This method will return a list of machines connected to this tileEntity
     *
     * @param tileEntity The tile entity to check
     * @return returns a {@link List} of connected pipes
     */
    public static List<TileEntityFluidMachine> getConnectedMachines(TileEntity tileEntity)
    {
        List<TileEntityFluidMachine> machines = new ArrayList<TileEntityFluidMachine>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity te = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));

            if (te != null)
            {
                if (te instanceof TileEntityMachine)
                {
                    machines.add((TileEntityFluidMachine) te);
                }
            }
        }

        return machines;
    }

    /**
     * This method is used to check whether a tileEntity has a connected pipe
     *
     * @param tileEntity The tile entity we are checking
     * @return boolean
     */
    public static boolean isConnectedToPipe(TileEntity tileEntity)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity te = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));
            if (te != null)
            {
                if (te instanceof TileEntityPipe)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isConnectedToMachine(TileEntityPipe tileEntityPipe)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity te = tileEntityPipe.getWorld().getTileEntity(tileEntityPipe.getPos().offset(side));
            if (te != null)
            {
                if (te instanceof TileEntityMachine)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
