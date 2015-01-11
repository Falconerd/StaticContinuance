package com.falconerd.staticcontinuance.handler;

import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationHandler
{
    public static Configuration configuration;
    public static boolean testValue = false;
    public static int pipeFluidTransferRate = 2000;
    public static int fluidNetworkTickRate = 1;

    public static void init(File configFile)
    {
        if (configuration == null)
        {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        testValue = configuration.getBoolean("configValue", Configuration.CATEGORY_GENERAL, false, "This is an example configuration value.");
        pipeFluidTransferRate = configuration.getInt("pipeFluidTransferRate", Configuration.CATEGORY_GENERAL, 2000, 0, 10000, "mB/tick");
        fluidNetworkTickRate = configuration.getInt("fluidNetworkTickRate", Configuration.CATEGORY_GENERAL, 1, 1, 20, "How many ticks per second the fluid network will be updated.");

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID))
        {
            loadConfiguration();
        }
    }
}
