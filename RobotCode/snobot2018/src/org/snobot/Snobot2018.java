package org.snobot;

import org.snobot.autonomous.AutonomousFactory;
import org.snobot.elevator.IElevator;
import org.snobot.elevator.SnobotElevator;
import org.snobot.lib.ASnobot;
import org.snobot.lib.logging.ILogger;
import org.snobot.positioner.IPositioner;
import org.snobot2018.drivetrain.IDriveTrain;
import org.snobot2018.drivetrain.SnobotDriveTrain;
import org.snobot2018.joystick.SnobotDriveOperatorJoystick;
import org.snobot2018.joystick.SnobotDriveXbaxJoystick;
import org.snobot2018.positioner.Positioner;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Snobot2018 extends ASnobot
{
    // RoboSubsystems
    private IDriveTrain mDriveTrain;
    private IElevator mElevator;

    // Autonomous
    private AutonomousFactory mAutonFactory;

    // Positioner
    private Positioner mPositioner;

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

        // DriveTrain
        SpeedController driveLeftMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_LEFT_A_PORT);
        SpeedController driveRightMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_RIGHT_A_PORT);
        Encoder leftDriveEncoder = new Encoder(PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_A, PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_B);
        Encoder rightDriveEncoder = new Encoder(PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_A, PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_B);
        Gyro gyro = new ADXRS450_Gyro();

        mDriveTrain = new SnobotDriveTrain(driveLeftMotor, driveRightMotor, leftDriveEncoder, rightDriveEncoder, driverJoystick, logger, gyro);
        addModule(mDriveTrain);

        // Elevator
        SpeedController elevatorMotor = new VictorSP(PortMappings2018.sELEVATOR_PWM_A_PORT);
        Encoder elevatorEncoder = new Encoder(PortMappings2018.sELEVATOR_ENCODER_PORT_A, PortMappings2018.sELEVATOR_ENCODER_PORT_B);

        mElevator = new SnobotElevator(elevatorMotor, elevatorEncoder, operatorJoystick, logger);
        addModule(mElevator);

        // Position
        mPositioner = new Positioner(mDriveTrain, logger);
        addModule(mPositioner);

        // This should be done last
        mAutonFactory = new AutonomousFactory(this);
    }


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

}
