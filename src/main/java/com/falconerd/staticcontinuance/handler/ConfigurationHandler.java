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
    public static int fluidNetworkTickDelay = 20;

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
        fluidNetworkTickDelay = configuration.getInt("fluidNetworkTickDelay", Configuration.CATEGORY_GENERAL, 20, 1, 100, "How many ticks to wait between updates. Increase this number for less updates.");

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