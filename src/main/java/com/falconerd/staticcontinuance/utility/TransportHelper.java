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
 */
public class TransportHelper
{
    public static void transferFluid(TileEntityFluidMachine from)
    {
        // Find the nearest fluid acceptor which isn't empty and contains the same type of fluid as this
        TileEntityFluidMachine to = getNearestFluidAcceptor(from);

        if (to != null)
        {
            // Load the transfer amount from config
            int transferAmount = ConfigurationHandler.pipeFluidTransferRate * (20 / ConfigurationHandler.fluidNetworkTickRate);

            // Find out how much fluid we have in to
            int availableTo = to.tank.getCapacity();

            // Find out how much fluid we have in from
            int availableFrom = from.tank.getCapacity();

            // Find out how much liquid we can transfer
            int requiredCapacity = transferAmount > availableFrom ? availableFrom : transferAmount;

            // If we have less space than we need, transfer only the amount we can fit
            if (availableTo < requiredCapacity)
            {
                requiredCapacity = availableTo;
            }

            LogHelper.info(requiredCapacity);

            FluidStack fluidStack = from.tank.drain(requiredCapacity, true);

            to.tank.fill(fluidStack, true);
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
    public static List<TileEntityMachine> getConnectedMachines(TileEntity tileEntity)
    {
        List<TileEntityMachine> machines = new ArrayList<TileEntityMachine>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity te = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));

            if (te != null)
            {
                if (te instanceof TileEntityMachine)
                {
                    machines.add((TileEntityMachine) te);
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
