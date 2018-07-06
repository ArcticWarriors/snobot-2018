package org.snobot.autonomous;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snobot.PortMappings2018;
import org.snobot.Properties2018;
import org.snobot.PropertiesAutonomousDefaults;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.leds.ILedManager;
import org.snobot.lib.PropertyManager.StringProperty;
import org.snobot.lib.autonomous.ObservableSendableChooser;
import org.snobot.lib.autonomous.SnobotAutonCrawler;
import org.snobot.lib.modules.ISmartDashboardUpdaterModule;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousFactory implements ISmartDashboardUpdaterModule
{
    protected static final Logger sLOGGER = LogManager.getLogger(AutonomousFactory.class);

    private static final double sY_START = 336 - 3 * 12;

    private static final double sX_RIGHT = -9 * 12;
    private static final double sX_CENTER = 0;
    private static final double sX_LEFT = 9 * 12;

    protected final ObservableSendableChooser<File> mAutonModeChooserA;
    protected final ObservableSendableChooser<File> mAutonModeChooserB;
    protected final ObservableSendableChooser<StartingPositions> mPositionChooser;

    protected final CommandParser mCommandParserA;
    protected final CommandParser mCommandParserB;

    protected final ILedManager mLedManager;

    protected final IPositioner mPositioner;
    protected final Snobot2018 mSnobot;

    protected final AutonChooserSwitch mModeChooserSwitch;
    protected final AutonChooserSwitch mPositionChooserSwitch;
    protected final Map<Integer, StringProperty> mPresetAutonModesA;
    protected final Map<Integer, StringProperty> mPresetAutonModesB;

    protected int mLastMode = 0;
    protected int mLastPosition = 0;

    /**
     * Starting positions.
     * 
     * @author Owner
     *
     */
    public enum StartingPositions
    {
        Left("Left", sX_RIGHT, -sY_START, 0), 
        Center("Center", sX_CENTER, -sY_START, 0), 
        Right("Right", sX_LEFT, -sY_START, 0), 
        Origin("Origin", 0, 0, 0);

        // The name, position and angle of the starting positions.
        public final String mDisplayName;
        public final double mX;
        public final double mY;
        public final double mAngle;

        private StartingPositions(String aDisplayName, double aX, double aY, double aAngle)
        {

            mDisplayName = aDisplayName;
            mX = aX;
            mY = aY;
            mAngle = aAngle;
        }

        @Override
        public String toString()
        {
            return mDisplayName;
        }
    }

    /**
     * Gets and sets autonomous values.
     * 
     * @param aSnobot
     *            The top level robot
     */
    public AutonomousFactory(Snobot2018 aSnobot, ILedManager aLedManager)
    {
        mLedManager = aLedManager;
        mSnobot = aSnobot;
        mModeChooserSwitch = new AutonChooserSwitch(6, PortMappings2018.sMODE_CHOOSER_SWITCH);
        mPositionChooserSwitch = new AutonChooserSwitch(3, PortMappings2018.sPOSITION_CHOOSER_SWITCH);

        mPositionChooser = new ObservableSendableChooser<StartingPositions>();
        for (StartingPositions pos : StartingPositions.values())
        {
            if (pos == StartingPositions.Center)
            {
                mPositionChooser.addDefault(pos.mDisplayName, pos);
            }
            else
            {
                mPositionChooser.addObject(pos.toString(), pos);
            }
        }
        SmartDashboard.putData(SmartDashboardNames.sPOSITION_CHOOSER, mPositionChooser);

        NetworkTable autoModeTableA = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sAUTON_TABLE_A_NAME);
        mCommandParserA = new CommandParser(aSnobot, autoModeTableA);
        mAutonModeChooserA = new SnobotAutonCrawler(Properties2018.sAUTON_FILE_FILTER.getValue())
                .loadAutonFiles(Properties2018.sAUTON_DIRECTORY.getValue() + "/", Properties2018.sAUTON_DEFAULT_FILE.getValue());
        SmartDashboard.putData(SmartDashboardNames.sAUTON_CHOOSER_A, mAutonModeChooserA);

        NetworkTable autoModeTableB = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sAUTON_TABLE_B_NAME);
        mCommandParserB = new CommandParser(aSnobot, autoModeTableB);
        mAutonModeChooserB = new SnobotAutonCrawler(Properties2018.sAUTON_FILE_FILTER.getValue())
                .loadAutonFiles(Properties2018.sAUTON_DIRECTORY.getValue() + "/", Properties2018.sAUTON_DEFAULT_FILE.getValue());
        SmartDashboard.putData(SmartDashboardNames.sAUTON_CHOOSER_B, mAutonModeChooserB);

        mPositioner = aSnobot.getPositioner();

        addListeners(autoModeTableA, mAutonModeChooserA, mCommandParserA);
        addListeners(autoModeTableB, mAutonModeChooserB, mCommandParserB);
        addPositionLister();

        mPresetAutonModesA = new HashMap<>();
        // 0 is reserved for SD
        mPresetAutonModesA.put(1, PropertiesAutonomousDefaults.sAUTON_MODE_A_1_FILE);
        mPresetAutonModesA.put(2, PropertiesAutonomousDefaults.sAUTON_MODE_A_2_FILE);
        mPresetAutonModesA.put(3, PropertiesAutonomousDefaults.sAUTON_MODE_A_3_FILE);
        mPresetAutonModesA.put(4, PropertiesAutonomousDefaults.sAUTON_MODE_A_4_FILE);
        mPresetAutonModesA.put(5, PropertiesAutonomousDefaults.sAUTON_MODE_A_5_FILE);

        // 12 is reserved for SD
        mPresetAutonModesA.put(13, PropertiesAutonomousDefaults.sAUTON_MODE_A_13_FILE);
        mPresetAutonModesA.put(14, PropertiesAutonomousDefaults.sAUTON_MODE_A_14_FILE);
        mPresetAutonModesA.put(15, PropertiesAutonomousDefaults.sAUTON_MODE_A_15_FILE);
        mPresetAutonModesA.put(16, PropertiesAutonomousDefaults.sAUTON_MODE_A_16_FILE);
        mPresetAutonModesA.put(17, PropertiesAutonomousDefaults.sAUTON_MODE_A_17_FILE);
        
        mPresetAutonModesB = new HashMap<>();
        // 0 is reserved for SD
        mPresetAutonModesB.put(1, PropertiesAutonomousDefaults.sAUTON_MODE_B_1_FILE);
        mPresetAutonModesB.put(2, PropertiesAutonomousDefaults.sAUTON_MODE_B_2_FILE);
        mPresetAutonModesB.put(3, PropertiesAutonomousDefaults.sAUTON_MODE_B_3_FILE);
        mPresetAutonModesB.put(4, PropertiesAutonomousDefaults.sAUTON_MODE_B_4_FILE);
        mPresetAutonModesB.put(5, PropertiesAutonomousDefaults.sAUTON_MODE_B_5_FILE);

        // 12 is reserved for SD
        mPresetAutonModesB.put(13, PropertiesAutonomousDefaults.sAUTON_MODE_B_13_FILE);
        mPresetAutonModesB.put(14, PropertiesAutonomousDefaults.sAUTON_MODE_B_14_FILE);
        mPresetAutonModesB.put(15, PropertiesAutonomousDefaults.sAUTON_MODE_B_15_FILE);
        mPresetAutonModesB.put(16, PropertiesAutonomousDefaults.sAUTON_MODE_B_16_FILE);
        mPresetAutonModesB.put(17, PropertiesAutonomousDefaults.sAUTON_MODE_B_17_FILE);
    }

    /**
     * Create an autonomous mode.
     * 
     * @return The autonomous mode
     */
    public CommandGroup createAutonMode()
    {
        String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
        if ((gameSpecificMessage == null) || (gameSpecificMessage.length() != 3))
        {
            sLOGGER.log(Level.WARN, "GAME DATA NOT SET!!!one!11!1!1 ('" + gameSpecificMessage + "')");
            return this.getDefaultCommand();
        }

        String switchPosition = String.valueOf(gameSpecificMessage.charAt(0));
        String scalePosition = String.valueOf(gameSpecificMessage.charAt(1));

        int modeSwitchPosition = this.getAutonModeSwitchPosition();
        int positionSwitchPosition = this.getPositonChooserSwitch();

        int modePlusSixPos = modeSwitchPosition + (6 * positionSwitchPosition);

        File planAFile;
        File planBFile;

        if (mPresetAutonModesA.containsKey(modePlusSixPos))
        {
            sLOGGER.log(Level.INFO,
                    "Using auton switches: mode switch=" + modeSwitchPosition + ", pos switch=" + positionSwitchPosition + " -> " + modePlusSixPos);
            planAFile = new File(Properties2018.sAUTON_DIRECTORY.getValue(), mPresetAutonModesA.get(modePlusSixPos).getValue());
            planBFile = new File(Properties2018.sAUTON_DIRECTORY.getValue(), mPresetAutonModesB.get(modePlusSixPos).getValue());
            setPosition();
        }
        else
        {
            sLOGGER.log(Level.INFO, "Using auton choosers ");
            planAFile = mAutonModeChooserA.getSelected();
            planBFile = mAutonModeChooserB.getSelected();
        }

        CommandGroup outputA = this.tryLoadFile(planAFile, scalePosition, switchPosition, mCommandParserA);
        CommandGroup outputB = this.tryLoadFile(planBFile, scalePosition, switchPosition, mCommandParserB); // NOPMD
        if (outputA != null)
        {
            mLedManager.setAutoSelection(AutonSelectionType.PlanA);
            sLOGGER.log(Level.INFO, "Using Plan A");
            return outputA;
        }
        
        if (outputB != null)
        {
            mLedManager.setAutoSelection(AutonSelectionType.PlanB);
            sLOGGER.log(Level.INFO, "Using Plan B");
            return outputB;
        }

        mLedManager.setAutoSelection(AutonSelectionType.Default);
        sLOGGER.log(Level.WARN, "Using default autonomous");
        return getDefaultCommand();
    }

    private CommandGroup tryLoadFile(File aFile, String aScalePos, String aSwitchPos, CommandParser aCommandParser)
    {
        if (aFile == null)
        {
            return null;
        }

        CommandGroup commandGroup = aCommandParser.readFile(aFile.toString());
        boolean checkScale = Objects.equals(aCommandParser.getScaleTrigger(), aScalePos);
        boolean checkSwitch = Objects.equals(aCommandParser.getSwitchTrigger(), aSwitchPos);
        boolean checkBothNull = (aCommandParser.getSwitchTrigger() == null) && (aCommandParser.getScaleTrigger() == null);
        setPosition();
        
        if (aCommandParser.wasParsingSuccesful() && (checkScale || checkSwitch || checkBothNull))
        {
            return commandGroup;
        }
        return null;
    }

    /**
     * Gets default command.
     * 
     * @return Default command
     */
    private CommandGroup getDefaultCommand()
    {
        return new CommandGroup();
    }

    private void addPositionLister()
    {
        TableEntryListener setPositionListener = new TableEntryListener()
        {
            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                setPosition();
            }
        };

        mPositionChooser.addSelectionChangedListener(setPositionListener);
    }

    private void addListeners(NetworkTable aNetworkTable, ObservableSendableChooser<File> aFileChooser, CommandParser aCommandParser)
    {
        TableEntryListener buildAutonListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                createAutonMode();
            }
        };

        TableEntryListener saveListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                if (aValue.getBoolean())
                {
                    aCommandParser.saveAutonMode();

                    // Reload the autonomous on a change
                    createAutonMode();

                    // Clear the save flag after re-reading the file
                    aNetworkTable.getEntry(SmartDashboardNames.sSAVE_AUTON).setBoolean(false);
                }
            }
        };

        aNetworkTable.addEntryListener(SmartDashboardNames.sSAVE_AUTON, saveListener, 0xFF);

        aFileChooser.addSelectionChangedListener(buildAutonListener);
    }

    // Sets the starting position for the robot
    private void setPosition()
    {
        StartingPositions startingPosition = mPositionChooser.getSelected();

        if (getAutonModeSwitchPosition() != Properties2018.sDEFAULT_SWITCH_POSITION.getValue() || startingPosition == null)
        {
            int positionChooser = getPositonChooserSwitch();

            switch (positionChooser)
            {
            case 0:
                startingPosition = StartingPositions.Left;
                break;
            case 1:
                startingPosition = StartingPositions.Center;
                break;
            case 2:
                startingPosition = StartingPositions.Right;
                break;
            default:
                startingPosition = StartingPositions.Center;
                sLOGGER.log(Level.WARN, "Unsupported position chooser " + positionChooser);
                break;
            }
        }

        setPosition(startingPosition);
    }

    private void setPosition(StartingPositions aPosition)
    {
        mPositioner.setPosition(aPosition.mX, aPosition.mY, aPosition.mAngle);
    }


    public int getAutonModeSwitchPosition()
    {
        return mModeChooserSwitch.getPosition();
    }

    public int getPositonChooserSwitch()
    {
        return mPositionChooserSwitch.getPosition();
    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sAUTON_MODE_SWITCH, getAutonModeSwitchPosition());
        SmartDashboard.putNumber(SmartDashboardNames.sAUTON_POSITION_SWITCH, getPositonChooserSwitch());

        boolean changed = false;

        int position = getPositonChooserSwitch();
        int mode = getAutonModeSwitchPosition();

        changed |= mode != mLastMode;
        changed |= position != mLastPosition;
        if (changed)
        {
            mLastMode = mode;
            mLastPosition = position;
            sLOGGER.log(Level.INFO, "Mode changed in update");
            createAutonMode();
        }

    }
}
