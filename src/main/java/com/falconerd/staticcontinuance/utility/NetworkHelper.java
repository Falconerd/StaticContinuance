package com.falconerd.staticcontinuance.utility;

import net.minecraft.util.EnumFacing;

import java.util.HashMap;

public class NetworkHelper
{
    public static HashMap<EnumFacing, Boolean> populateDirections(HashMap map, Boolean value)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            map.put(facing, value);
        }
        return map;
    }
}
