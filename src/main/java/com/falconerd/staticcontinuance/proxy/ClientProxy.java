package com.falconerd.staticcontinuance.proxy;

import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.pipes.TileEntityRenderPipe;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    public static void registerProxies()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipe.class, new TileEntityRenderPipe());
    }
}
