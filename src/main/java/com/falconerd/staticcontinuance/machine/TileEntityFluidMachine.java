package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.tank.TankSC;
import com.falconerd.staticcontinuance.pipes.IPipeInteractor;
import com.falconerd.staticcontinuance.reference.Reference;
import com.falconerd.staticcontinuance.utility.LogHelper;
import com.falconerd.staticcontinuance.utility.TransportHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class TileEntityFluidMachine extends TileEntityMachine implements IFluidHandler, IUpdatePlayerListBox, IPipeInteractor
{
    /**
     * This is a {@link HashMap} containing only the direction of pipes which are present. If a pipe is not present in a
     * direction, the entry is removed.
     */
    private HashMap<EnumFacing, Boolean> pipeConnections = new HashMap<EnumFacing, Boolean>();
    /**
     * This is a {@link HashMap} containing only the direction of machines which are present. If a machine is not present in a
     * direction, the entry is removed.
     */
    private HashMap<EnumFacing, Boolean> machineConnections = new HashMap<EnumFacing, Boolean>();
    /**
     * This is a {@link LinkedHashSet} containing all machine coordinates in the network ordered from nearest to
     * furthest in relation to this one.
     */
    private Set<BlockPos> networkedMachines = new LinkedHashSet<BlockPos>();
    /**
     * This is the mode of the machine.
     * TODO: Implement GUI instead of wrenching mode cycle
     */
    private int mode = 0;
    /**
     * This is the internal tank. It's constructed with an amount of millibuckets. 1 Bucket = 1000mB.
     */
    private TankSC tank = new TankSC(8000);
    /**
     * If this entity needs to be updated.
     */
    private boolean needsUpdate = false;
    /**
     * This is the update delay for this entity. It will wait this many ticks between each update.
     */
    private int updateDelay = ConfigurationHandler.fluidNetworkTickDelay;
    /**
     * This is the timer which is used to keep track of updates.
     */
    int updateTimer = updateDelay;

    public TileEntityFluidMachine(int capacity)
    {
        this.getTank().setCapacity(capacity);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        this.writeCustomNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.readCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        // Load the connections

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("pipe" + side.getName2()))
            {
                this.pipeConnections.put(side, true);
            }
        }

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("machine" + side.getName2()))
            {
                this.machineConnections.put(side, true);
            }
        }

        // Load the mode

        this.setMode(compound.getInteger("mode"));

        // Load the fluid

        if (compound.getBoolean("hasFluid"))
        {
            this.getTank().setFluid(FluidRegistry.getFluidStack(compound.getString("fluidName"), compound.getInteger("amount")));
        }

        // Load the fluid network

        NBTTagList networkedMachineList = compound.getTagList("networkedMachineList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < networkedMachineList.tagCount(); i++)
        {
            NBTTagCompound tag = networkedMachineList.getCompoundTagAt(i);
            int[] position = tag.getIntArray("machine" + i);
            this.getNetworkedMachines().add(new BlockPos(position[0], position[1], position[2]));
        }
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        // Save the connections

        for (EnumFacing side : pipeConnections.keySet())
        {
            compound.setBoolean("pipe" + side.getName2(), true);
        }

        for (EnumFacing side : machineConnections.keySet())
        {
            compound.setBoolean("machine" + side.getName2(), true);
        }

        // Save the tank mode

        compound.setInteger("mode", this.getMode());

        // Save the tank fluid

        FluidStack fluidStack = this.getTank().getFluid();
        compound.setBoolean("hasFluid", fluidStack != null);
        if (fluidStack != null)
        {
            compound.setString("fluidName", fluidStack.getFluid().getName());
            compound.setInteger("amount", fluidStack.amount);
        }

        // Save the fluid network

        NBTTagList networkedMachineList = new NBTTagList();

        int index = 0;

        for (BlockPos position : this.getNetworkedMachines())
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setIntArray("machine" + index, new int[]{position.getX(), position.getY(), position.getZ()});
            networkedMachineList.appendTag(tag);

            index++;
        }

        compound.setTag("networkedMachineList", networkedMachineList);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeCustomNBT(tag);
        return new S35PacketUpdateTileEntity(this.getPos(), 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readCustomNBT(pkt.getNbtCompound());
        this.getWorld().markBlockForUpdate(this.getPos());
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        this.needsUpdate = true;
        return this.getTank().fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return this.getTank().drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        this.needsUpdate = true;
        return this.getTank().drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return this.getMode() == Reference.MACHINE_MODE_IN;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return this.getMode() == Reference.MACHINE_MODE_OUT;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[]{this.getTank().getInfo()};
    }

    @Override
    public void update()
    {
        if (needsUpdate)
        {
            this.getWorld().markBlockForUpdate(getPos());
            needsUpdate = false;
        }
        if (updateTimer == 0)
        {
            updateTimer = updateDelay;
        } else
        {
            --updateTimer;
            if (updateTimer == 0)
            {
                if (!getWorld().isRemote)
                {
                    checkTransfer();
                }
            }
        }
    }

    public void checkTransfer()
    {
        if (!this.getNetworkedMachines().isEmpty() && this.getMode() == Reference.MACHINE_MODE_OUT && !this.getTank().isEmpty())
        {
            TransportHelper.transferLiquid(getPos(), getWorld());
        }
    }

    public double getFluidRatio()
    {
        return (double) getTank().getFluidAmount() / (double) getTank().getCapacity();
    }

    public HashMap<EnumFacing, Boolean> getPipeConnections()
    {
        return pipeConnections;
    }

    @Override
    public void setPipeConnections(HashMap<EnumFacing, Boolean> pipeConnections)
    {
        this.pipeConnections = pipeConnections;
    }

    public HashMap<EnumFacing, Boolean> getMachineConnections()
    {
        return machineConnections;
    }

    @Override
    public void setMachineConnections(HashMap<EnumFacing, Boolean> machineConnections)
    {
        this.machineConnections = machineConnections;
    }

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public TankSC getTank()
    {
        return tank;
    }

    public int getUpdateDelay()
    {
        return updateDelay;
    }

    public void setUpdateDelay(int updateDelay)
    {
        this.updateDelay = updateDelay;
    }

    public boolean getNeedsUpdate(boolean needsUpdate)
    {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate)
    {
        this.needsUpdate = needsUpdate;
    }

    public Set<BlockPos> getNetworkedMachines()
    {
        return networkedMachines;
    }

    public void setNetworkedMachines(Set<BlockPos> networkedMachines)
    {
        if (networkedMachines.contains(this.getPos())) networkedMachines.remove(this.getPos());
        this.networkedMachines = networkedMachines;
    }

    public void switchMode()
    {
        int mode = this.getMode();

        if (mode++ == Reference.MACHINE_MODE_COUNT) mode = 0;

        this.setMode(mode);

        LogHelper.info("Switching Mode: " + Reference.MACHINE_MODES[mode]);
    }
}