package org.snobot.vision_tester;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.snobot.vision_tester.java_algorithm.StandaloneVisionAlgorithm;
import org.snobot.vision_tester.utils.OpenCvUtilities;

public class VisionTesterBatcherMain
{
    private static final Logger sLOGGER = Logger.getLogger(VisionTesterBatcherMain.class);

    private void testImages(String aConfigFile, String aDumpDirectory, boolean aOneAtATime) throws IOException
    {
        TestConfig config = TestConfig.loadConfig(aConfigFile);

        StandaloneVisionAlgorithm visionAlgorithm = new StandaloneVisionAlgorithm(config);

        File dumpDirectory = new File(aDumpDirectory, new File(aConfigFile).getParent());

        if (!dumpDirectory.exists())
        {
            dumpDirectory.mkdirs();
        }

        long startTime = System.currentTimeMillis();
        for (String file : config.mFiles)
        {
            File imageFile = new File(file);
            sLOGGER.log(Level.DEBUG, "    processing " + imageFile.getAbsolutePath());
            BufferedImage image = ImageIO.read(imageFile);
            image = OpenCvUtilities.resizeImage(image);

            BufferedImage output = OpenCvUtilities.matToImage(visionAlgorithm.processImage(image));

            ImageIO.write(output, "jpg", new File(dumpDirectory, imageFile.getName()));
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) * 1e-3;
        double framesPerSecond = config.mFiles.size() / timeTaken;

        sLOGGER.log(Level.INFO, "  Took " + timeTaken + " to process " + config.mFiles.size() + " (" + framesPerSecond + " FPS)");
    }

    /**
     * Runner for the vision test.
     * 
     * @param aArgs
     *            Command line arguments
     * @throws IOException
     *             Any exceptions will be bubbled up
     */
    public static void main(String[] aArgs)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        boolean oneAtATime = true;

        List<String> configFiles = new ArrayList<>();

        // configFiles.add("TestImages/20180210_random_cubes/cube_test.yml");
        // configFiles.add("TestImages/20170209_last_years_tape_images/tape_test.yml");
        // configFiles.add("TestImages/20180210_kickoff_portal_blue/kickoff.yml");
        // configFiles.add("TestImages/20180210_kickoff_portal_red/kickoff.yml");
        // configFiles.add("TestImages/20180210_kickoff_portal_red/kickoff.yml");
        // configFiles.add("TestImages/MacAndCheese/macncheese.yml");
        configFiles.add("TestImages/CokeTest/coke.yml");
        // configFiles.add("TestImages/TensorflowExample/tf_example.yml");

        try
        {
            for (String configFile : configFiles)
            {
                sLOGGER.log(Level.INFO, "Running test " + configFile);
                new VisionTesterBatcherMain().testImages(configFile, "image_dump", oneAtATime);
            }
        }
        catch (Exception ex)
        {
            sLOGGER.log(Level.ERROR, ex);
        }

    }
}
