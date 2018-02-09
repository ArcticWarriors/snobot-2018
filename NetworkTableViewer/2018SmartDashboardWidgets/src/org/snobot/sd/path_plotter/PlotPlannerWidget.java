package org.snobot.sd.path_plotter;

import java.awt.BorderLayout;
import java.util.StringTokenizer;

import org.snobot.SmartDashboardNames;
import org.snobot.nt.coordinategui.IdealPlotSerializer;
import org.snobot.nt.path_plotter.PathPlotterPanel;
import org.snobot.sd.util.AutoUpdateWidget;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.smartdashboard.properties.Property;

public class PlotPlannerWidget extends AutoUpdateWidget
{
    public static final String NAME = "2018 PathPlanning";

    private final String mSDIdealPathName;
    private final String mSDRealPathname;

    private final PathPlotterPanel mPanel;
    private final NetworkTable mTable;
    private int mLastIndex;

    /**
     * Constructor.
     */
    public PlotPlannerWidget()
    {
        super(10);
        setLayout(new BorderLayout());
        mPanel = new PathPlotterPanel();
        add(mPanel);

        mLastIndex = 0;

        mSDIdealPathName = SmartDashboardNames.sPATH_IDEAL_POINTS;
        mSDRealPathname = SmartDashboardNames.sPATH_POINT;

        mTable = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sPATH_NAMESPACE);

        addPathListener();
    }

    private void addPathListener()
    {

        TableEntryListener plannedPathListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                mPanel.setPath(IdealPlotSerializer.deserializePath(aEntry.getString("")));
                mLastIndex = 0;
                revalidate();
                repaint();
            }
        };
        mTable.addEntryListener(mSDIdealPathName, plannedPathListener, 0xFF);

        TableEntryListener realPointListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                String pointInfo = mTable.getEntry(mSDRealPathname).getString("");

                StringTokenizer tokenizer = new StringTokenizer(pointInfo, ",");

                if (tokenizer.hasMoreElements())
                {
                    int index = Integer.parseInt(tokenizer.nextElement().toString());

                    if (index == 0 || index < mLastIndex)
                    {
                        mPanel.clearActuals();
                    }

                    if (index > mLastIndex)
                    {
                        mPanel.setPoint(index, IdealPlotSerializer.deserializePathPoint(tokenizer));
                    }

                    mLastIndex = index;
                }

                revalidate();
                repaint();
            }
        };
        mTable.addEntryListener(mSDRealPathname, realPointListener, 0xFF);
    }

    @Override
    public void propertyChanged(Property aArg)
    {
        // Nothing to do
    }

    @Override
    public void init()
    {
        // Nothing to do
    }

    @Override
    protected void poll() throws Exception
    {
        // Nothing to do
    }

}
