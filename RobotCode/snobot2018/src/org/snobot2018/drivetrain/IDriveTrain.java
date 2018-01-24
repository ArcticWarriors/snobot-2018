package org.snobot2018.drivetrain;

import org.snobot.lib.modules.ISubsystem;

public interface IDriveTrain extends ISubsystem
{

    double getRightDistance();

    double getLeftDistance();

    void setLeftRightSpeed(double aLeftSpeed, double aRightSpeed);

    double getLeftMotorSpeed();

    double getRightMotorSpeed();

    void restEncoders();

}
