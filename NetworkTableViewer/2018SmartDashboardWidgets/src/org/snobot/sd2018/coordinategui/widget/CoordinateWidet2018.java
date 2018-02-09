package org.snobot.sd2018.coordinategui.widget;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.snobot.SmartDashboardNames;
import org.snobot.coordinate_gui.model.Coordinate;
import org.snobot.coordinate_gui.powerup.CoordinateGui2018;
import org.snobot.nt.coordinategui.IdealSplineSerializer;
import org.snobot.nt.spline_plotter.SplineSegment;
import org.snobot.sd.util.AutoUpdateWidget;
import org.snobot.sd.util.SmartDashboardUtil;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.smartdashboard.properties.Property;

public class CoordinateWidet2018 extends AutoUpdateWidget
{
    public static final String NAME = "2018 Coordinate Widget";

    private static final long UPDATE_PERIOD = 20; // ms
    private static final long RENDER_FREQUENCY = 15; // number of loops (run at
                                                     // UPDATE_PERIOD) before
                                                     // you re-render
    
    private final CoordinateGui2018 mCoordinateGui;
    private int mRenderCtr;

    /**
     * Constructor.
     */
    public CoordinateWidet2018()
    {
        super(UPDATE_PERIOD);

        mCoordinateGui = new CoordinateGui2018();

        setLayout(new BorderLayout());
        add(mCoordinateGui.getComponent(), BorderLayout.CENTER);

        initializeTrajectoryListener();
        initializeGameDataListener();
        initializeGoToPositionListener();

        setSize(100, 100);
    }

    @Override
    public void init()
    {
        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property aProperty)
    {
        // Nothing to do
    }

    private void initializeGameDataListener()
    {
        TableEntryListener gameDataListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                mCoordinateGui.setPanelLocations(aValue.getString());
            }
        };

        NetworkTableInstance.getDefault().getTable("FMSInfo").addEntryListener("GameSpecificMessage", gameDataListener, 0xFF);
    }

    private void initializeGoToPositionListener()
    {
        TableEntryListener goToPositionListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                handleGoToPositionUpdate();
            }
        };

        NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME)
                .addEntryListener(SmartDashboardNames.sGO_TO_POSITION_START, goToPositionListener, 0xFF);
    }

    private void initializeTrajectoryListener()
    {
        TableEntryListener idealSplineListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                handleIdealTrajectory(aValue.getString());
            }
        };

        NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sSPLINE_NAMESPACE).addEntryListener(SmartDashboardNames.sSPLINE_IDEAL_POINTS,
                idealSplineListener, 0xFF);

        TableEntryListener waypointListener = new TableEntryListener()
        {

            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                handleWaypointUpdate(aValue.getString());
            }
        };

        NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sSPLINE_NAMESPACE).addEntryListener(SmartDashboardNames.sSPLINE_WAYPOINTS,
                waypointListener, 0xFF);
    }

    private void handleIdealTrajectory(String aValuesString)
    {
        List<Coordinate> coordinates = new ArrayList<>();
        for (SplineSegment splineSegment : IdealSplineSerializer.deserializePath(aValuesString))
        {
            coordinates.add(new Coordinate(splineSegment.mAverageX / 12.0, splineSegment.mAverageY / 12.0, splineSegment.mRobotHeading));
        }
        mCoordinateGui.setPath(coordinates);
    }

    private void handleWaypointUpdate(String aValuesString)
    {
        List<Coordinate> coordinates = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(aValuesString, ",");
        while (tokenizer.countTokens() >= 3)
        {
            double x = Double.parseDouble(tokenizer.nextToken());
            double y = Double.parseDouble(tokenizer.nextToken());
            double angle = Math.toDegrees(Double.parseDouble(tokenizer.nextToken()));
            Coordinate coordinate = new Coordinate(x / 12.0, y / 12.0, angle);
            coordinates.add(coordinate);
        }
        mCoordinateGui.setWaypoints(coordinates);
    }

    private void handleGoToPositionUpdate()
    {
        NetworkTable table = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME);
        String startString = table.getEntry(SmartDashboardNames.sGO_TO_POSITION_START).getString("");
        String endString = table.getEntry(SmartDashboardNames.sGO_TO_POSITION_END).getString("");

        if (startString.isEmpty() || endString.isEmpty())
        {
            return;
        }
        String[] startParts = startString.split(",");
        Coordinate start = new Coordinate(Double.parseDouble(startParts[0]), Double.parseDouble(startParts[1]), 0);

        String[] endParts = endString.split(",");
        Coordinate end = new Coordinate(Double.parseDouble(endParts[0]), Double.parseDouble(endParts[1]), 0);

        mCoordinateGui.setStartAndStopPoints(start, end);
        mCoordinateGui.getLayerManager().render();
    }

    @Override
    protected void poll() throws Exception
    {
        if (mCoordinateGui != null)
        {
            double x = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sX_POSITION).getDouble(0) / 12;
            double y = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sY_POSITION).getDouble(0) / 12;
            double angle = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sORIENTATION).getDouble(0);

            Coordinate coord = new Coordinate(x, y, angle);
            mCoordinateGui.addCoordinate(coord);

            if (mRenderCtr % RENDER_FREQUENCY == 0)
            {
                mCoordinateGui.getLayerManager().render();
            }

            ++mRenderCtr;
        }
    }
}
