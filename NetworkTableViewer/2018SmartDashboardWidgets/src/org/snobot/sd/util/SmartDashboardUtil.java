package org.snobot.sd.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public final class SmartDashboardUtil
{
    private static final NetworkTable sTABLE = NetworkTableInstance.getDefault().getTable("SmartDashboard");

    private SmartDashboardUtil()
    {

    }

    public static NetworkTable getTable()
    {
        return sTABLE;
    }
}
