package org.snobot.vision_tester;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.snobot.vision_tester.utils.OpenCvUtilities;

public class VisionTesterMain
{

    private void testImages(String aConfigFile, boolean aOneAtATime) throws IOException
    {
        TestConfig config = TestConfig.loadConfig(aConfigFile);

        for (String file : config.mFiles)
        {
            BufferedImage image = ImageIO.read(new File(file));
            image = OpenCvUtilities.resizeImage(image);

            VisionTestPanel testPanel = new VisionTestPanel(config);
            testPanel.processImage(image);

            if (aOneAtATime)
            {
                JDialog frame = new JDialog();
                frame.setTitle(file);
                frame.setModal(true);
                frame.setLayout(new BorderLayout());
                frame.add(testPanel, BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
            else
            {
                JFrame frame = new JFrame();
                frame.setTitle(file);
                frame.setLayout(new BorderLayout());
                frame.add(testPanel, BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        }
    }

    /**
     * Runner for the vision test.
     * 
     * @param aArgs
     *            Command line arguments
     * @throws IOException
     *             Any exceptions will be bubbled up
     */
    public static void main(String[] aArgs) throws IOException
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        boolean oneAtATime = true;

        // String configFile = "TestImages/20180210_random_cubes/cube_test.yml";
        // String configFile = "TestImages/20170209_last_years_tape_images/tape_test.yml";
        // String configFile = TestImages/20180210_kickoff_portal_blue/kickoff.yml";
        // String configFile = "TestImages/20180210_kickoff_portal_red/kickoff.yml";
        // String configFile = "TestImages/20180210_kickoff_portal_red/kickoff.yml";
        // String configFile = "TestImages/MacAndCheese/macncheese.yml";
        String configFile = "TestImages/CokeTest/coke.yml";
        // String configFile = "TestImages/TensorflowExample/tf_example.yml";

        new VisionTesterMain().testImages(configFile, oneAtATime);
    }
}
