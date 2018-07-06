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
    private static final int sDEFAULT_SEPERATION = 50;

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
        setPreferredSize(new Dimension(sDEFAULT_PIXEL_SIZE * 2 + sDEFAULT_SEPERATION, sDEFAULT_PIXEL_SIZE * aLedCount / 2));
        mNumLeds = aLedCount;

        mLedValues = new ArrayList<>();
    }

    @Override
    public void paint(Graphics aGraphics)
    {
        int widthSizeFactor = (getWidth() - sDEFAULT_SEPERATION) / 2;
        int heightSizeFactor = getHeight() / (mNumLeds / 2);

        int sizeFactor = Math.max(1, Math.min(widthSizeFactor, heightSizeFactor));

        aGraphics.setColor(Color.black);
        aGraphics.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mLedValues.size(); ++i)
        {
            int ledValue = mLedValues.get(i);

            int r = ((ledValue & 0xFF000000) >> 24) & 0xFF;
            int g = (ledValue & 0x00FF0000) >> 16;
            int b = (ledValue & 0x0000FF00) >> 8; // Note: Blue is not in right
                                                  // order

            Color color = new Color(r, g, b);
            aGraphics.setColor(color);

            int x;
            int y;
            
            if (i < mNumLeds / 2)
            {
                x = 0;
                y = i * sizeFactor;
            }
            else
            {
                x = sDEFAULT_SEPERATION + sizeFactor;
                y = (mNumLeds - i - 1) * sizeFactor;
            }
            

            aGraphics.fillOval(x, y, (int) sizeFactor, (int) sizeFactor);
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
