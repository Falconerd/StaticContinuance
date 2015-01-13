package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.pipes.IPipeInteractor;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * This class helps manage the fluid transport system in Static Continuance.
 * I want everything to run on the server side and then send packets to the client side for updates.
 */
public class TransportHelper
{
    /**
     * This method will find all machines on the network and remap them.
     * @param pos
     * @param world
     */
    public static void mapNetwork(BlockPos pos, World world)
    {
        long timeStart = System.nanoTime();

        Set<BlockPos> machines = findMachines(pos, world);

        for (BlockPos machine : machines)
        {
            TileEntity tileEntity = world.getTileEntity(machine);

            ((TileEntityFluidMachine) tileEntity).setNetworkedMachines(findMachines(machine, world));
        }

        long timeEnd = System.nanoTime();

        long timeTaken = timeEnd - timeStart;

        LogHelper.info("Machines: " + machines.size() + " | Time: " + timeTaken + " nanoseconds");
    }

    public static Set<BlockPos> findMachines(BlockPos pos, World world)
    {
        Set<BlockPos> machines = new LinkedHashSet<BlockPos>();

        Set<BlockPos> visited = new HashSet<BlockPos>();

        Queue<BlockPos> queue = new LinkedList<BlockPos>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity1 = world.getTileEntity(pos.offset(side));

            if (tileEntity1 != null)
            {
                if (tileEntity1 instanceof TileEntityPipe) queue.add(tileEntity1.getPos());
            }
        }

        while (!queue.isEmpty())
        {
            BlockPos current = queue.poll();

            if (!visited.contains(current))
            {
                visited.add(current);

                TileEntity currentTE = world.getTileEntity(current);

                if (currentTE != null)
                {
                    if (currentTE instanceof TileEntityPipe)
                    {
                        for (EnumFacing side : ((TileEntityPipe) currentTE).getMachineConnections().keySet())
                        {
                            if (!machines.contains(current.offset(side)))
                            {
                                TileEntity currentConnectedTE = world.getTileEntity(current.offset(side));
                                if (currentConnectedTE != null) machines.add(currentConnectedTE.getPos());
                            }
                        }

                        for (EnumFacing side : ((TileEntityPipe) currentTE).getPipeConnections().keySet())
                        {
                            if (!visited.contains(current.offset(side)))
                            {
                                TileEntity currentConnectedTE = world.getTileEntity(current.offset(side));
                                if (currentConnectedTE != null) queue.add(currentConnectedTE.getPos());
                            }
                        }
                    }
                }
            }
        }

        return machines;
    }


    public static void mapNode(BlockPos pos, World world, Boolean cascade)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        HashMap<EnumFacing, Boolean> machineConnections = new HashMap<EnumFacing, Boolean>();
        HashMap<EnumFacing, Boolean> pipeConnections = new HashMap<EnumFacing, Boolean>();

        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity1 = world.getTileEntity(pos.offset(side));

            if (tileEntity1 != null)
            {
                if (tileEntity1 instanceof IPipeInteractor)
                {
                    if (tileEntity1 instanceof TileEntityFluidMachine) machineConnections.put(side, true);
                    if (tileEntity1 instanceof TileEntityPipe) pipeConnections.put(side, true);

                    if (cascade) TransportHelper.mapNode(tileEntity1.getPos(), world, false);
                }
            }
        }

        if (tileEntity != null)
        {
            if (tileEntity instanceof TileEntityFluidMachine)
            {
                ((TileEntityFluidMachine) tileEntity).setMachineConnections(machineConnections);
                ((TileEntityFluidMachine) tileEntity).setPipeConnections(pipeConnections);
            }
            if (tileEntity instanceof TileEntityPipe)
            {
                ((TileEntityPipe) tileEntity).setMachineConnections(machineConnections);
                ((TileEntityPipe) tileEntity).setPipeConnections(pipeConnections);
            }
        }

        mapNetwork(pos, world);
    }

    public static BlockPos getNearestFluidAcceptor(BlockPos pos, World world)
    {
        TileEntityFluidMachine source = (TileEntityFluidMachine) world.getTileEntity(pos);

        for (BlockPos machine : source.getNetworkedMachines())
        {
            TileEntityFluidMachine fluidMachine = (TileEntityFluidMachine) world.getTileEntity(machine);

            if (fluidMachine.getMode() == Reference.MACHINE_MODE_IN)
            {
                if (fluidMachine.getTank().isEmpty()) return machine;
                if (fluidMachine.getTank().getFluid().isFluidEqual(source.getTank().getFluid()))
                {
                    if (fluidMachine.getTank().getRemainingCapacity() > 0)
                    {
                        return machine;
                    }
                }
            }
        }

        return null;
    }

    public static void transferLiquid(BlockPos pos, World world)
    {
        BlockPos destination = getNearestFluidAcceptor(pos, world);

        LogHelper.info("Nearest Acceptor is: " + destination);

        if (destination != null)
        {
            TileEntityFluidMachine to = (TileEntityFluidMachine) world.getTileEntity(destination);
            TileEntityFluidMachine from = (TileEntityFluidMachine) world.getTileEntity(pos);

            int transferAmount = ConfigurationHandler.pipeFluidTransferRate * (20 / ConfigurationHandler.fluidNetworkTickDelay);

            int capacity = to.getTank().getRemainingCapacity();
            int fluidAmount = from.getTank().getFluidAmount();

            int required = transferAmount > fluidAmount ? fluidAmount : transferAmount;

            if (capacity < required) required = capacity;

            FluidStack fluidStack = from.getTank().drain(required, true);
            to.getTank().fill(fluidStack, true);

            // Send the packet to tell the client we have moved things
            PacketHelper.updateFluidMachines(pos, destination, required);
        }
    }
}