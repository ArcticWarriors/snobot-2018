package org.snobot.autonomous;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snobot.Snobot2018;
import org.snobot.commands.StupidDriveStraight;

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
        mCommandCreatorMap.put(
                AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND, StupidDriveStraight::parseCommand);
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
        }
        else
        {
            newCommand = commandCreator.createCommand(aCommandArgs, aSnobot);
        }
        return newCommand;
    }
}
