package org.snobot.autonomous;

import java.io.File;
import java.util.Objects;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.commands.StupidDriveStraight;
import org.snobot.lib.autonomous.ObservableSendableChooser;
import org.snobot.lib.autonomous.SnobotAutonCrawler;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousFactory
{
    private static final double sY_START = 336 - 3 * 12;

    private static final double sX_RIGHT = -9 * 12;
    private static final double sX_CENTER = 0;
    private static final double sX_LEFT = 9 * 12;

    protected ObservableSendableChooser<File> mAutonModeChooserA;
    protected ObservableSendableChooser<File> mAutonModeChooserB;
    protected ObservableSendableChooser<StartingPositions> mPositionChooser;

    protected CommandParser mCommandParserA;
    protected CommandParser mCommandParserB;

    protected IPositioner mPositioner;
    protected Snobot2018 mSnobot;

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
    public AutonomousFactory(Snobot2018 aSnobot)
    {
        mSnobot = aSnobot;

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
            return this.getDefaultCommand();
        }

        String switchPosition = String.valueOf(gameSpecificMessage.charAt(0));
        String scalePosition = String.valueOf(gameSpecificMessage.charAt(1));

        CommandGroup outputA = this.tryLoadFile(mAutonModeChooserA.getSelected(), scalePosition, switchPosition, mCommandParserA);
        CommandGroup outputB = this.tryLoadFile(mAutonModeChooserB.getSelected(), scalePosition, switchPosition, mCommandParserB); // NOPMD
        if (outputA != null)
        {
            return outputA;
        }
        if (outputB != null)
        {
            return outputB;
        }
        return getDefaultCommand();
    }

    private CommandGroup tryLoadFile(File aFile, String aScalePos, String aSwitchPos, CommandParser aCommandParser)
    {
        if (aFile == null)
        {
            return getDefaultCommand();
        }

        CommandGroup commandGroup = aCommandParser.readFile(aFile.toString());
        boolean checkScale = Objects.equals(aCommandParser.getScaleTrigger(), aScalePos);
        boolean checkSwitch = Objects.equals(aCommandParser.getSwitchTrigger(), aSwitchPos);
        boolean checkBothNull = (aCommandParser.getSwitchTrigger() == null) && (aCommandParser.getScaleTrigger() == null);
        setPosition();
        
        if (checkScale || checkSwitch || checkBothNull)
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
        CommandGroup output = new CommandGroup();

        output.addSequential(new StupidDriveStraight(mSnobot.getDrivetrain(), Properties2018.sAUTON_DEFAULT_TIME.getValue(),
                Properties2018.sAUTON_DEFAULT_SPEED.getValue()));
        return output;

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

        if (startingPosition != null)
        {
            mPositioner.setPosition(startingPosition.mX, startingPosition.mY, startingPosition.mAngle);
        }
    }
}
