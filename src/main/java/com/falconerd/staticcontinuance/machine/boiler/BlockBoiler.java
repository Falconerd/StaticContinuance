package com.falconerd.staticcontinuance.machine.boiler;

import com.falconerd.staticcontinuance.StaticContinuance;
import com.falconerd.staticcontinuance.handler.ConfigurationHandler;
import com.falconerd.staticcontinuance.handler.GuiHandler;
import com.falconerd.staticcontinuance.machine.BlockFluidMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BlockBoiler extends BlockFluidMachine implements IGuiHandler
{
    public BlockBoiler()
    {
        super(Material.iron);
        this.setUnlocalizedName("boiler");
    }

    @Override
    protected void init()
    {
        StaticContinuance.guiHandler.registerGuiHandler(GuiHandler.GUI_ID_BOILER, this);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TileEntityBoiler boiler = (TileEntityBoiler) worldIn.getTileEntity(pos);

            if (boiler != null)
            {
                playerIn.openGui(StaticContinuance.instance, GuiHandler.GUI_ID_BOILER, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBoiler(ConfigurationHandler.boilerWaterCapcity);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
}
