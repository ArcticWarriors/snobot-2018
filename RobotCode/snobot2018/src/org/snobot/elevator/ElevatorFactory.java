package org.snobot.elevator;

import org.snobot.PortMappings2018;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.ILogger;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class ElevatorFactory
{
    /**
     * Creates an elevator.
     * 
     * @param aUseCan
     *            True to create a CTRE based elevator
     * @param aJoystick
     *            The operator joystick
     * @param aLogger
     *            The logger
     * @return The created elevator
     */
    public IElevator createDrivetrain(boolean aUseCan, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        if (aUseCan)
        {
            WPI_TalonSRX elevatorMotoA = new WPI_TalonSRX(PortMappings2018.sELEVATOR_CTRE_A_PORT);
            WPI_TalonSRX elevatorMotoB = new WPI_TalonSRX(PortMappings2018.sELEVATOR_CTRE_B_PORT);

            elevatorMotoB.follow(elevatorMotoA);

            elevatorMotoA.setInverted(true);
            elevatorMotoB.setInverted(true);

            return new SnobotCtreElevator(elevatorMotoA, aJoystick, aLogger);
        }
        else
        {
            SpeedController elevatorMotor = new VictorSP(PortMappings2018.sELEVATOR_PWM_A_PORT);
            Encoder elevatorEncoder = new Encoder(PortMappings2018.sELEVATOR_ENCODER_PORT_A, PortMappings2018.sELEVATOR_ENCODER_PORT_B);

            return new SnobotStandardElevator(elevatorMotor, elevatorEncoder, aJoystick, aLogger);
        }
    }
}
