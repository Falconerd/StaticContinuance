package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.network.PacketHandler;
import com.falconerd.staticcontinuance.network.message.MessageSetTankMode;
import com.falconerd.staticcontinuance.network.message.MessageSyncTileEntitySC;
import com.falconerd.staticcontinuance.network.message.MessageTransferFluid;
import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketHelper
{
    public static void setTankMode(TileEntityFluidMachine tank, int mode)
    {
        PacketHandler.INSTANCE.sendToServer(new MessageSetTankMode(tank, mode));
    }

    public static void syncTile(IMessage message)
    {
        PacketHandler.INSTANCE.sendToAll(message);
    }

    public static void syncTile(TileEntitySC tileEntitySC)
    {
        syncTile(new MessageSyncTileEntitySC(tileEntitySC));
    }

    public static void transferFluid(BlockPos from, BlockPos to, int amount)
    {
        PacketHandler.INSTANCE.sendToAll(new MessageTransferFluid(from, to, amount));
    }
}
