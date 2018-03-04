package org.snobot.vision;

import org.snobot.Snobot2018;
import org.snobot.lib.modules.ISmartDashboardUpdaterModule;
import org.snobot.lib.modules.IUpdateableModule;

import com.snobot.vision_app.app2018.messages.TargetUpdateMessage;

import edu.wpi.first.wpilibj.DriverStation;

public class VisionManager implements IUpdateableModule, ISmartDashboardUpdaterModule
{
    private final VisionAdbServer mVisionServer;
    private final Snobot2018 mSnobot;

    /**
     * Constructor.
     * 
     * @param aSnobot
     *            The robot
     */
    public VisionManager(Snobot2018 aSnobot)
    {
        mVisionServer = new VisionAdbServer(8254, 5800, 12000);
        mSnobot = aSnobot;
    }

    /**
     * Sends a message to start recording images.
     */
    public void sendStartRecordingMessage()
    {
        String matchType = DriverStation.getInstance().getMatchType().toString();
        int matchNumber = DriverStation.getInstance().getMatchNumber();

        String mode;

        if (mSnobot.isDisabled())
        {
            mode = "disabled";
        }
        else
        {
            if (mSnobot.isAutonomous())
            {
                mode = "auto";
            }
            else
            {
                mode = "tele";
            }
        }

        mVisionServer.sendStartRecordingMessage(matchType, Integer.toString(matchNumber), mode);
    }

    @Override
    public void update()
    {
        if (mVisionServer.hasFreshImage())
        {
            TargetUpdateMessage targets = mVisionServer.getLatestTargetUpdate();
        }
    }

    @Override
    public void updateSmartDashboard()
    {
        // Nothing to do
    }
}
