package com.falconerd.staticcontinuance.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler
{
    public static final int GUI_ID_BOILER = 1;

    protected final Map<Integer, IGuiHandler> guiHandlers = new HashMap<Integer, IGuiHandler>();

    public void registerGuiHandler(int id, IGuiHandler handler)
    {
        guiHandlers.put(id, handler);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        IGuiHandler handler = guiHandlers.get(ID);
        if (handler != null) return handler.getServerGuiElement(ID, player, world, x, y, z);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        IGuiHandler handler = guiHandlers.get(ID);
        if (handler != null) return handler.getClientGuiElement(ID, player, world, x, y, z);
        return null;
    }
}
