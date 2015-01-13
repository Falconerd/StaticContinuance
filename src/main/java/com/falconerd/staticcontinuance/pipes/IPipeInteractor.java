package com.falconerd.staticcontinuance.pipes;

import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.LinkedHashMap;

public interface IPipeInteractor
{
    HashMap<EnumFacing, Boolean> pipeConnections = new LinkedHashMap<EnumFacing, Boolean>();
    HashMap<EnumFacing, Boolean> machineConnections = new LinkedHashMap<EnumFacing, Boolean>();

    HashMap<EnumFacing, Boolean> getMachineConnections();

    void setMachineConnections(HashMap<EnumFacing, Boolean> machineConnections);

    HashMap<EnumFacing, Boolean> getPipeConnections();

    void setPipeConnections(HashMap<EnumFacing, Boolean> pipeConnections);
}
