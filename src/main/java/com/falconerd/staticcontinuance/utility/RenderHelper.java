package com.falconerd.staticcontinuance.utility;

import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderHelper
{
    public static void renderCube(double x, double y, double z, double x2, double y2, double z2, Block block, String texture)
    {
        renderCube(x, y, z, x2, y2, z2, block, texture, 0);
    }

    public static void renderCube(double x, double y, double z, double x2, double y2, double z2, Block block, String texture, int meta)
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GL11.glColor4f(1, 1, 1, 1);

        String textureName = texture != null ? texture : block.getUnlocalizedName();
        ResourceLocation textureResource = new ResourceLocation(Reference.MOD_ID + ":" + "textures/models/" + textureName + ".png");

        GL11.glTranslated(x, y, z);

        worldRenderer.startDrawingQuads();

        {

            // North
            worldRenderer.addVertexWithUV(0, 0, 0, 0, 0);
            worldRenderer.addVertexWithUV(0, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(1, 1, 0, 1, 1);
            worldRenderer.addVertexWithUV(1, 0, 0, 1, 0);

            worldRenderer.addVertexWithUV(0, 0, 0, 0, 0);
            worldRenderer.addVertexWithUV(1, 0, 0, 1, 0);
            worldRenderer.addVertexWithUV(1, 1, 0, 1, 1);
            worldRenderer.addVertexWithUV(0, 1, 0, 0, 1);

            // South
            worldRenderer.addVertexWithUV(0, 0, 1, 0, 0);
            worldRenderer.addVertexWithUV(0, 1, 1, 0, 1);
            worldRenderer.addVertexWithUV(1, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);

            worldRenderer.addVertexWithUV(0, 0, 1, 0, 0);
            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);
            worldRenderer.addVertexWithUV(1, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(0, 1, 1, 0, 1);

            // East
            worldRenderer.addVertexWithUV(1, 0, 0, 0, 0);
            worldRenderer.addVertexWithUV(1, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(1, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);

            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);
            worldRenderer.addVertexWithUV(1, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(1, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(1, 0, 0, 0, 0);

            // West
            worldRenderer.addVertexWithUV(0, 0, 0, 0, 0);
            worldRenderer.addVertexWithUV(0, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(0, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(0, 0, 1, 1, 0);

            worldRenderer.addVertexWithUV(0, 0, 1, 1, 0);
            worldRenderer.addVertexWithUV(0, 1, 1, 1, 1);
            worldRenderer.addVertexWithUV(0, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(0, 0, 0, 0, 0);

            // Top
            worldRenderer.addVertexWithUV(1, 1, 1, 0, 0);
            worldRenderer.addVertexWithUV(1, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(0, 1, 0, 1, 1);
            worldRenderer.addVertexWithUV(0, 1, 1, 1, 0);

            worldRenderer.addVertexWithUV(0, 1, 1, 1, 0);
            worldRenderer.addVertexWithUV(0, 1, 0, 1, 1);
            worldRenderer.addVertexWithUV(1, 1, 0, 0, 1);
            worldRenderer.addVertexWithUV(1, 1, 1, 0, 0);

            // Bottom
            worldRenderer.addVertexWithUV(0, 0, 1, 0, 0);
            worldRenderer.addVertexWithUV(0, 0, 0, 0, 1);
            worldRenderer.addVertexWithUV(1, 0, 0, 1, 1);
            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);

            worldRenderer.addVertexWithUV(1, 0, 1, 1, 0);
            worldRenderer.addVertexWithUV(1, 0, 0, 1, 1);
            worldRenderer.addVertexWithUV(0, 0, 0, 0, 1);
            worldRenderer.addVertexWithUV(0, 0, 1, 0, 0);

        }

        tessellator.draw();

        GL11.glTranslated(-x, -y, -z);

        GL11.glPopMatrix();
    }
}
