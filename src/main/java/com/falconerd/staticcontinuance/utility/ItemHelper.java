package com.falconerd.staticcontinuance.utility;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemHelper
{
    public static ItemStack consumeItem(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemPotion)
        {
            if (stack.stackSize == 1)
            {
                return new ItemStack(Items.glass_bottle);
            } else
            {
                stack.splitStack(1);
                return stack;
            }
        }
        if (stack.stackSize == 1)
        {
            if (stack.getItem().hasContainerItem(stack))
            {
                return stack.getItem().getContainerItem(stack);
            } else
            {
                return null;
            }
        } else
        {
            stack.splitStack(1);
            return stack;
        }
    }

    public static void dropItems(World worldIn, ItemStack stack, BlockPos pos, boolean randomSpread)
    {
        if (stack == null || stack.stackSize <= 0) return;

        EntityItem entityItem = createEntityItem(worldIn, stack, pos, randomSpread);
        worldIn.spawnEntityInWorld(entityItem);
    }

    public static EntityItem createEntityItem(World worldIn, ItemStack stack, BlockPos pos)
    {
        return createEntityItem(worldIn, stack, pos, true);
    }

    public static EntityItem createEntityItem(World worldIn, ItemStack stack, BlockPos pos, boolean randomSpread)
    {
        EntityItem entityItem;
        if (randomSpread)
        {
            float f1 = .75F;
            double d = (worldIn.rand.nextFloat() * f1) + (1F - f1) * .5D;
            double d1 = (worldIn.rand.nextFloat() * f1) + (1F - f1) * .5D;
            double d2 = (worldIn.rand.nextFloat() * f1) + (1F - f1) * .5D;
            entityItem = new EntityItem(worldIn, pos.getX() + d, pos.getY() + d1, +pos.getZ() + d2, stack);
            entityItem.setPickupDelay(10);
        } else
        {
            entityItem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            entityItem.motionX = 0;
            entityItem.motionY = 0;
            entityItem.motionZ = 0;
            entityItem.setPickupDelay(0);
        }
        return entityItem;
    }
}
