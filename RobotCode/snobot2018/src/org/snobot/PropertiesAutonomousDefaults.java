package org.snobot;

import org.snobot.lib.PropertyManager.StringProperty;

public class PropertiesAutonomousDefaults
{
    private static final String sLEFT_SWITCH_TO_LEFT_MODE = "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_LeftToLeftSwitch";
    private static final String sLEFT_SCALE_TO_LEFT_MODE = "RealAutonomous/ScoreOnScaleTrajectory/ScoreOnHighScale_LeftToLeft";
    private static final String sLEFT_SCALE_TO_RIGHT_MODE = "RealAutonomous/ScoreOnScaleTrajectory/ScoreOnHighScale_LeftToRight";

    private static final String sRIGHT_SWITCH_TO_RIGHT_MODE = "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_RightToRightSwitch";
    private static final String sRIGHT_SCALE_TO_RIGHT_MODE = "RealAutonomous/ScoreOnScaleTrajectory/ScoreOnHighScale_RightToRight";
    private static final String sRIGHT_SCALE_TO_LEFT_MODE = "RealAutonomous/ScoreOnScaleTrajectory/ScoreOnHighScale_RightToLeft";

    //********************************************************************
    // Block 0
    //********************************************************************
    
    //
    public static final StringProperty sAUTON_MODE_A_1_FILE = new StringProperty("AutonMode_01A", "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_CenterToLeftSwitch_Pyramid");
    public static final StringProperty sAUTON_MODE_B_1_FILE = new StringProperty("AutonMode_01B", "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_CenterToRightSwitch_Pyramid");
    
    //
    public static final StringProperty sAUTON_MODE_A_2_FILE = new StringProperty("AutonMode_02A", "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_CenterToLeftSwitch_Scale");
    public static final StringProperty sAUTON_MODE_B_2_FILE = new StringProperty("AutonMode_02B", "RealAutonomous/ScoreOnSwitchTrajectory/ScoreOnSwitch_CenterToRightSwitch_Scale");
    
    // Scale, Switch
    public static final StringProperty sAUTON_MODE_A_3_FILE = new StringProperty("AutonMode_03A", sRIGHT_SCALE_TO_RIGHT_MODE);
    public static final StringProperty sAUTON_MODE_B_3_FILE = new StringProperty("AutonMode_03B", sRIGHT_SWITCH_TO_RIGHT_MODE);
    
    // Switch, Scale
    public static final StringProperty sAUTON_MODE_A_4_FILE = new StringProperty("AutonMode_04A", sRIGHT_SWITCH_TO_RIGHT_MODE);
    public static final StringProperty sAUTON_MODE_B_4_FILE = new StringProperty("AutonMode_04B", sRIGHT_SCALE_TO_RIGHT_MODE);

    // Scale always
    public static final StringProperty sAUTON_MODE_A_5_FILE = new StringProperty("AutonMode_05A", sRIGHT_SCALE_TO_RIGHT_MODE);
    public static final StringProperty sAUTON_MODE_B_5_FILE = new StringProperty("AutonMode_05B", sRIGHT_SCALE_TO_LEFT_MODE);

    //********************************************************************
    // Block 1
    //********************************************************************

    //
    public static final StringProperty sAUTON_MODE_A_13_FILE = new StringProperty("AutonMode_13A", sRIGHT_SWITCH_TO_RIGHT_MODE);
    public static final StringProperty sAUTON_MODE_B_13_FILE = new StringProperty("AutonMode_13B", sRIGHT_SCALE_TO_RIGHT_MODE);
    
    //
    public static final StringProperty sAUTON_MODE_A_14_FILE = new StringProperty("AutonMode_14A", sRIGHT_SCALE_TO_RIGHT_MODE);
    public static final StringProperty sAUTON_MODE_B_14_FILE = new StringProperty("AutonMode_14B", sRIGHT_SWITCH_TO_RIGHT_MODE);
    
    // Scale, Switch
    public static final StringProperty sAUTON_MODE_A_15_FILE = new StringProperty("AutonMode_15A", sLEFT_SCALE_TO_LEFT_MODE);
    public static final StringProperty sAUTON_MODE_B_15_FILE = new StringProperty("AutonMode_15B", sLEFT_SWITCH_TO_LEFT_MODE);

    // Switch, Scale
    public static final StringProperty sAUTON_MODE_A_16_FILE = new StringProperty("AutonMode_16A", sLEFT_SWITCH_TO_LEFT_MODE);
    public static final StringProperty sAUTON_MODE_B_16_FILE = new StringProperty("AutonMode_16B", sLEFT_SCALE_TO_LEFT_MODE);

    // Scale always
    public static final StringProperty sAUTON_MODE_A_17_FILE = new StringProperty("AutonMode_17A", sLEFT_SCALE_TO_LEFT_MODE);
    public static final StringProperty sAUTON_MODE_B_17_FILE = new StringProperty("AutonMode_17B", sLEFT_SCALE_TO_RIGHT_MODE);

//  //********************************************************************
//  // Center
//  //********************************************************************
//  public static final StringProperty sAUTON_MODE_A_7_FILE = new StringProperty("AutonMode_07A", sSWITCH_CENTER_TO_LEFT_MODE);
//  public static final StringProperty sAUTON_MODE_B_7_FILE = new StringProperty("AutonMode_07B", sSWITCH_CENTER_TO_RIGHT_MODE);
//  
//  public static final StringProperty sAUTON_MODE_A_8_FILE = new StringProperty("AutonMode_08A", sSWITCH_CENTER_TO_LEFT_MODE);
//  public static final StringProperty sAUTON_MODE_B_8_FILE = new StringProperty("AutonMode_08B", sSWITCH_CENTER_TO_RIGHT_MODE);
//  
//  public static final StringProperty sAUTON_MODE_A_9_FILE = new StringProperty("AutonMode_09A", sSWITCH_CENTER_TO_LEFT_MODE);
//  public static final StringProperty sAUTON_MODE_B_9_FILE = new StringProperty("AutonMode_09B", sSWITCH_CENTER_TO_RIGHT_MODE);
//  
//  public static final StringProperty sAUTON_MODE_A_10_FILE = new StringProperty("AutonMode_10A", sSWITCH_CENTER_TO_LEFT_MODE);
//  public static final StringProperty sAUTON_MODE_B_10_FILE = new StringProperty("AutonMode_10B", sSWITCH_CENTER_TO_RIGHT_MODE);
//  
//  public static final StringProperty sAUTON_MODE_A_11_FILE = new StringProperty("AutonMode_11A", sSWITCH_CENTER_TO_LEFT_MODE);
//  public static final StringProperty sAUTON_MODE_B_11_FILE = new StringProperty("AutonMode_11B", sSWITCH_CENTER_TO_RIGHT_MODE);
    
}
