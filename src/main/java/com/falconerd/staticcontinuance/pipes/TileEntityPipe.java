package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.tileentity.TileEntitySC;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Everything to do with pipes may need to be cleaned up and/or rewritten
 */
public class TileEntityPipe extends TileEntitySC implements IPipeInteractor
{
    // I prefer to map connections like this rather than use an array with indexes 0-5
    public HashMap<EnumFacing, Boolean> connections = new HashMap<EnumFacing, Boolean>();

    public TileEntityPipe()
    {
        // Set all of the connections to false when instantiated
        connections.put(EnumFacing.UP, false);
        connections.put(EnumFacing.DOWN, false);
        connections.put(EnumFacing.NORTH, false);
        connections.put(EnumFacing.EAST, false);
        connections.put(EnumFacing.SOUTH, false);
        connections.put(EnumFacing.WEST, false);
    }

    public void updateConnections(boolean once)
    {
        // Get the x, y, z
        int x = getPos().getX();
        int y = getPos().getY();
        int z = getPos().getZ();

        // Get all possible connections
        HashMap<EnumFacing, BlockPos> candidates = new HashMap<EnumFacing, BlockPos>();
        candidates.put(EnumFacing.UP, new BlockPos(x, y + 1, z));
        candidates.put(EnumFacing.DOWN, new BlockPos(x, y - 1, z));
        candidates.put(EnumFacing.NORTH, new BlockPos(x, y, z - 1));
        candidates.put(EnumFacing.EAST, new BlockPos(x + 1, y, z));
        candidates.put(EnumFacing.SOUTH, new BlockPos(x, y, z + 1));
        candidates.put(EnumFacing.WEST, new BlockPos(x - 1, y, z));

        for (EnumFacing key : candidates.keySet())
        {
            // Get the tile entity of possible connection
            TileEntity tileEntity = worldObj.getTileEntity(candidates.get(key));

            // If the tile entity is something that can interact with pipes
            if (tileEntity instanceof IPipeInteractor)
            {
                // If the tile entity is a pipe itself
                if (!once && tileEntity instanceof TileEntityPipe)
                {
                    // Update it's connections as well (but do not let it update it's neighbours)
                    ((TileEntityPipe) tileEntity).updateConnections(true);
                }
                // Set this as a valid connection
                connections.replace(key, false, true);
            } else
            {
                // There is no valid connection here
                connections.replace(key, true, false);
            }
        }
    }

    /**
     * Check to see whether this pipe has no adjacent pipes
     *
     * @return boolean
     */
    public boolean onlyOneOpposite()
    {
        // Set the possible connections
        boolean up = connections.get(EnumFacing.UP);
        boolean down = connections.get(EnumFacing.DOWN);
        boolean north = connections.get(EnumFacing.NORTH);
        boolean south = connections.get(EnumFacing.SOUTH);
        boolean east = connections.get(EnumFacing.EAST);
        boolean west = connections.get(EnumFacing.WEST);

        // Compare and return true if only two connections and connections are opposite
        if (up && down && !north && !south && !east && !west)
        {
            return true;
        } else if (north && south && !up && !down && !east && !west)
        {
            return true;
        } else if (east && west && !up && !down && !north && !south)
        {
            return true;
        }

        return false;
    }

    /**
     * Check if the adjacent is a pipe
     *
     * @param facing The direction to check
     */
    public boolean isPipe(EnumFacing facing)
    {
        return (worldObj.getTileEntity(pos.offset(facing)) instanceof TileEntityPipe);
    }


    public boolean hasValidConnections()
    {
        for (EnumFacing facing : connections.keySet())
        {
            if (connections.get(facing))
            {
                return true;
            }
        }
        return false;
    }

    public List<TileEntityPipe> getValidConnections()
    {
        List<TileEntityPipe> validConnectionsPipes = new ArrayList<TileEntityPipe>();

        for (EnumFacing facing : connections.keySet())
        {
            if (connections.get(facing))
            {
                TileEntity te = worldObj.getTileEntity(getPos().offset(facing));
                if (te instanceof TileEntityPipe)
                {
                    validConnectionsPipes.add((TileEntityPipe) te);
                }
            }
        }
        return validConnectionsPipes;
    }

    /**
     * Check to see if this pipe is connected to some non-pipe which can handle liquid
     */
    public boolean isValve()
    {
        for (EnumFacing facing : connections.keySet())
        {
            if (connections.get(facing))
            {
                TileEntity te = worldObj.getTileEntity(getPos().offset(facing));
                // We know this connection can interact with pipes
                if (te instanceof IPipeInteractor)
                {
                    // This connection is not a pipe, therefore we are valve pipe
                    if (!(te instanceof TileEntityPipe))
                    {
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
