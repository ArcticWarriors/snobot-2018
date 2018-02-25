package org.snobot.vision;

import org.snobot.Snobot2018;
import org.snobot.lib.modules.ISmartDashboardUpdaterModule;
import org.snobot.lib.modules.IUpdateableModule;
import org.snobot.positioner.IPositioner;

import com.snobot.vision_app.app2018.messages.TargetUpdateMessage;

import edu.wpi.first.wpilibj.DriverStation;

public class VisionManager implements IUpdateableModule, ISmartDashboardUpdaterModule
{
    private final IPositioner mPositioner;
    private final VisionAdbServer mVisionServer;
    private final Snobot2018 mSnobot;

    public VisionManager(Snobot2018 aSnobot, IPositioner aPositioner)
    {
        mVisionServer = new VisionAdbServer(8254, 5800, 12000);
        mPositioner = aPositioner;
        mSnobot = aSnobot;
    }

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
            System.out.println(targets + ", " + mPositioner.getOrientationDegrees());
        }
    }

    @Override
    public void updateSmartDashboard()
    {
        // Nothing to do
    }
}
