package com.falconerd.staticcontinuance.machine.tank;

import com.falconerd.staticcontinuance.reference.Reference;
import com.falconerd.staticcontinuance.utility.FluidHelper;
import com.falconerd.staticcontinuance.utility.LogHelper;
import com.falconerd.staticcontinuance.utility.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTank extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int i)
    {
//        TileEntityTank tileEntityTank = (TileEntityTank) tileEntity;
//        IFluidTank internalTank = tileEntityTank.tank;
//        FluidStack fluidStack = internalTank.getFluid();
//
//        GL11.glPushMatrix();
//        {
//            RenderHelper.renderCube(posX, posY, posZ, posX + 1, posY + 1, posZ + 1, new BlockTank(), "tank");
//
//            if (fluidStack != null)
//            {
//                GL11.glTranslated(posX, posY, posZ);
//                GL11.glDisable(GL11.GL_LIGHTING);
//
//                bindTexture(FluidHelper.getFluidResourceLocation(fluidStack));
//
//                Tessellator tessellator = Tessellator.getInstance();
//                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
//
//                double fluidMin = .0625;
//                double fluidMax = .9375;
//
//                worldRenderer.startDrawingQuads();
//
//                {
//                    worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMin, fluidMin, fluidMin);
//                    worldRenderer.addVertexWithUV(fluidMin, fluidMax, fluidMin, fluidMin, fluidMax);
//                    worldRenderer.addVertexWithUV(fluidMax, fluidMax, fluidMin, fluidMax, fluidMax);
//                    worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMin, fluidMax, fluidMin);
//
//                    //                worldRenderer.addVertexWithUV(0, 0, 0, 0, 0);
//                    //                worldRenderer.addVertexWithUV(1, 0, 0, 1, 0);
//                    //                worldRenderer.addVertexWithUV(1, 1, 0, 1, 1);
//                    //                worldRenderer.addVertexWithUV(0, 1, 0, 0, 1);
//                }
//
//                tessellator.draw();
//
//                GL11.glEnable(GL11.GL_LIGHTING);
//            }
//        }
//        GL11.glPopMatrix();

        bindTexture(new ResourceLocation(Reference.MOD_ID + ":" + "textures/blocks/tank.png"));
        TileEntityTank tileEntityTank = (TileEntityTank) tileEntity;
        GL11.glPushMatrix();

        RenderHelper.renderCube(posX, posY, posZ, posX + 1, posY + 1, posZ + 1, new BlockTank(), null);

        IFluidTank internalTank = tileEntityTank.tank;
        FluidStack fluidStack = internalTank.getFluid();

//        LogHelper.info("RenderTank.renderTileEntityAt: FluidStack: " + (fluidStack != null ? fluidStack.getLocalizedName() : "null"));
//        LogHelper.info("RenderTank.renderTileEntityAt: FluidAmount: " + tileEntityTank.tank.getFluidAmount());
//        LogHelper.info("RenderTank.renderTileEntityAt: Tank: " + tileEntityTank);

        if (fluidStack != null)
        {
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glTranslated(posX, posY, posZ);

            Fluid fluid = fluidStack.getFluid();

            ResourceLocation fluidTexture = FluidHelper.getFluidResourceLocation(fluidStack);
            final int color;

            if (fluidTexture != null)
            {
                bindTexture(fluidTexture);
                color = fluid.getColor(fluidStack);
            } else
            {
                LogHelper.info("Cannot find fluidTexture");
                color = fluid.getColor(fluidStack);
            }

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();

            double fluidMin = .0625;
            double fluidMax = .9375;
            double fluidLvl = fluidMax * tileEntityTank.getFluidRenderHeight();

            worldRenderer.startDrawingQuads();

            {
                // North
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMin, fluidMin, fluidMin);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMin, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMin, fluidMax, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMin, fluidMax, fluidMin);

                // South
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMax, fluidMax, fluidMin);
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMax, fluidMax, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMax, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMax, fluidMin, fluidMin);

                // East
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMax, fluidMax, fluidMin);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMax, fluidMax, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMin, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMin, fluidMin, fluidMin);

                // West
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMin, fluidMin, fluidMin);
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMin, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMax, fluidMax, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMax, fluidMax, fluidMin);

                // Top
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMax, fluidMin, fluidMin);
                worldRenderer.addVertexWithUV(fluidMax, fluidLvl, fluidMin, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMin, fluidMax / 20, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMin, fluidLvl, fluidMax, fluidMax / 20, fluidMin);

                // Bottom
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMax, fluidMin, fluidMin);
                worldRenderer.addVertexWithUV(fluidMin, fluidMin, fluidMin, fluidMin, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMin, fluidMax / 20, fluidMax / 20);
                worldRenderer.addVertexWithUV(fluidMax, fluidMin, fluidMax, fluidMax / 20, fluidMin);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glPopMatrix();
    }
}
