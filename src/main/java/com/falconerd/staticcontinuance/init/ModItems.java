package com.falconerd.staticcontinuance.init;

import com.falconerd.staticcontinuance.item.*;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems
{
    public static final ItemSC copperIngot = new ItemCopperIngot();
    public static final ItemSC copperGear = new ItemCopperGear();
    public static final ItemSC zincIngot = new ItemZincIngot();
    public static final ItemSC brassIngot = new ItemBrassIngot();
    public static final ItemSC brassGear = new ItemBrassGear();

    public static void init()
    {
        GameRegistry.registerItem(copperIngot, "copperIngot");
        GameRegistry.registerItem(copperGear, "copperGear");
        GameRegistry.registerItem(zincIngot, "zincIngot");
        GameRegistry.registerItem(brassIngot, "brassIngot");
        GameRegistry.registerItem(brassGear, "brassGear");
    }

    public static void registerModels()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(copperIngot, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "copperIngot", "inventory"));
    }
}
