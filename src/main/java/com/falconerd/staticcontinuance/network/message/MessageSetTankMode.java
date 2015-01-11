package com.falconerd.staticcontinuance.network.message;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.utility.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetTankMode implements IMessage, IMessageHandler<MessageSetTankMode, IMessage>
{
    public int x;
    public int y;
    public int z;
    public int mode;

    public MessageSetTankMode()
    {
    }

    public MessageSetTankMode(TileEntityFluidMachine tileEntityTank, int mode)
    {
        this.x = tileEntityTank.getPos().getX();
        this.y = tileEntityTank.getPos().getY();
        this.z = tileEntityTank.getPos().getZ();
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
    public IMessage onMessage(MessageSetTankMode message, MessageContext context)
    {
        TileEntityFluidMachine tileEntity = (TileEntityFluidMachine) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
        if (tileEntity != null)
        {
            tileEntity.setMode(message.mode);
            LogHelper.info("MessageSetTankMode.onMessage: Setting mode on tank at coords" + tileEntity.getPos() + " to: " + message.mode);
        } else
        {
            LogHelper.info("MessageSetTankMode.onMessage: There was a null tileEntity");
        }

        return null;
    }
}
