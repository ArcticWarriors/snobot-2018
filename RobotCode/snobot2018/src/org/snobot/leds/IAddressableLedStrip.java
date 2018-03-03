package org.snobot.leds;

public interface IAddressableLedStrip
{

    /**
     * Updates the LED strip.
     */
    void updateStrip();

    /**
     * Sets an individual LED's color.
     * 
     * @param aLedIndex
     *            The index of the LED
     * @param aRed
     *            The red component, 0-255
     * @param aGreen
     *            The green component, 0-255
     * @param aBlue
     *            The blue component, 0-255
     */
    void setColor(int aLedIndex, int aRed, int aGreen, int aBlue);

    /**
     * Sets the color, with HSL values. Converts them to RGB and calls
     * {@link #setColor(int, int, int, int)}
     * 
     * @param aLedIndex
     *            The LED index
     * @param aHue
     *            The Hue
     * @param aSaturation
     *            The Saturation
     * @param aLuminance
     *            The luminance
     */
    default void setLEDColorHSL(int aLedIndex, double aHue, double aSaturation, double aLuminance)
    {
        double r;
        double g;
        double b;

        if (aSaturation == 0f)
        {
            r = g = b = aLuminance; // achromatic
        }
        else
        {
            double q = aLuminance < 0.5f ? aLuminance * (1 + aSaturation) : aLuminance + aSaturation - aLuminance * aSaturation;
            double p = 2 * aLuminance - q;
            r = hueToRgb(p, q, aHue + 1f / 3f);
            g = hueToRgb(p, q, aHue);
            b = hueToRgb(p, q, aHue - 1f / 3f);
        }

        setColor(aLedIndex, (int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    /**
     * Converts an hue to the corresponding RGB value.
     * 
     * @param aP
     *            p
     * @param aQ
     *            q
     * @param aT
     *            t
     * @return The RGB
     */
    public static double hueToRgb(double aP, double aQ, double aT) // NOPMD
    {
        if (aT < 0f)
        {
            aT += 1f;
        }
        if (aT > 1f)
        {
            aT -= 1f;
        }
        if (aT < 1f / 6f)
        {
            return aP + (aQ - aP) * 6f * aT;
        }
        if (aT < 1f / 2f)
        {
            return aQ;
        }
        if (aT < 2f / 3f)
        {
            return aP + (aQ - aP) * (2f / 3f - aT) * 6f;
        }
        return aP;
    }

}
