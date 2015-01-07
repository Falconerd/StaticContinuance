package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.reference.Reference;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityRenderPipe extends TileEntitySpecialRenderer
{
    ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/models/pipe.png");

    float pixel = 1F / 16F;
    float texturePixel = 1F / 32F;
    float a = 11 * pixel / 2;
    float b = 1 - 11 * pixel / 2;
    float t1 = 5 * texturePixel;
    float t2 = 10 * texturePixel;
    float t3 = 26 * texturePixel;
    boolean drawInside = true;

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int j)
    {
        GL11.glTranslated(posX, posY, posZ);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.bindTexture(texture);
        {
            TileEntityPipe pipe = (TileEntityPipe) tileEntity;

            if (pipe.onlyOneOpposite())
            {
                for (EnumFacing key : pipe.connections.keySet())
                {
                    if (pipe.connections.get(key))
                    {
                        if (pipe.isPipe(key))
                        {
                            drawStraight(key);
                            break;
                        } else
                        {
                            drawMachineConnection(key);
                        }
                    }
                }
            } else
            {
                drawCore();

                for (EnumFacing key : pipe.connections.keySet())
                {
                    if (pipe.connections.get(key))
                    {
                        if (pipe.isPipe(key))
                        {
                            drawConnection(key);
                        } else
                        {
                            drawMachineConnection(key);
                        }
                    }
                }
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslated(-posX, -posY, -posZ);
    }

    public void drawStraight(EnumFacing facing)
    {
        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.EAST) || facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(90, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        {
            tessellator.getWorldRenderer().addVertexWithUV(b, 0, b, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t3, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t3, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, 0, b, t2, 0);

            tessellator.getWorldRenderer().addVertexWithUV(a, 0, a, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t3, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t3, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, 0, a, t2, 0);

            tessellator.getWorldRenderer().addVertexWithUV(b, 0, a, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t3, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t3, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, 0, b, t2, 0);

            tessellator.getWorldRenderer().addVertexWithUV(a, 0, b, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t3, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t3, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, 0, a, t2, 0);

            if (drawInside)
            {
                tessellator.getWorldRenderer().addVertexWithUV(a, 0, b, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t3, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t3, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, 0, b, t2, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, 0, a, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t3, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t3, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, 0, a, t2, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, 0, b, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t3, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t3, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, 0, a, t2, t1);

                tessellator.getWorldRenderer().addVertexWithUV(a, 0, a, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t3, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t3, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, 0, b, t2, t1);
            }
        }
        tessellator.draw();

        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(-90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.EAST) || facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(-90, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);
    }

    public void drawConnection(EnumFacing facing)
    {
        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.DOWN))
        {
            GL11.glRotatef(180, 1, 0, 0);
        } else if (facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.NORTH))
        {
            GL11.glRotatef(270, 1, 0, 0);
        } else if (facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(90, 0, 0, 1);
        } else if (facing.equals(EnumFacing.EAST))
        {
            GL11.glRotatef(270, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        {
            tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t2, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, 0);

            tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t2, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);

            tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t2, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, 0);

            tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t2, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t2, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, 0);

            if (drawInside)
            {
                tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t2, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t2, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, b, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, 1, a, t2, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, a, t2, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, 1, b, t2, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, t1);
            }
        }
        tessellator.draw();

        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.DOWN))
        {
            GL11.glRotatef(-180, 1, 0, 0);
        } else if (facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(-90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.NORTH))
        {
            GL11.glRotatef(-270, 1, 0, 0);
        } else if (facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(-90, 0, 0, 1);
        } else if (facing.equals(EnumFacing.EAST))
        {
            GL11.glRotatef(-270, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);
    }

    public void drawMachineConnection(EnumFacing facing)
    {
        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.DOWN))
        {
            GL11.glRotatef(180, 1, 0, 0);
        } else if (facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.NORTH))
        {
            GL11.glRotatef(270, 1, 0, 0);
        } else if (facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(90, 0, 0, 1);
        } else if (facing.equals(EnumFacing.EAST))
        {
            GL11.glRotatef(270, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        {
            tessellator.getWorldRenderer().addVertexWithUV(b, 1, 11 * pixel, 26 * texturePixel, 7 * texturePixel);
            tessellator.getWorldRenderer().addVertexWithUV(b, 11 * pixel, 11 * pixel, 31 * texturePixel, 7 * texturePixel);
            tessellator.getWorldRenderer().addVertexWithUV(a, 11 * pixel, 11 * pixel, 31 * texturePixel, 0 * texturePixel);
            tessellator.getWorldRenderer().addVertexWithUV(a, 1, 11 * pixel, 26 * texturePixel, 0 * texturePixel);

            // TODO: This
//            if (drawInside)
//            {
//            }
        }
        tessellator.draw();

        GL11.glTranslatef(.5F, .5F, .5F);
        if (facing.equals(EnumFacing.DOWN))
        {
            GL11.glRotatef(-180, 1, 0, 0);
        } else if (facing.equals(EnumFacing.SOUTH))
        {
            GL11.glRotatef(-90, 1, 0, 0);
        } else if (facing.equals(EnumFacing.NORTH))
        {
            GL11.glRotatef(-270, 1, 0, 0);
        } else if (facing.equals(EnumFacing.WEST))
        {
            GL11.glRotatef(-90, 0, 0, 1);
        } else if (facing.equals(EnumFacing.EAST))
        {
            GL11.glRotatef(-270, 0, 0, 1);
        }
        GL11.glTranslatef(-.5F, -.5F, -.5F);
    }

    public void drawCore()
    {
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();
        {
            tessellator.getWorldRenderer().addVertexWithUV(b, a, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, b, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, a, b, 0, t1);

            tessellator.getWorldRenderer().addVertexWithUV(b, a, a, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, b, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, a, b, 0, t1);

            tessellator.getWorldRenderer().addVertexWithUV(a, a, a, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, a, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, a, a, 0, t1);

            tessellator.getWorldRenderer().addVertexWithUV(a, a, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, a, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, a, a, 0, t1);

            tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, a, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(a, b, b, 0, t1);

            tessellator.getWorldRenderer().addVertexWithUV(a, a, b, t1, t1);
            tessellator.getWorldRenderer().addVertexWithUV(a, a, a, t1, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, a, a, 0, 0);
            tessellator.getWorldRenderer().addVertexWithUV(b, a, b, 0, t1);

            if (drawInside)
            {
                tessellator.getWorldRenderer().addVertexWithUV(a, a, b, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, b, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, a, b, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, a, b, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, b, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, a, a, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, a, a, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, a, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, a, a, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(a, a, a, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, a, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, b, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, a, b, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(a, b, b, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(a, b, a, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(b, b, b, t1, t1);

                tessellator.getWorldRenderer().addVertexWithUV(b, a, b, 0, t1);
                tessellator.getWorldRenderer().addVertexWithUV(b, a, a, 0, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, a, a, t1, 0);
                tessellator.getWorldRenderer().addVertexWithUV(a, a, b, t1, t1);
            }
        }
        tessellator.draw();
    }
}
