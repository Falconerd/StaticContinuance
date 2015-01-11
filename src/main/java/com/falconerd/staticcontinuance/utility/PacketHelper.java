package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;
import com.falconerd.staticcontinuance.network.PacketHandler;
import com.falconerd.staticcontinuance.network.message.MessageSetTankMode;
import com.falconerd.staticcontinuance.network.message.MessageSyncTileEntitySC;
import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
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
}
