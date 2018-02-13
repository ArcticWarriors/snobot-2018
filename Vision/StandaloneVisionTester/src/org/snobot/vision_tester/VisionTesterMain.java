package org.snobot.vision_tester;

import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.yaml.snakeyaml.Yaml;

public class VisionTesterMain
{
    @SuppressWarnings("unused")
    private void dumpConfig(TestConfig aConfig, String aDumpFile)
    {
        try (FileWriter stream = new FileWriter(aDumpFile))
        {
            Yaml yaml = new Yaml();
            yaml.dump(aConfig, stream);
        }
        catch (IOException ex)
        {
            ex.printStackTrace(); // NOPMD
        }
    }

    private TestConfig loadConfig(String aConfigFile)
    {
        TestConfig output = new TestConfig();
        
        try (FileInputStream stream = new FileInputStream(aConfigFile))
        {
            Yaml yaml = new Yaml();
            output = (TestConfig) yaml.load(stream);
        }
        catch (IOException ex)
        {
            ex.printStackTrace(); // NOPMD
        }

        return output;
    }

    private void testImages(String aConfigFile, boolean aOneAtATime) throws IOException
    {
        TestConfig config = loadConfig(aConfigFile);

        for (String file : config.mFiles)
        {
            BufferedImage image = ImageIO.read(new File(file));

            if (image.getWidth() > 1280)
            {
                double scaleFactor = 640.0 / image.getWidth();

                BufferedImage after = new BufferedImage((int) (scaleFactor * image.getWidth()), (int) (scaleFactor * image.getHeight()),
                        BufferedImage.TYPE_3BYTE_BGR);
                AffineTransform at = new AffineTransform();
                at.scale(scaleFactor, scaleFactor);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                after = scaleOp.filter(image, after);

                image = after;
            }

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

//         String configFile = "TestImages/20180210_random_cubes/cube_test.yml";
//       String configFile = "TestImages/20170209_last_years_tape_images/tape_test.yml";
        String configFile = "TestImages/20180210_kickoff_portal_blue/kickoff.yml";
//        String configFile = "TestImages/20180210_kickoff_portal_red/kickoff.yml";

        new VisionTesterMain().testImages(configFile, oneAtATime);
    }
}
