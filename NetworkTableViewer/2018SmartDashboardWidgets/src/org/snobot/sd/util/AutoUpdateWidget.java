package org.snobot.sd.util;

import edu.wpi.first.smartdashboard.gui.StaticWidget;

/**
 * Base class for a widget that updates periodically.
 * 
 * @author PJ
 */
public abstract class AutoUpdateWidget extends StaticWidget
{
    private static final long serialVersionUID = -5324757383577336302L;

    private final long mUpdateMs;

    /**
     * Constructor.
     * 
     * @param aUpdateMs
     *            The update period, in ms
     */
    public AutoUpdateWidget(long aUpdateMs)
    {
        mUpdateMs = aUpdateMs;

        BGThread bgThread = new BGThread();
        bgThread.start();
    }

    public class BGThread extends Thread
    {
        boolean mDestroyed = false;

        public BGThread()
        {
            super("Camera Background");
        }

        @Override
        public void run()
        {
            while (!mDestroyed)
            {
                try
                {
                    poll();
                    Thread.sleep(mUpdateMs);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace(); // NOPMD
                }
            }

        }

        @SuppressWarnings("deprecation")
        @Override
        public void destroy()
        {
            mDestroyed = true;
        }
    }

    protected abstract void poll() throws Exception;
}
