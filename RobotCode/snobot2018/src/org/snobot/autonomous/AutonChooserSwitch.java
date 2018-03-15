package org.snobot.autonomous;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.snobot.Properties2018;

import edu.wpi.first.wpilibj.AnalogInput;

public class AutonChooserSwitch
{
    private final AnalogInput mAnalogInput;
    private final TreeMap<Double, Integer> mVoltageToModeMapping;

    /**
     * Chooses the Autonomous Mode with the Switch.
     * 
     * @param aNumberOfPositions
     *            The Number of Positions on the Switch
     * @param aAnalogPort
     *            The Analog Ports
     */
    public AutonChooserSwitch(int aNumberOfPositions, int aAnalogPort)
    {
        mAnalogInput = new AnalogInput(aAnalogPort);
        mVoltageToModeMapping = new TreeMap<>();

        double minVoltage = Properties2018.sMIN_VOLTAGE.getValue();
        double maxVoltage = Properties2018.sMAX_VOLTAGE.getValue();

        double stepSize = (maxVoltage - minVoltage) / aNumberOfPositions;

        int mode = 0;
        for (double voltage = minVoltage; voltage < maxVoltage; voltage += stepSize)
        {
            mVoltageToModeMapping.put(voltage, mode);
            ++mode;
        }
    }

    /**
     * Obtains the position of the switch.
     * 
     * @return the interval
     */
    public int getPosition()
    {
        double voltage = mAnalogInput.getVoltage();
        Entry<Double, Integer> pair = mVoltageToModeMapping.lowerEntry(voltage);

        if (pair != null)
        {
            return pair.getValue();
        }

        return Properties2018.sDEFAULT_SWITCH_POSITION.getValue();
    }
}
