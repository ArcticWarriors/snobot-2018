package org.snobot.nt.dotstar;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public final class StandaloneDotstartPanelTester
{
    private StandaloneDotstartPanelTester()
    {

    }

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

        DotstarSimulatorPanel panel = new DotstarSimulatorPanel(19 * 2);
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < 144 / 3; ++i)
        {
            values.add(0xFF000000);
            values.add(0x00FF0000);
            values.add(0x0000FF00);
        }

        panel.setValues(values);

    }
}
