package com.snobot.vision_app.app2018;

import java.util.Comparator;

/**
 * Created by Andrew Johnson on 1/26/2018.
 */

public final class TargetComparators // NOPMD
{
    private TargetComparators()
    {

    }

    public static class AspectRatioComparator implements Comparator<TargetLocation>
    {
        @Override
        public int compare(TargetLocation aTarget1, TargetLocation aTarget2)
        {
            double aspectRatioDifference1 = Math.abs(0.4 - aTarget1.getAspectRatio());
            double aspectRatioDifference2 = Math.abs(0.4 - aTarget2.getAspectRatio());

            if (aspectRatioDifference1 > aspectRatioDifference2)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }

    public static class AngleComparator implements Comparator<TargetLocation>
    {
        @Override
        public int compare(TargetLocation aTarget1, TargetLocation aTarget2)
        {
            if (aTarget1.getAngle() > aTarget2.getAngle())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }
}
