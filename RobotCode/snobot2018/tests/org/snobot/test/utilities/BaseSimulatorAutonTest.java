package org.snobot.test.utilities;

import org.snobot.autonomous.CommandParser;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class BaseSimulatorAutonTest extends BaseSimulatorTest
{
    /**
     * Constructor.
     * 
     * @param aUseCan
     *            If CTRE tools are being used. Used to create the simulator and
     *            robot
     */
    public BaseSimulatorAutonTest(boolean aUseCan)
    {
        super(aUseCan);
    }

    protected final CommandGroup setupCommand(String aAutonTestFile)
    {
        NetworkTable table = NetworkTableInstance.create().getTable("Test");
        CommandParser parser = new CommandParser(mSnobot, table);

        CommandGroup command = parser.readFile(aAutonTestFile);
        command.start();

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setDisabled(false);

        return command;
    }
}
