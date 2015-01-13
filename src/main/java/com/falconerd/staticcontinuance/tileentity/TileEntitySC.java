package com.falconerd.staticcontinuance.tileentity;

import com.falconerd.staticcontinuance.network.PacketHandler;
import com.falconerd.staticcontinuance.network.message.MessageSyncTileEntitySC;
import com.falconerd.staticcontinuance.utility.PacketHelper;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySC extends TileEntity
{
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.INSTANCE.getPacketFrom(new MessageSyncTileEntitySC(this));
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    public void sync()
    {
        PacketHelper.syncTile(this);
    }
}
