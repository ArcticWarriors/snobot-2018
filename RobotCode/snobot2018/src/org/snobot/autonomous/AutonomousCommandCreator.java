package org.snobot.autonomous;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.Snobot2018;
import org.snobot.autonomous.path.DriveStraightPath;
import org.snobot.autonomous.path.DriveStraightPathWithGyro;
import org.snobot.autonomous.path.DriveTurnPath;
import org.snobot.autonomous.trajectory.TrajectoryPathCommand;
import org.snobot.commands.ClawCommand;
import org.snobot.commands.GoToHeightCommand;
import org.snobot.commands.GoToXY;
import org.snobot.commands.StupidDriveStraight;
import org.snobot.commands.StupidGoToXY;
import org.snobot.commands.StupidTurn;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This class holds a map of all the autonomous command classes and allows
 * generic create of a command with just the command name.
 * 
 * @author Nora/Josh
 *
 */
public class AutonomousCommandCreator
{
    protected static final Logger sLOGGER = Logger.getLogger(AutonomousCommandCreator.class);

    /**
     * This is the map that holds all our autonomous command create references
     * Note: For each autonomous command in org.snobot.commands put a entry in
     * the map.
     */
    private final Map<String, ICommandCreator> mCommandCreatorMap;

    /**
     * CommandCreator Interface Reference All autonomous commands have to have a
     * create method with this format.
     */
    @FunctionalInterface
    public static interface ICommandCreator
    {
        Command createCommand(List<String> aArgs, Snobot2018 aSnobot);
    }

    /**
     * Constructor.
     */
    public AutonomousCommandCreator()
    {
        mCommandCreatorMap = new HashMap<String, AutonomousCommandCreator.ICommandCreator>();
        mCommandCreatorMap.put(AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND, StupidDriveStraight::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sTRAJECTORY_PATH_AUTONOMOUS, TrajectoryPathCommand::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sDRIVE_STRAIGHT_PATH, DriveStraightPath::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sDRIVE_STRAIGHT_PATH_WITH_GYRO, DriveStraightPathWithGyro::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sDRIVE_TURN_PATH, DriveTurnPath::parseCommand);
        mCommandCreatorMap.put(
                AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND, StupidDriveStraight::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sSTUPID_TURN_COMMAND, StupidTurn::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sSTUPID_GO_TO_XY_COMMAND, StupidGoToXY::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sGO_TO_XY_COMMAND, GoToXY::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sGO_TO_HEIGHT_COMMAND, GoToHeightCommand::parseCommand);
        mCommandCreatorMap.put(AutonomousCommandNames.sCLAW_COMMAND, ClawCommand::parseCommand);

    }

    /**
     * It returns a command object after looking it up in the map by the command
     * name and then calling create command on the reference in the map.
     * 
     * @param aCommandName
     *            The name of the command
     * @param aCommandArgs
     *            The list of commands that are in the command text file.
     * @returns the command for that name or null if i doesn't exist.
     */
    public Command createCommand(String aCommandName, List<String> aCommandArgs, Snobot2018 aSnobot)
    {
        Command newCommand;
        ICommandCreator commandCreator = mCommandCreatorMap.get(aCommandName);

        if (commandCreator == null)
        {
            newCommand = null;
            sLOGGER.log(Level.ERROR, "No creater registered for command '" + aCommandName + "'");
        }
        else
        {
            newCommand = commandCreator.createCommand(aCommandArgs, aSnobot);
        }
        return newCommand;
    }
}
