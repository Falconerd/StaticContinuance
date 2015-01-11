package com.falconerd.staticcontinuance.machine.tank;

import com.falconerd.staticcontinuance.machine.TileEntityFluidMachine;

public class TileEntityTank extends TileEntityFluidMachine
{
    public double getFluidRenderHeight()
    {
        double renderHeight = getFluidRatio();
        if (renderHeight <= 0.02) return 0.02;
        if (renderHeight > 0.98) return 1.0;

        return Math.max(0, renderHeight);
    }
}
