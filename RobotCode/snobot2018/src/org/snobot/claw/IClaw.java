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

    /**
     * return if it is open.
     *
     * @return true if it is open and false if it is closed.
     */
    boolean isOpen();
}
