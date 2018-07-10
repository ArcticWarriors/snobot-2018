package org.snobot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snobot.lib.PropertyManager.BooleanProperty;
import org.snobot.lib.PropertyManager.DoubleProperty;
import org.snobot.lib.PropertyManager.IntegerProperty;
import org.snobot.lib.PropertyManager.StringProperty;

import edu.wpi.first.wpilibj.RobotBase;

public class Properties2018
{
    // Error Logger
    private static final Logger sLOGGER = LogManager.getLogger(Properties2018.class);

    // CSV Logger
    public static final StringProperty sCSV_LOG_DIRECTORY;

    // CTRE Detector
    public static final StringProperty sCTRE_FILE;

    // Autonomous
    public static final StringProperty sAUTON_FILE_FILTER = new StringProperty("AutonFileFilter", "");
    public static final StringProperty sAUTON_DIRECTORY;
    public static final StringProperty sAUTON_DEFAULT_FILE = new StringProperty("AutonDefault", "");
    public static final DoubleProperty sAUTON_DEFAULT_TIME = new DoubleProperty("AutonDefaultTime", 3);
    public static final DoubleProperty sAUTON_DEFAULT_SPEED = new DoubleProperty("AutonDefaultSpeed", .5);
    public static final DoubleProperty sGO_TO_XY_KPD = new DoubleProperty("GoToXYKPD", 0.015);
    public static final DoubleProperty sGO_TO_XY_KPA = new DoubleProperty("GoToXYKPA", .05);
    public static final DoubleProperty sMIN_VOLTAGE = new DoubleProperty("MinVoltage", 0);
    public static final DoubleProperty sMAX_VOLTAGE = new DoubleProperty("MaxVoltage", 5);
    
    //Autonomous Switch Modes A1-A15
    public static final IntegerProperty sDEFAULT_SWITCH_POSITION = new IntegerProperty("DefaultSwitchPosition", 0); // Use

    
    // DriveTrain
    public static final DoubleProperty sLEFT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderLeftDPP", -0.074223);
    public static final DoubleProperty sRIGHT_ENCODER_DIST_PER_PULSE = new DoubleProperty("DriveEncoderRightDPP", 0.0530035);
    public static final DoubleProperty sDRIVE_SLOW_MULTIPLIER = new DoubleProperty("DriveSlowMultilpier", 0.75);
    public static final DoubleProperty sDRIVE_SUPER_SLOW_MULTIPLIER = new DoubleProperty("DriveSuperSlowMultilpier", 0.5);


    // Turn Path
    public static final DoubleProperty sDRIVE_PATH_KP = new DoubleProperty("DrivePathKP", 0.001);
    public static final DoubleProperty sDRIVE_PATH_KD = new DoubleProperty("DrivePathKD", 0.00);
    public static final DoubleProperty sDRIVE_PATH_KV = new DoubleProperty("DrivePathKV", 0.018);
    public static final DoubleProperty sDRIVE_PATH_KA = new DoubleProperty("DrivePathKA", 0.00);
    
    public static final DoubleProperty sTURN_PATH_KP = new DoubleProperty("TurnPathKP", 0.00);
    public static final DoubleProperty sTURN_PATH_KD = new DoubleProperty("TurnPathKD", 0.00);
    public static final DoubleProperty sTURN_PATH_KV = new DoubleProperty("TurnPathKV", 0.005);
    public static final DoubleProperty sTURN_PATH_KA = new DoubleProperty("TurnPathKA", 0.00);
    
    public static final StringProperty sAUTON_PATH_DIRECTORY;
    public static final DoubleProperty sDRIVE_PATH_WITH_GYRO_KP = new DoubleProperty("DrivePathWithGyro", 0.01);
    
    public static final DoubleProperty sSPLINE_TURN_FACTOR = new DoubleProperty("SplineTurnFactor", 0.05);
    
