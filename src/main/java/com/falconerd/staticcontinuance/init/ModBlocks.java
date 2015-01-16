package com.falconerd.staticcontinuance.init;

import com.falconerd.staticcontinuance.block.BlockBrassBlock;
import com.falconerd.staticcontinuance.block.BlockContainerSC;
import com.falconerd.staticcontinuance.block.BlockCopperBlock;
import com.falconerd.staticcontinuance.block.BlockCopperOre;
import com.falconerd.staticcontinuance.block.BlockSC;
import com.falconerd.staticcontinuance.block.BlockSteam;
import com.falconerd.staticcontinuance.block.BlockZincBlock;
import com.falconerd.staticcontinuance.block.BlockZincOre;
import com.falconerd.staticcontinuance.machine.BlockFluidMachine;
import com.falconerd.staticcontinuance.machine.boiler.BlockBoiler;
import com.falconerd.staticcontinuance.machine.tank.BlockTank;
import com.falconerd.staticcontinuance.pipes.BlockPipe;
import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
    public static final BlockSC copperOre = new BlockCopperOre();
    public static final BlockSC copperBlock = new BlockCopperBlock();
    public static final BlockSC zincOre = new BlockZincOre();
    public static final BlockSC zincBlock = new BlockZincBlock();
    public static final BlockSC brassBlock = new BlockBrassBlock();
    public static final BlockFluidMachine tank = new BlockTank();
    public static final BlockContainerSC pipe = new BlockPipe();
    public static final BlockFluidMachine boiler = new BlockBoiler();
    public static Block blockSteam;

    public static void init()
    {
        GameRegistry.registerBlock(copperOre, "copperOre");
        GameRegistry.registerBlock(copperBlock, "copperBlock");
        GameRegistry.registerBlock(zincOre, "zincOre");
        GameRegistry.registerBlock(zincBlock, "zincBlock");
        GameRegistry.registerBlock(brassBlock, "brassBlock");
        GameRegistry.registerBlock(tank, "tank");
        GameRegistry.registerBlock(pipe, "pipe");
        GameRegistry.registerBlock(boiler, "boiler");

        // FluidBlocks

        if (ModFluids.fluidSteam.getBlock() == null)
        {
            blockSteam = new BlockSteam(ModFluids.fluidSteam, Material.water);
            blockSteam.setUnlocalizedName("blockSteam");
            //blockSteam.setCreativeTab(null);
            GameRegistry.registerBlock(blockSteam, "blockSteam");
            ModFluids.fluidSteam.setBlock(blockSteam);
        } else
        {
            blockSteam = ModFluids.fluidSteam.getBlock();
            blockSteam.setCreativeTab(null);
        }
    }

    public static void registerModels()
    {
        // TODO: I will find a better way to do this when I learn more about Java.
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(copperOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "copperOre", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(copperBlock), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "copperBlock", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(zincOre), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "zincOre", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(zincBlock), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "zincBlock", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(brassBlock), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "brassBlock", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(tank), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "tank", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(pipe), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "pipe", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(boiler), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "boiler", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(blockSteam), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "blockSteam", "inventory"));
    }
}
