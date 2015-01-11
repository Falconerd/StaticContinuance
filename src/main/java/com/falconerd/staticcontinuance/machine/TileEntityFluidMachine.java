package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.machine.tank.FluidTankSC;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.TreeMap;

public class TileEntityFluidMachine extends TileEntityMachine implements IFluidHandler, IUpdatePlayerListBox
{
    /**
     * This map holds a list of all machines in the network and their relative priority to this machine
     */
    public TreeMap<Integer, TileEntityFluidMachine> networkedMachines = new TreeMap<Integer, TileEntityFluidMachine>();

    /**
     * Mode which this machine is set to. For now we'll just have in/out/disabled.
     */
    public int mode = Reference.MACHINE_MODE_IN;

    /**
     * The tank allocated to this machine.
     */
    public FluidTankSC tank = new FluidTankSC(16000);

    /**
     * If this is set to true, the block at this position will be marked for update
     */
    public boolean needsUpdate = false;

    /**
     * The update rate for the fluid network is by default 20. This means the game will wait 20 ticks before updating
     * again.
     */
    private int updateRate = ConfigurationHandler.fluidNetworkTickRate;

    /**
     * The update timer. When this hits {@link updateRate}, the tile entity will fire {@link doUpdate}.
     */
    private int updateTimer = 0;

    public void switchMode()
    {
        this.mode++;
        if (this.mode > Reference.MACHINE_MODE_COUNT) this.mode = 0;
    }

    /**
     * Setter for this.mode
     *
     * @param mode The mode to set to. Represented by an integer
     */
    public void setMode(int mode)
    {
        this.mode = mode;
    }

    /**
     * This method is called when {@link needsUpdate} is true.
     * <s>This method is called every {@link updateRate} ticks. Put most if not all logic in here.</s>
     */
    void doUpdate()
    {
    }

    /**
     * This method is called every tick. It checks if this tile entity needs to be updated. if so, it will call
     * doUpdate().
     */
    @Override
    public void update()
    {
        if (needsUpdate)
        {
            doUpdate();
            worldObj.markBlockForUpdate(pos);
            needsUpdate = false;
        }
//        if (updateTimer == updateRate)
//        {
//            updateTimer = 0;
//        }
//        else
//        {
//            updateTimer++;
//            if (updateTimer == updateRate)
//            {
//                doUpdate();
//            }
//        }
    }

    /**
     * This method gets the fluid ratio of the tank. Useful for GUI and rendering fluids.
     *
     * @return The ratio
     */
    public double getFluidRatio()
    {
        return (double) tank.getFluidAmount() / (double) tank.getCapacity();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[0];
    }
}
