package com.falconerd.staticcontinuance.reference;

public class Reference
{
    public static final String MOD_ID = "staticcontinuance";
    public static final String MOD_NAME = "Static Continuance";
    public static final String MOD_VERSION = "1.8-0.1";
    public static final String GROUP = "com.falconerd." + MOD_ID;
    public static final String CLIENT_PROXY_CLASS = GROUP + ".proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = GROUP + ".proxy.ServerProxy";
    public static final String GUI_FACTORY_CLASS = GROUP + ".client.gui.GuiFactory";
    public static final int MACHINE_MODE_IN = 0;
    public static final int MACHINE_MODE_OUT = 1;
    public static final int MACHINE_MODE_DISABLED = 2;
    public static final int MACHINE_MODE_COUNT = 2;
    public static final String[] MACHINE_MODES = {"In", "Out", "Disabled"};
}