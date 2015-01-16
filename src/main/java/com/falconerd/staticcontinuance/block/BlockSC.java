package com.falconerd.staticcontinuance.block;

import com.falconerd.staticcontinuance.creativetab.CreativeTabSC;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockSC extends Block
{
    public BlockSC(Material material)
    {
        super(material);
        this.setCreativeTab(CreativeTabSC.SC_TAB);
    }

    public BlockSC()
    {
        this(Material.rock);
    }

    protected void init()
    {

    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedLocalizedName(super.getUnlocalizedName()));
    }

    // This is no longer how you register block icons
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerBlockIcons(IIconRegister iconRegister)
//    {
//        blockIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
//    }

    protected String getUnwrappedLocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
