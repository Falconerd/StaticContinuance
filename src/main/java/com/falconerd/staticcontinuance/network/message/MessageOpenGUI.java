package com.falconerd.staticcontinuance.network.message;

import com.falconerd.staticcontinuance.StaticContinuance;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenGUI implements IMessage, IMessageHandler<MessageOpenGUI, IMessage>
{
    public int gui;

    public MessageOpenGUI(int gui)
    {
        this.gui = gui;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.gui = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.gui);
    }

    @Override
    public IMessage onMessage(MessageOpenGUI message, MessageContext ctx)
    {
        EntityPlayer player = ctx.getServerHandler().playerEntity;

        if (player != null)
        {
            BlockPos pos = player.getPosition();

            player.openGui(StaticContinuance.instance, message.gui, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
        }

        return null;
    }
}
