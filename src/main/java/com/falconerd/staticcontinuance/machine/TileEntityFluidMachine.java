package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.tank.TankSC;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.reference.Reference;
import com.falconerd.staticcontinuance.utility.LogHelper;
import com.falconerd.staticcontinuance.utility.TransportHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class TileEntityFluidMachine extends TileEntityMachine implements IFluidHandler, IUpdatePlayerListBox
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
     * This is a {@link LinkedHashSet} containing all machines in the network ordered from nearest to furthest in
     * relation to this one.
     */
    private Set<TileEntityFluidMachine> networkedMachines = new LinkedHashSet<TileEntityFluidMachine>();

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

    /**
     * This method will update the set of networked machines.
     *
     * @see #networkedMachines
     */
    public void updateNetworkedMachines()
    {
        this.setNetworkedMachines(TransportHelper.getNetworkedMachines(pos, worldObj));
    }

    /**
     * This method will update all connections related to the fluid network
     *
     * @param once If this is false, adjacent entities will also call this method.
     *             TODO: Generalise this and move it to TransportHelper
     */
    public void updateConnections(boolean once)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos.offset(side));

            if (tileEntity instanceof TileEntityPipe)
            {
                pipeConnections.put(side, true);
                if (!once) ((TileEntityPipe) tileEntity).updateConnections(true);
            } else if (tileEntity instanceof TileEntityFluidMachine)
            {
                machineConnections.put(side, true);
            } else
            {
                pipeConnections.remove(side);
                machineConnections.remove(side);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (EnumFacing side : pipeConnections.keySet())
        {
            compound.setBoolean("pipe" + side.getName2(), true);
        }

        for (EnumFacing side : machineConnections.keySet())
        {
            compound.setBoolean("pipe" + side.getName2(), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("pipe" + side.getName2()))
            {
                this.pipeConnections.put(side, true);
            }
        }

        for (EnumFacing side : EnumFacing.values())
        {
            if (compound.getBoolean("pipe" + side.getName2()))
            {
                this.machineConnections.put(side, true);
            }
        }
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
        return new FluidTankInfo[]{this.getTank().getInfo() };
    }

    @Override
    public void update()
    {
        if (needsUpdate)
        {
            worldObj.markBlockForUpdate(pos);
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
                checkTransfer();
            }
        }
    }

    public void checkTransfer()
    {
        if (!worldObj.isRemote)
        {
            if (!this.getTank().isEmpty())
            {
                if (this.getMode() == Reference.MACHINE_MODE_OUT)
                {
                    TransportHelper.transferFluid(this);
                }
            }
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

    public HashMap<EnumFacing, Boolean> getMachineConnections()
    {
        return machineConnections;
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

    public Set<TileEntityFluidMachine> getNetworkedMachines()
    {
        return networkedMachines;
    }

    public void setNetworkedMachines(Set<TileEntityFluidMachine> networkedMachines)
    {
        this.networkedMachines = networkedMachines;
    }

    public void switchMode()
    {
        int mode = this.getMode();

        if (mode++ == Reference.MACHINE_MODE_COUNT) mode = 0;

        this.setMode(mode);

        LogHelper.info("Switching Mode: " + Reference.MACHINE_MODES[mode]);

        //TODO: Work out this stuff
        //PacketHelper.setFluidMachineMode(this, this.mode);
    }
}
