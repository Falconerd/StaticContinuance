package com.falconerd.staticcontinuance.machine;

import com.falconerd.staticcontinuance.block.BlockContainerSC;
import com.falconerd.staticcontinuance.machine.tank.TileEntityTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockMachine extends BlockContainerSC
{
    public BlockMachine(Material material)
    {
        super(material);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            wrenchInteraction(playerIn, worldIn, pos);
            return true;
        }
        return false;
    }

    public void wrenchInteraction(EntityPlayer playerIn, World worldIn, BlockPos pos)
    {
        ItemStack itemStack = playerIn.inventory.getCurrentItem();

        TileEntityMachine tileEntityMachine = (TileEntityMachine) worldIn.getTileEntity(pos);

        if (itemStack != null)
        {
            if (itemStack.getUnlocalizedName().toLowerCase().contains("wrench"))
            {
                if (tileEntityMachine != null)
                {
                    if (tileEntityMachine instanceof TileEntityTank)
                    {
                        TileEntityTank tileEntityTank = (TileEntityTank) tileEntityMachine;

                        tileEntityTank.switchMode();
                        //String mode = Reference.MACHINE_MODES[(int) tileEntityTank.mode];

                        playerIn.addChatMessage(new ChatComponentText("Mode switched to: " + tileEntityTank.mode));
                    }
                }
            }
        }
    }
}
