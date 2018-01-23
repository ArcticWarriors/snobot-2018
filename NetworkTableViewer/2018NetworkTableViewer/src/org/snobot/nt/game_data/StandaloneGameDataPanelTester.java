package org.snobot.nt.game_data;

import javax.swing.JFrame;

public class StandaloneGameDataPanelTester
{

    /**
     * Main runner.
     * 
     * @param aArgs
     *            Arguments
     */
    public static void main(String[] aArgs)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameDataContainer panel = new GameDataContainer();
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        panel.setPositionData("10", "red", "RRR");

    }
}
