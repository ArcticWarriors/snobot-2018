package org.snobot.nt.game_data;

import javax.swing.JFrame;

public class StandaloneGameDataPanelTester
{

    public static void main(String[] args)
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
