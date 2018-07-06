package org.snobot.autonomous;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.snobot.commands.WaitThenGoToHeight;
import org.snobot.lib.autonomous.AAutonomousCommandFactory;

/**
 * This class holds a map of all the autonomous command classes and allows
 * generic create of a command with just the command name.
 * 
 * @author Nora/Josh
 *
 */
public class AutonomousCommandCreator extends AAutonomousCommandFactory<Snobot2018>
{
    protected static final Logger sLOGGER = LogManager.getLogger(AutonomousCommandCreator.class);

    /**
     * Constructor.
     */
    public AutonomousCommandCreator()
    {
        registerCreator(AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND, StupidDriveStraight::parseCommand);
        registerCreator(AutonomousCommandNames.sTRAJECTORY_PATH_AUTONOMOUS, TrajectoryPathCommand::parseCommand);
        registerCreator(AutonomousCommandNames.sDRIVE_STRAIGHT_PATH, DriveStraightPath::parseCommand);
        registerCreator(AutonomousCommandNames.sDRIVE_STRAIGHT_PATH_WITH_GYRO, DriveStraightPathWithGyro::parseCommand);
        registerCreator(AutonomousCommandNames.sDRIVE_TURN_PATH, DriveTurnPath::parseCommand);
        registerCreator(AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND, StupidDriveStraight::parseCommand);
        registerCreator(AutonomousCommandNames.sSTUPID_TURN_COMMAND, StupidTurn::parseCommand);
        registerCreator(AutonomousCommandNames.sSTUPID_GO_TO_XY_COMMAND, StupidGoToXY::parseCommand);
        registerCreator(AutonomousCommandNames.sGO_TO_XY_COMMAND, GoToXY::parseCommand);
        registerCreator(AutonomousCommandNames.sGO_TO_HEIGHT_COMMAND, GoToHeightCommand::parseCommand);
        registerCreator(AutonomousCommandNames.sCLAW_COMMAND, ClawCommand::parseCommand);
        registerCreator(AutonomousCommandNames.sWAIT_THEN_GO_TO_HEIGHT, WaitThenGoToHeight::parseCommand);
    }
}
