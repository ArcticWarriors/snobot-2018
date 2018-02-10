package org.snobot;

public class SmartDashboardNames
{
    // DriveTrain
    public static final String sLEFT_DRIVE_ENCODER_DISTANCE = "Left Drive Motor Encoder";
    public static final String sRIGHT_DRIVE_ENCODER_DISTANCE = "Right Drive Motor Encoder";

    // Elevator
    public static final String sELEVATOR_HEIGHT = "Elevator Height";
    public static final String sELEVATOR_MOTOR_SPEED = "Elevator Motor Speed";
    
    // Positioner
    public static final String sX_POSITION = "X Position";
    public static final String sY_POSITION = "Y Position";
    public static final String sORIENTATION = "Orientation";
    public static final String sSPEED = "Speed";

    public static final String sROBOT_COMMAND_TEXT = "Robot Command text";

    // Autonomous
    public static final String sSAVE_AUTON = "Save Auton";
    public static final String sAUTON_TABLE_A_NAME = "AutonTableA";
    public static final String sAUTON_TABLE_B_NAME = "AutonTableB";
    public static final String sAUTON_CHOOSER_A = "Autonomous Selection Plan A";
    public static final String sAUTON_CHOOSER_B = "Autonomous Selection Plan B";
    public static final String sAUTON_FILENAME = "Auton Filename";
    public static final String sSUCCESFULLY_PARSED_AUTON = "Parsed Command";

    // Autonomous Factory
    public static final String sPOSITION_CHOOSER = "Position Chooser";

    // Winch
    public static final String sWINCH_SPEED = "WinchSpeed";

    // Trajectory
    public static final String sSPLINE_NAMESPACE = "Spline Namespace";
    public static final String sSPLINE_IDEAL_POINTS = "Ideal Spline Points";
    public static final String sSPLINE_REAL_POINT = "Real Spline Point";
    public static final String sSPLINE_WAYPOINTS = "Spline Waypoints";

    // Simple motion profiling
    public static final String sPATH_NAMESPACE = "Path Namespace";
    public static final String sPATH_IDEAL_POINTS = "Planned Path";
    public static final String sPATH_POINT = "Path Point";

    // Go to position widgets (for GoToXY or Simple Motion Profiling)
    public static final String sGO_TO_POSITION_TABLE_NAME = "GoToPosition";
    public static final String sGO_TO_POSITION_START = "Start";
    public static final String sGO_TO_POSITION_END = "End";

    // Claw
    public static final String sSNOBOT_CLAW_POSITION = "Snobot Claw Position";
}
