package org.snobot.nt.dotstar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DotstarSimulatorPanel extends JPanel
{
    private static final int sDEFAULT_PIXEL_SIZE = 5;

    private final List<Integer> mLedValues;
    private int mNumLeds;

    /**
     * Constructor.
     * 
     * @param aLedCount
     *            The expected number of LED's in the strip
     */
    public DotstarSimulatorPanel(int aLedCount)
    {
        setPreferredSize(new Dimension(sDEFAULT_PIXEL_SIZE * aLedCount, sDEFAULT_PIXEL_SIZE));
        mNumLeds = aLedCount;

        mLedValues = new ArrayList<>();
    }

    @Override
    public void paint(Graphics aGraphics)
    {
        int widthSizeFactor = getWidth() / mNumLeds;
        int heightSizeFactor = getHeight();

        int sizeFactor = Math.max(1, Math.min(widthSizeFactor, heightSizeFactor));

        aGraphics.setColor(Color.black);
        aGraphics.fillRect(0, 0, getWidth(), getHeight());
        aGraphics.setColor(Color.white);
        aGraphics.drawRect(0, 0, (int) (mNumLeds * sizeFactor), (int) sizeFactor);

        for (int i = 0; i < mLedValues.size(); ++i)
        {
            int ledValue = mLedValues.get(i);

            int r = ((ledValue & 0xFF000000) >> 24) & 0xFF;
            int b = (ledValue & 0x00FF0000) >> 16;
            int g = (ledValue & 0x0000FF00) >> 8; // Note: Blue is not in right order

            Color color = new Color(r, g, b);
            aGraphics.setColor(color);
            aGraphics.fillOval((int) (i * sizeFactor), 0, (int) sizeFactor, (int) sizeFactor);
        }
    }

    /**
     * Sets the values for the LED.
     * 
     * @param aValues
     *            The values
     */
    public void setValues(List<Integer> aValues)
    {
        mNumLeds = aValues.size();
        mLedValues.clear();
        mLedValues.addAll(aValues);
        repaint();
    }

}
