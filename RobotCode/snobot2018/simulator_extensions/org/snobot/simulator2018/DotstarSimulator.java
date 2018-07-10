package org.snobot.simulator2018;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

import com.snobot.simulator.module_wrapper.ASensorWrapper;
import com.snobot.simulator.module_wrapper.interfaces.ISpiWrapper;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.sim.ConstBufferCallback;
import edu.wpi.first.wpilibj.sim.SPISim;

public class DotstarSimulator extends ASensorWrapper implements ISpiWrapper, ConstBufferCallback
{
    protected final SPISim mWpiWrapper;
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
        super("Dotstar");

        mWpiWrapper = new SPISim(aPort);
        mWpiWrapper.registerWriteCallback(this);

        NetworkTableInstance.getDefault().getTable("LedSimulator").getEntry(".type").setString("LedSimulator");
        mNetworkTableEntry = NetworkTableInstance.getDefault().getTable("LedSimulator").getEntry("Values");
        mValueBuffer = new LinkedList<>();
    }

    private void translateWrite(ByteBuffer aBuffer)
    {
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
    public void callback(String aName, ByteBuffer aBuffer)
    {
        if ("Write".equals(aName))
        {
            translateWrite(aBuffer);
        }
    }


}
