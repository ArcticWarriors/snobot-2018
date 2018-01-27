package org.snobot.autonomous;

import java.io.File;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.lib.autonomous.ObservableSendableChooser;
import org.snobot.lib.autonomous.SnobotAutonCrawler;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousFactory
{
    private static final double sY_START = 336 - 3 * 12;

    protected ObservableSendableChooser<File> mAutonModeChooser;
    protected ObservableSendableChooser<StartingPositions> mPositionChooser;
    protected NetworkTable mAutoModeTable;

    protected CommandParser mCommandParser;

    protected IPositioner mPositioner;

    /**
     * Starting positions.
     * 
     * @author Owner
     *
     */
    // TODO make the starting positions accurate for this year
    public enum StartingPositions
    {
        RedLeft("Red Left", 0, -sY_START, 0), RedMiddle("Red Middle", 0, -sY_START, 0), RedRight("Red Right", 0, -sY_START, 0), BlueLeft("Blue Left",
                0, sY_START, 180), BlueMiddle("Blue Middle", 0, sY_START, 180), BlueRight("Blue Right", 0, sY_START, 180), Origin("Origin", 0, 0, 0);

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
        mPositionChooser = new ObservableSendableChooser<StartingPositions>();
        mCommandParser = new CommandParser(aSnobot, mPositionChooser);
        mAutoModeTable = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sAUTON_TABLE_NAME);

        mPositioner = aSnobot.getPositioner();

        mAutonModeChooser = new SnobotAutonCrawler(Properties2018.sAUTON_FILE_FILTER.getValue())
                .loadAutonFiles(Properties2018.sAUTON_DIRECTORY.getValue() + "/", Properties2018.sAUTON_DEFAULT_FILE.getValue());

        SmartDashboard.putData(SmartDashboardNames.sAUTON_CHOOSER, mAutonModeChooser);


        for (StartingPositions pos : StartingPositions.values())
        {
            mPositionChooser.addObject(pos.toString(), pos);
        }

        SmartDashboard.putData(SmartDashboardNames.sPOSITION_CHOOSER, mPositionChooser);
        addListeners();
    }

    /**
     * Create an autonomous mode.
     * 
     * @return The autonomous mode
     */
    public CommandGroup createAutonMode()
    {
        File selectedFile = mAutonModeChooser.getSelected();
        if (selectedFile != null)
        {
            setPosition();
            return mCommandParser.readFile(selectedFile.toString());
        }

        return null;

    }

    private void addListeners()
    {
        TableEntryListener buildAutonListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                createAutonMode();
            }
        };

        TableEntryListener setPositionListener = new TableEntryListener()
        {
            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                setPosition();
            }
        };

        TableEntryListener saveListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                if (mAutoModeTable.getEntry(SmartDashboardNames.sSAVE_AUTON).getBoolean(false))
                {
                    mCommandParser.saveAutonMode();
                }
            }
        };

        mAutoModeTable.addEntryListener(SmartDashboardNames.sSAVE_AUTON, saveListener, 0xFF);

        mPositionChooser.addSelectionChangedListener(setPositionListener);
        mAutonModeChooser.addSelectionChangedListener(buildAutonListener);
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
