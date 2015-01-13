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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTank extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int i)
    {
        bindTexture(new ResourceLocation(Reference.MOD_ID + ":" + "textures/blocks/tank.png"));
        TileEntityTank tileEntityTank = (TileEntityTank) tileEntity;
        GL11.glPushMatrix();

        RenderHelper.renderCube(posX, posY, posZ, posX + 1, posY + 1, posZ + 1, new BlockTank(), null);

        TankSC internalTank = tileEntityTank.getTank();
        FluidStack fluidStack = internalTank.getFluid();

        if (fluidStack != null)
        {
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glTranslated(posX, posY, posZ);

            Fluid fluid = fluidStack.getFluid();

            ResourceLocation fluidTexture = FluidHelper.getFluidResourceLocation(fluidStack);

            if (fluidTexture != null)
            {
                bindTexture(fluidTexture);
            } else
            {
                LogHelper.info("Cannot find fluidTexture");
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
