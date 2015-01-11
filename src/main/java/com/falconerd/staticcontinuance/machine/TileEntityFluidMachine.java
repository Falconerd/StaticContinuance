package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.machine.tank.FluidTankSC;
import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.reference.Reference;
import com.falconerd.staticcontinuance.utility.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.TreeMap;

public class TileEntityFluidMachine extends TileEntityMachine implements IFluidHandler, IUpdatePlayerListBox
{
    /**
     * Map of machines which are on the same network as this one, with an integer representing their distance and hence
     * priority. We only want to update this when the network is changed in some way.
     */
    public TreeMap<Integer, TileEntityFluidMachine> networkedFluidMachines = new TreeMap<Integer, TileEntityFluidMachine>();
    public int mode = 0;
    public FluidTankSC tank = new FluidTankSC(16000);
    public boolean needsUpdate = false;
    /**
     * The amount of ticks in between each update. 20 ticks = ~1 second dependant on lag
     */
    private int updateRate = 20;
    /**
     * The timer which keeps track of the updates
     */
    private int updateTimer = updateRate;

    public void switchMode()
    {
        this.mode++;
        if (this.mode > Reference.TANK_MODES.length - 1) this.mode = 0;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Mode switched to: " + Reference.TANK_MODES[this.mode]));
        PacketHelper.setTankMode(this, this.mode);
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        this.needsUpdate = true;
        return this.tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        this.needsUpdate = true;
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        // TODO: Make this check fluid type
        return this.mode == 0;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return this.mode == 1;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    /**
     * This method is called every tick. Use doUpdate()
     */
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
            updateTimer = updateRate;
        } else
        {
            --updateTimer;
            if (updateTimer == 0)
            {
                doUpdate();
            }
        }
    }

    public void doUpdate()
    {
    }

    public void updateNetwork()
    {
        for (EnumFacing side : EnumFacing.values())
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos.offset(side));
            if (tileEntity instanceof TileEntityPipe)
            {
                ((TileEntityPipe) tileEntity).updateConnections(true);
            }
        }
    }
}
