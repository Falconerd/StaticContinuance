package com.falconerd.staticcontinuance.network;

import com.falconerd.staticcontinuance.network.message.MessageSetFluidMachineMode;
import com.falconerd.staticcontinuance.network.message.MessageSetTankMode;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init()
    {
        INSTANCE.registerMessage(MessageSetTankMode.class, MessageSetTankMode.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFluidMachineMode.class, MessageSetFluidMachineMode.class, 1, Side.CLIENT);
    }
}
