package org.snobot.drivetrain;

import org.snobot.PortMappings2018;
import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.CsvLogger;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class DrivetrainFactory
{

    /**
     * Creates a drivetrain.
     * 
     * @param aUseCan
     *            If it should create the CAN version
     * @param aJoystick
     *            The driver joystick
     * @param aLogger
     *            The logger
     * @return The appropriately created drivetrain
     */
    public IDriveTrain createDrivetrain(boolean aUseCan, IDriveJoystick aJoystick, CsvLogger aLogger)
    {
        SnobotADXRS450_Gyro gyro = new SnobotADXRS450_Gyro();

        if (aUseCan)
        {
            WPI_TalonSRX driveLeftMotorA = new WPI_TalonSRX(PortMappings2018.sDRIVE_CTRE_LEFT_A_PORT);
            WPI_TalonSRX driveLeftMotorB = new WPI_TalonSRX(PortMappings2018.sDRIVE_CTRE_LEFT_B_PORT);
            WPI_TalonSRX driveRightMotorA = new WPI_TalonSRX(PortMappings2018.sDRIVE_CTRE_RIGHT_A_PORT);
            WPI_TalonSRX driveRightMotorB = new WPI_TalonSRX(PortMappings2018.sDRIVE_CTRE_RIGHT_B_PORT);

            driveLeftMotorB.follow(driveLeftMotorA);
            driveRightMotorB.follow(driveRightMotorA);

            return new SnobotCanDriveTrain(driveLeftMotorA, driveRightMotorA, gyro, aJoystick, aLogger);
        }
        else
        {
            SpeedController driveLeftMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_LEFT_A_PORT);
            SpeedController driveRightMotor = new VictorSP(PortMappings2018.sDRIVE_PWM_RIGHT_A_PORT);
            Encoder leftDriveEncoder = new Encoder(PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_A, PortMappings2018.sLEFT_DRIVE_ENCODER_PORT_B);
            Encoder rightDriveEncoder = new Encoder(PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_A, PortMappings2018.sRIGHT_DRIVE_ENCODER_PORT_B);


            return new SnobotStandardDriveTrain(driveLeftMotor, driveRightMotor, leftDriveEncoder, rightDriveEncoder, gyro, aJoystick, aLogger);
        }
    }
}
