package com.falconerd.staticcontinuance.creativetab;

import com.falconerd.staticcontinuance.init.ModItems;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabSC
{
    public static final CreativeTabs SC_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase())
    {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.copperGear;
        }
    };
}
