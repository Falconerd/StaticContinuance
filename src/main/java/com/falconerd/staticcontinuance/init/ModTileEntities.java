package com.falconerd.staticcontinuance.init;

import com.falconerd.staticcontinuance.pipes.TileEntityPipe;
import com.falconerd.staticcontinuance.tileentity.TileEntityTank;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityTank.class, "Tank");
        GameRegistry.registerTileEntity(TileEntityPipe.class, "Pipe");
    }
}
