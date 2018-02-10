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

    // CTRE Detector
    public static final StringProperty sCTRE_FILE;

    // Autonomous
    public static final StringProperty sAUTON_FILE_FILTER = new StringProperty("AutonFileFilter", "");
    public static final StringProperty sAUTON_DIRECTORY;
    public static final StringProperty sAUTON_DEFAULT_FILE = new StringProperty("AutonDefault", "");
    public static final DoubleProperty sAUTON_DEFAULT_TIME = new DoubleProperty("AutonDefaultTime", 3);
    public static final DoubleProperty sAUTON_DEFAULT_SPEED = new DoubleProperty("AutonDefaultSpeed", .5);
    public static final DoubleProperty sGO_TO_XY_KPD = new DoubleProperty("GoToXYKPD", -.05);
    public static final DoubleProperty sGO_TO_XY_KPA = new DoubleProperty("GoToXYKPA", .05);

    // DriveTrain
    public static final DoubleProperty sLEFT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderLeftDPP", -0.074223);
    public static final DoubleProperty sRIGHT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderRightDPP", 0.0530035);

    // Turn Path
    public static final DoubleProperty sDRIVE_PATH_KP = new DoubleProperty("TurnPathKP", 0.05);
    public static final DoubleProperty sDRIVE_PATH_KD = new DoubleProperty("TurnPathKD", 0.05);
    public static final DoubleProperty sDRIVE_PATH_KV = new DoubleProperty("TurnPathKV", 0.05);
    public static final DoubleProperty sDRIVE_PATH_KA = new DoubleProperty("sDRIVE_PATH_KA", 0.05);
    
    public static final DoubleProperty sTURN_PATH_KP = new DoubleProperty("TurnPathKP", 0.05);
    public static final DoubleProperty sTURN_PATH_KD = new DoubleProperty("TurnPathKD", 0.05);
    public static final DoubleProperty sTURN_PATH_KV = new DoubleProperty("TurnPathKV", 0.05);
    public static final DoubleProperty sTURN_PATH_KA = new DoubleProperty("sDRIVE_PATH_KA", 0.05);
    
    public static final StringProperty sAUTON_PATH_DIRECTORY;
    public static final DoubleProperty sDRIVE_PATH_WITH_GYRO_KP = new DoubleProperty("DrivePathWithGyro", 0.01);
    
    public static final DoubleProperty sSPLINE_TURN_FACTOR = new DoubleProperty("SplineTurnFactor", 0.05);
    
    // Elevator
    public static final DoubleProperty sELEVATOR_K_P = new DoubleProperty("ElevatorKP", 0.1);
    public static final DoubleProperty sELEVATOR_HEIGHT_DEADBAND = new DoubleProperty("ElevatorHeightDeadband", 3);
    public static final DoubleProperty sELEVATOR_MAX_HEIGHT = new DoubleProperty("ElevatorMaxHeight", 74);
    public static final DoubleProperty sELEVATOR_MIN_HEIGHT = new DoubleProperty("ElevatorMinHeight", 1);
    public static final DoubleProperty sELEVATOR_DEADBAND = new DoubleProperty("ElevatorDeadband", 0.1);

    // Winch
    public static final DoubleProperty sWINCH_DEADBAND = new DoubleProperty("WinchDeadband", 0.05);

    static
    {
        String resourcesDir;
        String homeDirectory;

        if (RobotBase.isSimulation())
        {
            homeDirectory = "./";
            resourcesDir = "resources/";

            sLOGGER.log(Level.INFO, "Using simulation constants");
        }
        else
        {
            homeDirectory = "/home/lvuser/";
            resourcesDir = homeDirectory + "/2017Resources/";

            sLOGGER.log(Level.INFO, "Using tactical constants");
        }

        sCTRE_FILE = new StringProperty("CtreFile", homeDirectory + "isCan.properties");

        sAUTON_DIRECTORY = new StringProperty("AutonDir", resourcesDir + "autonomous/");
        sAUTON_PATH_DIRECTORY = new StringProperty("AutonDirPaths", resourcesDir + "trajectories");
    }

}
