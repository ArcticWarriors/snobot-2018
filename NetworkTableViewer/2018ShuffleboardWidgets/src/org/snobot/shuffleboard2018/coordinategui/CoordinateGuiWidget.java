
package org.snobot.shuffleboard2018.coordinategui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.PropertyBinding;
import org.snobot.SmartDashboardNames;
import org.snobot.coordinate_gui.model.Coordinate;
import org.snobot.coordinate_gui.powerup.CoordinateGui2018;
import org.snobot.nt.coordinategui.IdealSplineSerializer;
import org.snobot.nt.spline_plotter.SplineSegment;
import org.snobot.shuffleboard2018.game_data.FmsInfoData;
import org.snobot.shuffleboard2018.trajectory.TrajectoryData;

import edu.wpi.first.shuffleboard.api.sources.DataSource;
import edu.wpi.first.shuffleboard.api.widget.AbstractWidget;
import edu.wpi.first.shuffleboard.api.widget.AnnotatedWidget;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.plugin.networktables.sources.NetworkTableSource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Coordinate GUI", dataTypes = {TrajectoryData.class, FmsInfoData.class, GoToPositionData.class })
@ParametrizedController("CoordinateGuiWidget.fxml")
public class CoordinateGuiWidget extends AbstractWidget implements AnnotatedWidget
{
    private static final Logger sLOGGER = LogManager.getLogger(CoordinateGuiWidget.class);

    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private final ObjectProperty<DataSource> mTrajectoryDataSource = new SimpleObjectProperty<>(this, "trajectorySource", DataSource.none());
    private final PropertyBinding<TrajectoryData> mTrajectoryData = EasyBind.monadic(mTrajectoryDataSource).selectProperty(DataSource::dataProperty);

    private final ObjectProperty<DataSource> mFmsInfoDataSource = new SimpleObjectProperty<>(this, "fmsInfoSource", DataSource.none());
    private final PropertyBinding<FmsInfoData> mFmsInfoData = EasyBind.monadic(mFmsInfoDataSource).selectProperty(DataSource::dataProperty);

    private final ObjectProperty<DataSource> mGoToPositionDataSource = new SimpleObjectProperty<>(this, "goToPosition", DataSource.none());
    private final PropertyBinding<GoToPositionData> mGoToPositionData = EasyBind.monadic(mGoToPositionDataSource)
            .selectProperty(DataSource::dataProperty);

    private final ObjectProperty<DataSource> mPositionDataSource = new SimpleObjectProperty<>(this, "robotSource", DataSource.none());
    private final PropertyBinding<CoordinateData> mPositionData = EasyBind.monadic(mPositionDataSource).selectProperty(DataSource::dataProperty);

    private CoordinateGui2018 mCoordinateGui;

    @FXML
    private void initialize()
    {
        mCoordinateGui = new CoordinateGui2018();

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mSwingNode.setContent((JPanel) mCoordinateGui.getComponent());
            }
        });

        ///////////////////////////
        // Trajectory
        ///////////////////////////
        DataSource<?> trajectoryDataSource = NetworkTableSource.forKey(SmartDashboardNames.sSPLINE_NAMESPACE);
        mTrajectoryDataSource.set(trajectoryDataSource);
        trajectoryDataSource.addClient(this);
        mTrajectoryData.addListener((aUnused, aPrev, aCur) ->
        {
            handleTrajectoryUpdate(aCur);
        });
        if (trajectoryDataSource.isActive())
        {
            handleTrajectoryUpdate((TrajectoryData) trajectoryDataSource.getData());
        }

        ///////////////////////////
        // Go To Position
        ///////////////////////////
        DataSource<?> goToPositionDataSource = NetworkTableSource.forKey(SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME);
        mGoToPositionDataSource.set(goToPositionDataSource);
        goToPositionDataSource.addClient(this);
        mGoToPositionData.addListener((aUnused, aPrev, aCur) ->
        {
            handleGoToPositionUpdate(aCur);
        });
        if (goToPositionDataSource.isActive())
        {
            handleGoToPositionUpdate((GoToPositionData) goToPositionDataSource.getData());
        }

        ///////////////////////////
        // FMS Info
        ///////////////////////////
        DataSource<?> fmsInfoDataSource = NetworkTableSource.forKey("FMSInfo");
        mFmsInfoDataSource.set(fmsInfoDataSource);
        fmsInfoDataSource.addClient(this);
        mFmsInfoData.addListener((aUnused, aPrev, aCur) ->
        {
            handleFmsInfoUpdate(aCur);
        });

        if (fmsInfoDataSource.isActive())
        {
            handleFmsInfoUpdate((FmsInfoData) fmsInfoDataSource.getData());
        }

        ///////////////////////////
        // Robot Position
        ///////////////////////////
        DataSource<?> robotPositionDataSource = NetworkTableSource.forKey("RobotPosition");
        mPositionDataSource.set(robotPositionDataSource);
        robotPositionDataSource.addClient(this);
        mPositionData.addListener((aUnused, aPrev, aCur) ->
        {
            handlePositionUpdate(aCur);
        });

        if (robotPositionDataSource.isActive())
        {
            handlePositionUpdate((CoordinateData) robotPositionDataSource.getData());
        }
    }

    private void handleGoToPositionUpdate(GoToPositionData aCur)
    {
        if (aCur.getStart().isEmpty() || aCur.getEnd().isEmpty())
        {
            sLOGGER.log(Level.WARN, "Empty GoToPosition data, ignoring it");
            return;
        }
        String[] startParts = aCur.getStart().split(",");
        Coordinate start = new Coordinate(Double.parseDouble(startParts[0]) / 12, Double.parseDouble(startParts[1]) / 12, 0);

        String[] endParts = aCur.getEnd().split(",");
        Coordinate end = new Coordinate(Double.parseDouble(endParts[0]) / 12, Double.parseDouble(endParts[1]) / 12, 0);

        mCoordinateGui.setStartAndStopPoints(start, end);
        mCoordinateGui.getLayerManager().render();
    }

    private void handleFmsInfoUpdate(FmsInfoData aCur)
    {
        mCoordinateGui.setPanelLocations(aCur.getGameSpecificMessage());
        mCoordinateGui.getLayerManager().render();
    }

    private void handleTrajectoryUpdate(TrajectoryData aCur)
    {
        parseIdealSpline(aCur.getIdealSpline());
        parseWaypoints(aCur.getSplineWaypoints());
    }

    private void parseIdealSpline(String aSplineText)
    {
        List<Coordinate> coordinates = new ArrayList<>();
        List<SplineSegment> segments = IdealSplineSerializer.deserializePath(aSplineText);
        sLOGGER.log(Level.INFO, "Parsing ideal spline (" + segments.size() + ")");

        for (SplineSegment splineSegment : segments)
        {
            coordinates.add(new Coordinate(splineSegment.mAverageX / 12.0, splineSegment.mAverageY / 12.0, splineSegment.mRobotHeading));
        }
        mCoordinateGui.setPath(coordinates);
    }

    private void parseWaypoints(String aWaypointText)
    {
        List<Coordinate> coordinates = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(aWaypointText, ",");
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

    private void handlePositionUpdate(CoordinateData aCur)
    {
        double x = aCur.getX() / 12;
        double y = aCur.getY() / 12;
        double angle = aCur.getAngle();

        Coordinate coord = new Coordinate(x, y, angle);
        mCoordinateGui.addCoordinate(coord);
        mCoordinateGui.getLayerManager().render();
    }

    @Override
    public Pane getView()
    {
        return mRoot;
    }

}
