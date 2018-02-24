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

}