    // Elevator
    public static final BooleanProperty sELEVATOR_OVERRIDE_SAFETY = new BooleanProperty("ElevatorOverrideHeightSafety", false);
    public static final DoubleProperty sELEVATOR_HEIGHT_DEADBAND = new DoubleProperty("ElevatorHeightDeadband", 3);
    public static final DoubleProperty sELEVATOR_MAX_HEIGHT = new DoubleProperty("ElevatorMaxHeight", 74);
    public static final DoubleProperty sELEVATOR_MIN_HEIGHT = new DoubleProperty("ElevatorMinHeight", 1);
    public static final DoubleProperty sELEVATOR_JOYSTICK_DEADBAND = new DoubleProperty("ElevatorJoystickDeadband", 0.1);
    public static final DoubleProperty sELEVATOR_FLOOR_HEIGHT = new DoubleProperty("ElevatorFloorHeight", 1);
    public static final DoubleProperty sELEVATOR_SCALE_HEIGHT = new DoubleProperty("ElevatorScaleHeightMedium", 60);
    public static final DoubleProperty sELEVATOR_SCALE_HEIGHT_LOW = new DoubleProperty("ElevatorScaleHeightLow", 48);
    public static final DoubleProperty sELEVATOR_SCALE_HEIGHT_HIGH = new DoubleProperty("ElevatorScaleHeightHigh", 72);
    public static final DoubleProperty sELEVATOR_SWITCH_HEIGHT = new DoubleProperty("ElevatorSwitchHeight", 15);
    public static final DoubleProperty sELEVATOR_EXCHANGE_HEIGHT = new DoubleProperty("ElevatorExchangeHeight", 1.75);

    public static final DoubleProperty sELEVATOR_K_P = new DoubleProperty("ElevatorKP", 0.1);

    public static final DoubleProperty sELEVATOR_CTRE_CONVERSION_FACTOR = new DoubleProperty("ElevatorCtreConversionFactor", 1);
    public static final IntegerProperty sELEVATOR_CTRE_CRUISE_VELOCITY = new IntegerProperty("ElevatorCtreCruiseVelocity", 12000);
    public static final IntegerProperty sELEVATOR_CTRE_CRUISE_ACCELERATION = new IntegerProperty("ElevatorCtreCruiseAcceleration", 12000);
    public static final DoubleProperty sELEVATOR_CTRE_KP = new DoubleProperty("ElevatorCtreKp", 0);
    public static final DoubleProperty sELEVATOR_CTRE_KFF = new DoubleProperty("ElevatorCtreKff", .2);

    // Winch
    public static final DoubleProperty sWINCH_DEADBAND = new DoubleProperty("WinchDeadband", 0.05);

    // Vision
    public static final StringProperty sADB_PATH;

    static
    {
        String resourcesDir;
        String homeDirectory;
        String csvLogDirectory;
        String adbLocation;

        if (RobotBase.isSimulation())
        {
            homeDirectory = "./";
            resourcesDir = "resources/";
            csvLogDirectory = "logs";

            if (System.getProperty("os.name").contains("Windows"))
            {
                adbLocation = System.getProperty("user.home") + "/AppData/Local/Android/sdk/platform-tools/adb.exe";
            }
            else
            {
                adbLocation = System.getProperty("user.home") + "/Android/Sdk/platform-tools/adb";
            }

            sLOGGER.log(Level.INFO, "Using simulation constants");
        }
        else
        {
            homeDirectory = "/home/lvuser/";
            resourcesDir = homeDirectory + "/resources";
            csvLogDirectory = homeDirectory + "/logs";
            adbLocation = "/tmp/adb";

            sLOGGER.log(Level.INFO, "Using tactical constants");
        }

        sCTRE_FILE = new StringProperty("CtreFile", homeDirectory + "isCan.properties");

        sAUTON_DIRECTORY = new StringProperty("AutonDir", resourcesDir + "autonomous/");
        sAUTON_PATH_DIRECTORY = new StringProperty("AutonDirPaths", resourcesDir + "trajectories");
        sCSV_LOG_DIRECTORY = new StringProperty("CsvLogger", csvLogDirectory);
        sADB_PATH = new StringProperty("AdbLocation", adbLocation);
    }

}
