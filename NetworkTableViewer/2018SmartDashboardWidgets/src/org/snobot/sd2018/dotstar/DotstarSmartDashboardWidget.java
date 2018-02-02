package org.snobot.sd2018.dotstar;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import org.snobot.nt.dotstar.DotstarSimulatorPanel;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;

public class DotstarSmartDashboardWidget extends StaticWidget
{
    public static final String NAME = "2018 Dotstar Widget";

    private final DotstarSimulatorPanel mPanel;
    
    /**
     * Constructor.
     */
    public DotstarSmartDashboardWidget()
    {
        mPanel = new DotstarSimulatorPanel(19 * 2);

        setLayout(new BorderLayout());
        add(mPanel, BorderLayout.CENTER);

        NetworkTable ledSimulatorTable = NetworkTableInstance.getDefault().getTable("LedSimulator");

        ledSimulatorTable.addEntryListener(new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                handleUpdate(aValue.getString());
            }
        }, 0xFF);
    }

    private void handleUpdate(String aValueString)
    {
        List<Integer> values = new ArrayList<>();

        for (String s : aValueString.split(","))
        {
            values.add(Integer.parseInt(s));
        }

        mPanel.setValues(values);
    }

    @Override
    public void propertyChanged(Property aProperty)
    {
        // Nothing to do
    }

    @Override
    public void init()
    {
        // Nothing to do
    }

}
