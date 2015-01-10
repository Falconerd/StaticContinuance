package com.falconerd.staticcontinuance.proxy;

import com.falconerd.staticcontinuance.machine.tank.RenderTank;
import com.falconerd.staticcontinuance.machine.tank.TileEntityTank;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    public static void registerProxies()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new RenderTank());
    }
}
