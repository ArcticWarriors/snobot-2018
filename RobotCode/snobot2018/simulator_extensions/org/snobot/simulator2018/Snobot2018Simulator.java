package org.snobot.simulator2018;

import org.snobot.PortMappings2018;
import org.snobot.RobotTypeDetector;

import com.snobot.simulator.ASimulator;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.jni.standard_components.SpiCallbackJni;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.robot_container.IRobotClassContainer;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Snobot2018Simulator extends ASimulator
{
    private final boolean mUseCan;

    public Snobot2018Simulator()
    {
        this(RobotTypeDetector.isCan());
    }

    /**
     * Constructor.
     * 
     * @param aUseCan
     *            If CTRE modules are being used. Used to specify which
     *            simulator config to read
     */
    public Snobot2018Simulator(boolean aUseCan)
    {
        mUseCan = aUseCan;

        SpiCallbackJni.setSpiFactory(new SnobotSpiFactory());
    }

    @Override
    public boolean loadConfig(String aConfigFile)
    {
        String actualConfigFile = null;
        if (mUseCan)
        {
            actualConfigFile = "simulator_config/simulator_config_ctre.yml";
        }
        else
        {
            actualConfigFile = "simulator_config/simulator_config_standard.yml";
        }

        return super.loadConfig(actualConfigFile);
    }

    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {
        super.setRobot(aRobot);

        SmartDashboard.putNumber("SIMULATOR.ModeSwitch", 2.5);
        SmartDashboard.putNumber("SIMULATOR.PositionSwitch", 2.5);
    }

    @Override
    public void update()
    {

        AnalogWrapper modeWrapper = SensorActuatorRegistry.get().getAnalog().get(PortMappings2018.sMODE_CHOOSER_SWITCH);
        AnalogWrapper positionWrapper = SensorActuatorRegistry.get().getAnalog().get(PortMappings2018.sPOSITION_CHOOSER_SWITCH);

        modeWrapper.setVoltage(SmartDashboard.getNumber("SIMULATOR.ModeSwitch", 2.5));
        positionWrapper.setVoltage(SmartDashboard.getNumber("SIMULATOR.PositionSwitch", 4));
    }
}
