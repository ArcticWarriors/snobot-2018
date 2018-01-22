package org.snobot.nt.game_data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class GameDataPanel extends JPanel
{
    private Color mOurColor;
    private Color mTheirColor;
    private String mPositions;

    public GameDataPanel()
    {
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.GREEN);

        mOurColor = Color.orange;
        mTheirColor = Color.white;
    }

    @Override
    public void paint(Graphics g)
    {
        if (mPositions == null || mPositions.length() != 3)
        {
            System.out.println("Invalid positions string '" + mPositions + "'");
            return;
        }

        int boxWidths = getWidth() / 2;
        int boxHeights = getHeight() / 3;

        drawBox(g, mPositions.charAt(0), 0 * boxHeights, boxWidths, boxHeights);
        drawBox(g, mPositions.charAt(1), 1 * boxHeights, boxWidths, boxHeights);
        drawBox(g, mPositions.charAt(2), 2 * boxHeights, boxWidths, boxHeights);
    }

    private void drawBox(Graphics g, char direction, int startY, int boxWidth, int boxHeight)
    {
        g.setColor(getColor(direction == 'l'));
        g.fillRect(0, startY, boxWidth, boxHeight);
        g.setColor(Color.black);
        g.drawRect(0, startY, boxWidth, boxHeight);

        g.setColor(getColor(direction == 'r'));
        g.fillRect(boxWidth, startY, boxWidth, boxHeight);
        g.setColor(Color.black);
        g.drawRect(boxWidth, startY, boxWidth, boxHeight);
    }

    private Color getColor(boolean isOurs)
    {
        return isOurs ? mOurColor : mTheirColor;
    }

    public void setColors(Color aOurColor, Color aTheirColor)
    {
        mOurColor = aOurColor;
        mTheirColor = aTheirColor;
    }

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
