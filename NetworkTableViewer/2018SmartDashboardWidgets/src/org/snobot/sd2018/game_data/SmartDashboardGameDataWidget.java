package org.snobot.sd2018.game_data;

import java.awt.BorderLayout;

import org.snobot.nt.game_data.GameDataContainer;
import org.snobot.sd.util.AutoUpdateWidget;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.smartdashboard.properties.Property;

public class SmartDashboardGameDataWidget extends AutoUpdateWidget
{
    public static final String NAME = "2018 Game Data Widget";

    private final GameDataContainer mPanel;

    private final NetworkTableEntry mGameMessageEntry;
    private final NetworkTableEntry mIsRedAllianceEntry;
    private final NetworkTableEntry mMatchNumberEntry;

    /**
     * Constructor.
     */
    public SmartDashboardGameDataWidget()
    {
        super(100);

        System.out.println("Adding widget");

        mPanel = new GameDataContainer();

        setLayout(new BorderLayout());
        add(mPanel, BorderLayout.CENTER);

        NetworkTable fmsInfoTable = NetworkTableInstance.getDefault().getTable("FMSInfo");
        mGameMessageEntry = fmsInfoTable.getEntry("GameSpecificMessage");
        mIsRedAllianceEntry = fmsInfoTable.getEntry("IsRedAlliance");
        mMatchNumberEntry = fmsInfoTable.getEntry("MatchNumber");
    }

    @Override
    public void propertyChanged(Property aProperty)
    {
        // Nothing to do
    }

    @Override
    protected void poll() throws Exception
    {
        String gameInfo = mGameMessageEntry.getString("");
        String allianceColor = mIsRedAllianceEntry.getBoolean(false) ? "Red" : "Blue";
        String matchNumber = mMatchNumberEntry.getNumber(0).toString();

        mPanel.setPositionData(matchNumber, allianceColor, gameInfo);
    }

    @Override
    public void init()
    {
        revalidate();
        repaint();
    }

}
