package org.snobot.leds;

import java.nio.ByteBuffer;
import java.util.Arrays;

import edu.wpi.first.wpilibj.SPI;

public class DostarLedStrip implements IAddressableLedStrip
{
    private static final int sSPI_CLK_RATE = 13000000;
    private static final int sMAX_BYTES_PER_MESSAGE = 124; // Max size the hardware supports is 127.  Rounding down to an 4 byte boundary

    private static final int sBYTES_PER_LED = 4;
    private static final int sLED_ZERO_OFFSET = 4;

    private final int mBufferSize;

    private final int mColorOffsetRed;
    private final int mColorOffsetGreen; // Note: Green and blue are backwards
    private final int mColorOffsetBlue;

    private final SPI mSpi;
    private final ByteBuffer mDataBuffer;

    /**
     * Constructor.
     * 
     * @param aNumLeds
     *            The number of LEDs to talk to. Used to specify the magic word
     *            for "end of transmission"
     * @param aPort
     *            The SPI port this is connected to
     */
    public DostarLedStrip(int aNumLeds, SPI.Port aPort)
    {
        mBufferSize = aNumLeds * sBYTES_PER_LED + 4 + 4;
        mDataBuffer = ByteBuffer.allocate(mBufferSize);

        mColorOffsetRed = 3;
        mColorOffsetGreen = 1; // Note: Green and blue are backwards
        mColorOffsetBlue = 2;

        mSpi = new SPI(SPI.Port.kMXP);
        mSpi.setMSBFirst();
        mSpi.setClockActiveLow();
        mSpi.setClockRate(sSPI_CLK_RATE);
        mSpi.setSampleDataOnFalling();

        // Populate the start of the message
        for (int i = 0; i < sBYTES_PER_LED; ++i)
        {
            mDataBuffer.put(i, (byte) 0);
        }

        // Populate the start of the message
        int endStart = mBufferSize - sBYTES_PER_LED;
        for (int i = endStart; i < endStart + sBYTES_PER_LED; ++i)
        {
            mDataBuffer.put(i, (byte) 255);
        }
    }
    
    @Override
    public void updateStrip()
    {
        // Make local copy of ledBuffer to help make color changing clean
        byte[] bufferCopy = new byte[mBufferSize];
        mDataBuffer.position(0);
        mDataBuffer.get(bufferCopy);

        for (int offset = 0; offset < bufferCopy.length; offset = offset + sMAX_BYTES_PER_MESSAGE)
        {
            int startIndex = offset;
            int endIndex = Math.min(offset + sMAX_BYTES_PER_MESSAGE, bufferCopy.length);
            int size = endIndex - startIndex;
            byte[] txArray = Arrays.copyOfRange(bufferCopy, startIndex, endIndex);

            mSpi.write(txArray, size);
        }

    }

    @Override
    public void setColor(int aLedIndex, int aRed, int aGreen, int aBlue)
    {
        mDataBuffer.put(aLedIndex * sBYTES_PER_LED + sLED_ZERO_OFFSET + 0, (byte) 1);
        mDataBuffer.put(aLedIndex * sBYTES_PER_LED + sLED_ZERO_OFFSET + mColorOffsetRed, (byte) aRed);
        mDataBuffer.put(aLedIndex * sBYTES_PER_LED + sLED_ZERO_OFFSET + mColorOffsetGreen, (byte) aGreen);
        mDataBuffer.put(aLedIndex * sBYTES_PER_LED + sLED_ZERO_OFFSET + mColorOffsetBlue, (byte) aBlue);
    }
}
