package com.falconerd.staticcontinuance.network.message;

import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncTileEntitySC implements IMessage, IMessageHandler<MessageSyncTileEntitySC, IMessage>
{
    public NBTTagCompound data;
    public int x;
    public int y;
    public int z;

    public MessageSyncTileEntitySC()
    {
    }

    public MessageSyncTileEntitySC(TileEntitySC tileEntitySC)
    {
        data = new NBTTagCompound();
        tileEntitySC.writeToNBT(data);

        this.x = tileEntitySC.getPos().getX();
        this.y = tileEntitySC.getPos().getY();
        this.z = tileEntitySC.getPos().getZ();
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        data = ByteBufUtils.readTag(buffer);
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, data);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public IMessage onMessage(MessageSyncTileEntitySC message, MessageContext ctx)
    {
        TileEntitySC tileEntitySC = (TileEntitySC) FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(x, y, z));

        if (tileEntitySC != null)
        {
            tileEntitySC.readFromNBT(message.data);
        }
        return null;
    }
}
