package org.snobot.claw;

import org.snobot.lib.modules.ISubsystem;

public interface IClaw extends ISubsystem
{
    /**
     * Opens claw.
     */
    void open();
    
    /**
     * Closes claw.
     */
    void close();
}
