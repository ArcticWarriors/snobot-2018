package org.snobot;

import org.snobot.autonomous.AutonomousFactory;
import org.snobot.claw.IClaw;
import org.snobot.claw.SnobotClaw;
import org.snobot.drivetrain.DrivetrainFactory;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.elevator.ElevatorFactory;
import org.snobot.elevator.IElevator;
import org.snobot.joystick.SnobotDriveOperatorJoystick;
import org.snobot.joystick.SnobotDriveXbaxJoystick;
import org.snobot.leds.ILedManager;
import org.snobot.leds.SnobotLedManager;
import org.snobot.lib.ASnobot;
import org.snobot.lib.logging.ILogger;
import org.snobot.positioner.IPositioner;
import org.snobot.positioner.Positioner;
import org.snobot.winch.IWinch;
import org.snobot.winch.WinchFactory;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Snobot2018 extends ASnobot
{
    private final boolean mUseCan;

    // RoboSubsystems
    private IDriveTrain mDriveTrain;
    private IElevator mElevator;
    private IWinch mWinch;
    private IClaw mClaw;

    // Autonomous
    private AutonomousFactory mAutonFactory;

    // Positioner
    private Positioner mPositioner;

    public Snobot2018()
    {
        this(RobotTypeDetector.isCan());
    }

    public Snobot2018(boolean aUseCan)
    {
        mUseCan = aUseCan;
    }

    /**
     * This is where all of the different subsystems are brought together so
     * that they work.
     */
    public void robotInit()
    {
        ILogger logger = getLogger();

        // Joystick
        Joystick driverJoystickRaw = new Joystick(PortMappings2018.sDRIVER_JOYSTICK_PORT);
        SnobotDriveXbaxJoystick driverJoystick = new SnobotDriveXbaxJoystick(driverJoystickRaw, logger);
        addModule(driverJoystick);

        Joystick elevatorJoystickRaw = new Joystick(PortMappings2018.sELEVATOR_JOYSTICK_PORT);
        SnobotDriveOperatorJoystick operatorJoystick = new SnobotDriveOperatorJoystick(elevatorJoystickRaw, logger);
        addModule(operatorJoystick);

        // LEDs
        ILedManager ledManager = new SnobotLedManager();
        addModule(ledManager);

        // DriveTrain
        DrivetrainFactory drivetrainFactory = new DrivetrainFactory();
        mDriveTrain = drivetrainFactory.createDrivetrain(mUseCan, driverJoystick, logger);
        addModule(mDriveTrain);

        // Elevator
        ElevatorFactory elevatorFactory = new ElevatorFactory();
        mElevator = elevatorFactory.createDrivetrain(mUseCan, operatorJoystick, logger);
        addModule(mElevator);
        
        // Claw
        DoubleSolenoid clawSolenoid = new DoubleSolenoid(PortMappings2018.sCLAW_FORWARD, PortMappings2018.sCLAW_REVERSE);
        mClaw = new SnobotClaw(clawSolenoid, logger, operatorJoystick);
        addModule(mClaw);


        // Winch
        WinchFactory winchFactory = new WinchFactory();
        mWinch = winchFactory.createWinch(mUseCan, operatorJoystick, logger);
        addModule(mWinch);

        // Position
        mPositioner = new Positioner(mDriveTrain, logger);
        addModule(mPositioner);


        // This should be done last
        mAutonFactory = new AutonomousFactory(this);

        // initialize the default auton command
        mAutonFactory.createAutonMode();
    }

    @Override
    protected CommandGroup createAutonomousCommand()
    {
        return mAutonFactory.createAutonMode();
    }

    public IDriveTrain getDrivetrain()
    {
        return mDriveTrain;
    }

    public IPositioner getPositioner()
    {
        return mPositioner;
    }

    public IElevator getElevator()
    {
        return mElevator;
    }

    public IWinch getWinch()
    {
        return mWinch;
    }

    public IClaw getClaw()
    {
        return mClaw;
    }

}
