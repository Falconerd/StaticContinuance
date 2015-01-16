package com.falconerd.staticcontinuance;

import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.handler.GuiHandler;
import com.falconerd.staticcontinuance.init.ModBlocks;
import com.falconerd.staticcontinuance.init.ModFluids;
import com.falconerd.staticcontinuance.init.ModItems;
import com.falconerd.staticcontinuance.init.ModTileEntities;
import com.falconerd.staticcontinuance.init.OreDictionaried;
import com.falconerd.staticcontinuance.init.Recipes;
import com.falconerd.staticcontinuance.network.PacketHandler;
import com.falconerd.staticcontinuance.proxy.ClientProxy;
import com.falconerd.staticcontinuance.proxy.IProxy;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class StaticContinuance
{
    @Mod.Instance
    public static StaticContinuance instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static SimpleNetworkWrapper simpleNetworkWrapper;

    public static GuiHandler guiHandler = new GuiHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

        PacketHandler.init();

        ModFluids.init();

        ModItems.init();
        ModBlocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        ModTileEntities.init();
        ModItems.registerModels();
        ModBlocks.registerModels();
        OreDictionaried.init();
        Recipes.init();

        ClientProxy.registerProxies();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
