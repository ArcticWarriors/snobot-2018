package org.snobot.sd.spline_plotter;

import java.awt.BorderLayout;
import java.util.StringTokenizer;

import org.snobot.SmartDashboardNames;
import org.snobot.nt.coordinategui.IdealSplineSerializer;
import org.snobot.nt.spline_plotter.SplinePlotterPanel;
import org.snobot.nt.spline_plotter.SplineSegment;
import org.snobot.sd.util.AutoUpdateWidget;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.smartdashboard.properties.Property;

/**
 * Widget used to plot information on driving a cubic spline, pre-planned path.
 * 
 * @author PJ
 *
 */
public class SplinePlannerWidget extends AutoUpdateWidget
{
    public static final String NAME = "2018 SplinePlanning";

    private final String mIdealSplineName; // The SD name used to convey the ideal spline points
    private final String mRealSplineName; // The SD name of the real points
    private final NetworkTable mTable; // The network table used to send trajectory data

    private final SplinePlotterPanel mPanel;
    private int mLastIndex;



    /**
     * Constructor.
     */
    public SplinePlannerWidget()
    {
        super(10);
        setLayout(new BorderLayout());
        mPanel = new SplinePlotterPanel();
        add(mPanel);

        mTable = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sSPLINE_NAMESPACE);

        mLastIndex = 0;

        mIdealSplineName = SmartDashboardNames.sSPLINE_IDEAL_POINTS;
        mRealSplineName = SmartDashboardNames.sSPLINE_REAL_POINT;

        addPathListener();
    }

    private void addPathListener()
    {

        TableEntryListener idealSplineListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                mPanel.setPath(IdealSplineSerializer.deserializePath(aValue.getString()));
                mLastIndex = 0;
                revalidate();
                repaint();
            }
        };
        mTable.addEntryListener(mIdealSplineName, idealSplineListener, 0xFF);

        TableEntryListener realSplineListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                String pointInfo = mTable.getEntry(mRealSplineName).getString("");

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
                        SplineSegment segment = IdealSplineSerializer.deserializePathPoint(tokenizer);
                        mPanel.setPoint(index, segment);
                    }

                    mLastIndex = index;
                }
            }
        };
        mTable.addEntryListener(mRealSplineName, realSplineListener, 0xFF);
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
