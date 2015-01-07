package com.falconerd.staticcontinuance.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaried
{
    public static void init()
    {
        OreDictionary.registerOre("ingotCopper", new ItemStack(ModItems.copperIngot));
        OreDictionary.registerOre("oreCopper", new ItemStack(ModBlocks.copperOre));
    }
}
