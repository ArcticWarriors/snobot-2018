package org.snobot.simulator2018;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

import com.snobot.simulator.jni.standard_components.SpiCallbackJni;
import com.snobot.simulator.simulator_components.ISpiWrapper;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DotstarSimulator implements ISpiWrapper
{
    protected final NetworkTableEntry mNetworkTableEntry;
    protected final List<Integer> mValueBuffer;

    /**
     * Constructor.
     * 
     * @param aPort
     *            The SPI port this is plugged in to
     */
    public DotstarSimulator(int aPort)
    {
        SpiCallbackJni.registerSpiReadWriteCallback(aPort, this);

        NetworkTableInstance.getDefault().getTable("LedSimulator").getEntry(".type").setString("LedSimulator");
        mNetworkTableEntry = NetworkTableInstance.getDefault().getTable("LedSimulator").getEntry("Values");
        mValueBuffer = new LinkedList<>();
    }

    @Override
    public void handleRead(ByteBuffer aBuffer)
    {
        // Nothing to do
    }

    @Override
    public void handleWrite(ByteBuffer aBuffer)
    {
        // arg0.order(ByteOrder.BIG_ENDIAN);
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int firstNumber = aBuffer.getInt();
        
        if (firstNumber == 0)
        {
            StringBuffer buffer = new StringBuffer(mValueBuffer.size() * 8);

            for (int value : mValueBuffer)
            {
                buffer.append(Integer.toString(value)).append(',');
            }

            mNetworkTableEntry.setString(buffer.toString());
            mValueBuffer.clear();
        }
        else
        {
            mValueBuffer.add(firstNumber);
        }
        
        for (int i = 1; i < aBuffer.capacity() / 4; ++i)
        {
            mValueBuffer.add(aBuffer.getInt());
        }
    }

    @Override
    public void shutdown()
    {
        // Nothing to do
    }


}
