package org.snobot.sd2018.robot;

import javax.swing.JFrame;

public final class StandaloneRobotDrawer
{
    // Private constructor to prevent instantiation
    private StandaloneRobotDrawer()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Main program.
     * 
     * @param aArgs
     *            Main command line arguments.
     */
    public static void main(String[] aArgs)
    {
        JFrame frame = new JFrame("Test Robot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RobotDrawer panel = new RobotDrawer();

        panel.setWinchMotorSpeed(.7);
        panel.setClawHeight(56);
        panel.setClawIsOpen(false);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

}
