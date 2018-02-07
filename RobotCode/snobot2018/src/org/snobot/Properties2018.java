package org.snobot;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.lib.PropertyManager.DoubleProperty;
import org.snobot.lib.PropertyManager.StringProperty;

import edu.wpi.first.wpilibj.RobotBase;

public class Properties2018
{
    // Logger
    private static final Logger sLOGGER = Logger.getLogger(Properties2018.class);

    // Autonomous
    public static final StringProperty sAUTON_FILE_FILTER = new StringProperty("AutonFileFilter", "");
    public static final StringProperty sAUTON_DIRECTORY;
    public static final StringProperty sAUTON_DEFAULT_FILE = new StringProperty("AutonDefault", "");

    // DriveTrain
    public static final DoubleProperty sLEFT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderLeftDPP", -0.074223);
    public static final DoubleProperty sRIGHT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderRightDPP", 0.0530035);

    // Elevator
    public static final DoubleProperty sELEVATOR_K_P = new DoubleProperty("ElevatorKP", 0.1);
    public static final DoubleProperty sELEVATOR_HEIGHT_DEADBAND = new DoubleProperty("ElevatorHeightDeadband", 3);
    public static final DoubleProperty sELEVATOR_MAX_HEIGHT = new DoubleProperty("ElevatorMaxHeight", 74);
    public static final DoubleProperty sELEVATOR_MIN_HEIGHT = new DoubleProperty("ElevatorMinHeight", 1);
    public static final DoubleProperty sELEVATOR_DEADBAND = new DoubleProperty("ElevatorDeadband", 0.1);

    static
    {
        String resourcesDir;
        if (RobotBase.isSimulation())
        {
            resourcesDir = "resources/";

            sLOGGER.log(Level.INFO, "Using simulation constants");
        }
        else
        {
            resourcesDir = "/home/lvuser/2017Resources/";

            sLOGGER.log(Level.INFO, "Using tactical constants");
        }

        sAUTON_DIRECTORY = new StringProperty("AutonDir", resourcesDir + "autonomous/");
    }

}
