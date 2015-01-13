package com.falconerd.staticcontinuance.network.message;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.utility.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateFluidMachines implements IMessage, IMessageHandler<MessageUpdateFluidMachines, IMessage>
{
    public int fromX;
    public int fromY;
    public int fromZ;
    public int toX;
    public int toY;
    public int toZ;
    public int amount;

    public MessageUpdateFluidMachines()
    {
    }

    public MessageUpdateFluidMachines(BlockPos from, BlockPos to, int amount)
    {
        this.fromX = from.getX();
        this.fromY = from.getY();
        this.fromZ = from.getZ();
        this.toX = to.getX();
        this.toY = to.getY();
        this.toZ = to.getZ();
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        this.fromX = buffer.readInt();
        this.fromY = buffer.readInt();
        this.fromZ = buffer.readInt();
        this.toX = buffer.readInt();
        this.toY = buffer.readInt();
        this.toZ = buffer.readInt();
        this.amount = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(fromX);
        buffer.writeInt(fromY);
        buffer.writeInt(fromZ);
        buffer.writeInt(toX);
        buffer.writeInt(toY);
        buffer.writeInt(toZ);
        buffer.writeInt(amount);
    }

    @Override
    public IMessage onMessage(MessageUpdateFluidMachines message, MessageContext context)
    {
        World world = FMLClientHandler.instance().getClient().theWorld;

        TileEntityFluidMachine from = (TileEntityFluidMachine) world.getTileEntity(new BlockPos(message.fromX, message.fromY, message.fromZ));
        TileEntityFluidMachine to = (TileEntityFluidMachine) world.getTileEntity(new BlockPos(message.toX, message.toY, message.toZ));

        if (from != null && to != null)
        {
            FluidStack fluidStack = from.getTank().drain(message.amount, true);
            to.getTank().fill(fluidStack, true);
            LogHelper.info("TRANSFERRED!");
        }

        return null;
    }
}
