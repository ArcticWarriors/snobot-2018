package org.snobot;

import org.snobot.autonomous.AutonomousFactory;
import org.snobot.lib.ASnobot;
import org.snobot.lib.logging.ILogger;
import org.snobot.positioner.IPositioner;
import org.snobot2018.drivetrain.IDriveTrain;
import org.snobot2018.drivetrain.SnobotDriveTrain;
import org.snobot2018.joystick.SnobotDriveXbaxJoystick;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Snobot2018 extends ASnobot
{
    // RoboSubsystems
    private IDriveTrain mDriveTrain;

    // Autonomous
    private AutonomousFactory mAutonFactory;

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

        // DriveTrain
        SpeedController driveLeftMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_LEFT_A_PORT);
        SpeedController driveRightMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_RIGHT_A_PORT);
        Encoder leftDriveEncoder = new Encoder(PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_A, PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_B);
        Encoder rightDriveEncoder = new Encoder(PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_A, PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_B);

        mDriveTrain = new SnobotDriveTrain(driveLeftMotor, driveRightMotor, leftDriveEncoder, rightDriveEncoder, driverJoystick, logger);

        addModule(mDriveTrain);

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
        // TODO Auto-generated method stub
        return null;
    }

}
