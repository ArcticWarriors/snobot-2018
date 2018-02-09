package org.snobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public final class RobotTypeDetector
{
    private static final Logger sLOGGER = Logger.getLogger(RobotTypeDetector.class);

    private static final String sMAGIC_FILE = Properties2018.sCTRE_FILE.getValue();

    private RobotTypeDetector()
    {

    }

    /**
     * Looks at a config file to determine if the robot should use the CTRE
     * only. mode
     * 
     * @return True if this is a CTRE robot
     */
    public static boolean isCan()
    {
        boolean isCan = true;

        File file = new File(sMAGIC_FILE);
        
        if (file.exists())
        {
            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                Properties properties = new Properties();
                properties.load(br);

                isCan = Boolean.parseBoolean(properties.getOrDefault("is_can", isCan).toString());
            }
            catch (Exception ex)
            {
                sLOGGER.log(Level.ERROR, ex);
            }
        }

        return isCan;
    }
}
