package com.falconerd.staticcontinuance.block;

import com.falconerd.staticcontinuance.creativetab.CreativeTabSC;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

/**
 * Use this class for machines and other blocks which need TileEntity functionality.
 */
public abstract class BlockContainerSC extends BlockContainer
{
    public BlockContainerSC(Material material)
    {
        super(material);
        this.setCreativeTab(CreativeTabSC.SC_TAB);
    }

    public BlockContainerSC()
    {
        this(Material.iron);
    }

    protected void init()
    {
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedLocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedLocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
