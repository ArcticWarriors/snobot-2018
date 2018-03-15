package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.elevator.IElevator;
import org.snobot.joystick.SnobotDriveOperatorJoystick.ElevatorHeights;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class WaitThenGoToHeight
{
    
    public static CommandGroup parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double height;
        CommandGroup returnGroup = new CommandGroup();
        try
        {
            height = ElevatorHeights.valueOf(aArgs.get(2)).mHeight;
        }
        catch (Exception ex)
        {
            height = Double.parseDouble(aArgs.get(2));
        }
        
        IElevator returnElevator = aSnobot.getElevator();
        Command goToHeight = new GoToHeightCommand(height, returnElevator);
        Command waitCommand = new WaitCommand(Double.parseDouble(aArgs.get(1)));

        returnGroup.addSequential(waitCommand);
        returnGroup.addSequential(goToHeight);
        return returnGroup;
    }
}
