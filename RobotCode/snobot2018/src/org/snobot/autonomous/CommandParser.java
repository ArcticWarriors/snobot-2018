package org.snobot.autonomous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Level;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.lib.autonomous.ACommandParser;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Creates commands from a file path and adds them to a CommandGroup.
 * 
 * @author Nora/Josh
 *
 */
public class CommandParser extends ACommandParser
{
    private final AutonomousCommandCreator mCommandCreator = new AutonomousCommandCreator();
    protected final Snobot2018 mSnobot;
    protected final NetworkTableEntry mAutonFilenameEntry;
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
        super(aNetworkTable.getEntry(SmartDashboardNames.sROBOT_COMMAND_TEXT), aNetworkTable.getEntry(SmartDashboardNames.sSUCCESFULLY_PARSED_AUTON),
                " ", "#");

        aNetworkTable.getEntry(".type").setString("AutonWidget");

        mSnobot = aSnobot;

        mAutonFilenameEntry = aNetworkTable.getEntry(SmartDashboardNames.sAUTON_FILENAME);

    }

    /**
     * Takes a list of Strings and creates a Command.
     * 
     * @param args
     *            The command's name and parameters.
     */
    @Override
    protected Command parseCommand(List<String> aCommandArgs)
    {
        String commandName = aCommandArgs.get(0);
        Command newCommand = null;
        try
        {
            // Generic from base class
            switch (commandName)
            {
            case AutonomousCommandNames.sPARALLEL_COMMAND:
            {
                newCommand = parseParallelCommand(aCommandArgs);
                break;
            }
            case AutonomousCommandNames.sWAIT_COMMAND:
            {
                newCommand = parseWaitCommand(aCommandArgs);
                break;
            }
            default:
            {
                // Subsystems from AuotonomousoCommandCreator
                newCommand = mCommandCreator.createCommand(commandName, aCommandArgs, mSnobot);
                if (null == newCommand)
                {
                    addError("Received unexpected command name '" + commandName + "'");
                }
                break;
            }
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            addError("You have not specified enough aguments for the command: " + commandName + ". " + ex.getMessage());
        }
        catch (Exception ex)
        {
            addError("Failed to parse the command: " + commandName + ". " + ex.getMessage());
            sLOGGER.log(Level.ERROR, "Failed to parse the command: " + commandName + ". ", ex);
        }
        return newCommand;
    }

    protected Command parseWaitCommand(List<String> aArgs)
    {
        double time = Double.parseDouble(aArgs.get(1));
        return new WaitCommand(time);
    }

    @Override
    public CommandGroup readFile(String aFilePath)
    {
        mSwitchTrigger = null;
        mScaleTrigger = null;
        mAutonFilenameEntry.setString(aFilePath);

        initReading();

        CommandGroup output = createNewCommandGroup(aFilePath);

        StringBuilder fileContents = new StringBuilder();

        File file = new File(aFilePath);

        if (file.exists())
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(aFilePath));

                String line;
                while ((line = br.readLine()) != null)
                {
                    if (line.startsWith(AutonomousCommandNames.sSWITCH_TRIGGER_COMMAND))
                    {
                        mSwitchTrigger = String.valueOf(line.charAt(7));
                    }
                    else if (line.startsWith(AutonomousCommandNames.sSCALE_TRIGGER_COMMAND))
                    {
                        mScaleTrigger = String.valueOf(line.charAt(6));
                    }
                    else
                    {
                        this.parseLine(output, line, false);
                        fileContents.append(line).append('\n');
                    }
                }

                br.close();
            }
            catch (Exception ex)
            {
                sLOGGER.log(Level.ERROR, "", ex);
            }
        }
        else
        {
            addError("File " + aFilePath + " not found!");
        }

        publishParsingResults(fileContents.toString());

        return output;
    }

    /**
     * Saves the autonomous mode to the RoboRio.
     */
    public void saveAutonMode()
    {
        String newText = mAutonSdTableTextName.getString("");
        String filename = mAutonFilenameEntry.getString("auton_file.txt");

        sLOGGER.log(Level.INFO, "\n" 
                + "*****************************************\n" 
                + "Saving auton mode to " + filename + "\n"
                + "*****************************************");

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
            bw.write(newText);
            bw.close();
        }
        catch (Exception ex)
        {
            sLOGGER.log(Level.INFO, ex);
        }
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
