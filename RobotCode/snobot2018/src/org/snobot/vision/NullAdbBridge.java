package org.snobot.vision;

import org.snobot.lib.adb.IAdbBridge;

public class NullAdbBridge implements IAdbBridge
{

    @Override
    public void start()
    {
        // Nothing to do
    }

    @Override
    public void stop()
    {
        // Nothing to do
    }

    @Override
    public void restartAdb()
    {
        // Nothing to do
    }

    @Override
    public void restartApp()
    {
        // Nothing to do
    }

    @Override
    public void portForward(int aLocalPort, int aRemotePort)
    {
        // Nothing to do
    }

    @Override
    public void reversePortForward(int aLocalPort, int aRemotePort)
    {
        // Nothing to do
    }

}
