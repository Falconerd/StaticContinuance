package com.falconerd.staticcontinuance.network.message;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.utility.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetFluidMachineMode implements IMessage, IMessageHandler<MessageSetFluidMachineMode, IMessage>
{
    public int x;
    public int y;
    public int z;
    public int mode;

    public MessageSetFluidMachineMode()
    {
    }

    public MessageSetFluidMachineMode(TileEntityFluidMachine tileEntityFluidMachine, int mode)
    {
        this.x = tileEntityFluidMachine.getPos().getX();
        this.y = tileEntityFluidMachine.getPos().getY();
        this.z = tileEntityFluidMachine.getPos().getZ();
        this.mode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.mode = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(mode);
    }

    @Override
    public IMessage onMessage(MessageSetFluidMachineMode message, MessageContext context)
    {
        TileEntityFluidMachine tileEntity = (TileEntityFluidMachine) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
        if (tileEntity != null)
        {
            //TODO:tileEntity.setMode(message.mode);
            LogHelper.info("MessageSetTankMode.onMessage: Setting mode on tank at coords" + tileEntity.getPos() + " to: " + message.mode);
        } else
        {
            LogHelper.info("MessageSetTankMode.onMessage: There was a null tileEntity");
        }

        return null;
    }
}
