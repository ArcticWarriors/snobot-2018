package org.snobot.autonomous;

import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.lib.autonomous.ACommandParser;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Creates commands from a file path and adds them to a CommandGroup.
 * 
 * @author Nora/Josh
 *
 */
public class CommandParser extends ACommandParser<Snobot2018>
{
    /**
     * Variable that keeps track of the value of Switch triggers for each file parsed. 
     * Null if none seen
     * R if the parser sees R after "Switch"
     * L if the parser sees L after "Switch"
     */
    private String mSwitchTrigger;
    
    /**
     * Variable that keeps track of the value of Switch triggers for each file parsed. 
     * Null if none seen
     * R if the parser sees R after "Scale"
     * L if the parser sees L after "Scale"
     */
    private String mScaleTrigger;

    /**
     * Creates a CommandParser object.
     * 
     * @param aSnobot
     *            The robot using the CommandParser.
     */
    public CommandParser(Snobot2018 aSnobot, NetworkTable aNetworkTable)
    {
        super(
                aNetworkTable.getEntry(SmartDashboardNames.sROBOT_COMMAND_TEXT), 
                aNetworkTable.getEntry(SmartDashboardNames.sSUCCESFULLY_PARSED_AUTON),
                aNetworkTable.getEntry(SmartDashboardNames.sAUTON_FILENAME),
                " ", "#",
                AutonomousCommandNames.sWAIT_COMMAND,
                AutonomousCommandNames.sPARALLEL_COMMAND,
                new AutonomousCommandCreator(), 
                aSnobot);

        aNetworkTable.getEntry(".type").setString("AutonWidget");
    }

    protected void parseLine(CommandGroup aGroup, String aLine, boolean aAddParallel)
    {
        if (aLine.startsWith(AutonomousCommandNames.sSWITCH_TRIGGER_COMMAND))
        {
            mSwitchTrigger = String.valueOf(aLine.charAt(7));
        }
        else if (aLine.startsWith(AutonomousCommandNames.sSCALE_TRIGGER_COMMAND))
        {
            mScaleTrigger = String.valueOf(aLine.charAt(6));
        }
        else
        {
            super.parseLine(aGroup, aLine, aAddParallel);
        }
    }

    @Override
    public CommandGroup readFile(String aFilePath)
    {
        mSwitchTrigger = null;
        mScaleTrigger = null;

        return super.readFile(aFilePath);
    }

    /**
     * Gives the value of scale trigger word for a command- n,r, or l.
     * 
     * @return Switch trigger string
     */
    public String getScaleTrigger()
    {
        return mScaleTrigger;
    }

    /**
     * Gives the value of switch trigger word for a command- n,r, or l.
     * 
     * @return Switch trigger string
     */
    public String getSwitchTrigger()
    {
        return mSwitchTrigger;
    }
}
