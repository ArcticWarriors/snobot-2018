package org.snobot.nt.game_data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class GameDataPanel extends JPanel
{
    private static final Logger sLOGGER = Logger.getLogger(GameDataPanel.class);

    private Color mOurColor;
    private Color mTheirColor;
    private String mPositions;

    /**
     * Constructor.
     */
    public GameDataPanel()
    {
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.GREEN);

        mOurColor = Color.orange;
        mTheirColor = Color.white;
    }

    @Override
    public void paint(Graphics aGraphics)
    {
        if (mPositions == null || mPositions.length() != 3)
        {
            sLOGGER.log(Level.WARN, "Invalid positions string '" + mPositions + "'");
            return;
        }

        int boxWidths = getWidth() / 2;
        int boxHeights = getHeight() / 3;

        drawBox(aGraphics, mPositions.charAt(0), 0 * boxHeights, boxWidths, boxHeights);
        drawBox(aGraphics, mPositions.charAt(1), 1 * boxHeights, boxWidths, boxHeights);
        drawBox(aGraphics, mPositions.charAt(2), 2 * boxHeights, boxWidths, boxHeights);
    }

    private void drawBox(Graphics aGraphics, char aDirection, int aStartY, int aBoxWidth, int aBoxHeight)
    {
        aGraphics.setColor(getColor(aDirection == 'l'));
        aGraphics.fillRect(0, aStartY, aBoxWidth, aBoxHeight);
        aGraphics.setColor(Color.black);
        aGraphics.drawRect(0, aStartY, aBoxWidth, aBoxHeight);

        aGraphics.setColor(getColor(aDirection == 'r'));
        aGraphics.fillRect(aBoxWidth, aStartY, aBoxWidth, aBoxHeight);
        aGraphics.setColor(Color.black);
        aGraphics.drawRect(aBoxWidth, aStartY, aBoxWidth, aBoxHeight);
    }

    private Color getColor(boolean aIsOurs)
    {
        return aIsOurs ? mOurColor : mTheirColor;
    }

    public void setColors(Color aOurColor, Color aTheirColor)
    {
        mOurColor = aOurColor;
        mTheirColor = aTheirColor;
    }

    /**
     * Sets the position data.
     * 
     * @param aData
     *            The data
     */
    public void setPositionData(String aData)
    {
        boolean repaint = !aData.equals(mPositions);
        mPositions = aData;

        if (repaint)
        {
            repaint();
        }
    }
}
