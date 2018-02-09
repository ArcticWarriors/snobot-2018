package com.snobot.vision_app.app2018.common;

import java.lang.annotation.Target;
import java.util.Comparator;

/**
 * Created by Andrew Johnson on 1/26/2018.
 */

public class TargetComparators
{
    public static class AspectRatioComparator implements Comparator<TargetLocation>
    {
        @Override
        public int compare(TargetLocation o1, TargetLocation o2)
        {
            double aspectRatioDifference_1 = Math.abs(0.4 - o1.getAspectRatio());
            double aspectRatioDifference_2 = Math.abs(0.4 - o2.getAspectRatio());

            if (aspectRatioDifference_1 > aspectRatioDifference_2)
                return 1;
            else
                return -1;
        }
    }

    public static class AngleComparator implements Comparator<TargetLocation>
    {
        @Override
        public int compare(TargetLocation o1, TargetLocation o2)
        {
            if (o1.getAngle() > o2.getAngle())
                return 1;
            else
                return -1;
        }
    }
}
