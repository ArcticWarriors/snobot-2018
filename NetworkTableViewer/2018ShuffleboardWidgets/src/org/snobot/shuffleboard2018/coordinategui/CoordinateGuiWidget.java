package org.snobot.shuffleboard2018.coordinategui;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.snobot.coordinate_gui.model.Coordinate;
import org.snobot.coordinate_gui.powerup.CoordinateGui2018;
import org.snobot.shuffleboard2018.game_data.FmsInfoData;

import edu.wpi.first.shuffleboard.api.sources.DataSource;
import edu.wpi.first.shuffleboard.api.widget.AnnotatedWidget;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SingleSourceWidget;
import edu.wpi.first.shuffleboard.plugin.networktables.sources.NetworkTableSource;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Coordinate GUI", dataTypes = FmsInfoData.class)
@ParametrizedController("CoordinateGuiWidget.fxml")
public class CoordinateGuiWidget extends SingleSourceWidget implements AnnotatedWidget
{
    private static final long UPDATE_PERIOD = 20; // ms
    private static final long RENDER_FREQUENCY = 15; // number of loops (run at
                                                     // UPDATE_PERIOD) before
                                                     // you re-render

    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private CoordinateGui2018 mCoordinateGui;
    private DataSource<?> mXProperty;
    private DataSource<?> mYProperty;
    private DataSource<?> mAngleProperty;

    private int mRenderCtr;

    @FXML
    private void initialize()
    {
        mCoordinateGui = new CoordinateGui2018();

        mXProperty = NetworkTableSource.forKey("SmartDashboard/X");
        mYProperty = NetworkTableSource.forKey("SmartDashboard/Y");
        mAngleProperty = NetworkTableSource.forKey("SmartDashboard/Angle");

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mSwingNode.setContent((JPanel) mCoordinateGui.getComponent());
            }
        });

        TimerTask task = new TimerTask()
        {

            @Override
            public void run()
            {
                try
                {
                    handleUpdate();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace(); // NOPMD
                }
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task, 0, UPDATE_PERIOD);
    }

    private void handleUpdate()
    {

        boolean active = true;
        active &= mXProperty.isActive();
        active &= mYProperty.isActive();
        active &= mAngleProperty.isActive();

        if (active)
        {
            Coordinate coord = new Coordinate((Double) mXProperty.getData(), (Double) mYProperty.getData(), (Double) mAngleProperty.getData());
            mCoordinateGui.addCoordinate(coord);
        }
        else
        {
            System.out.println("Skipping update");

            mXProperty = NetworkTableSource.forKey("SmartDashboard/X");
            mYProperty = NetworkTableSource.forKey("SmartDashboard/Y");
            mAngleProperty = NetworkTableSource.forKey("SmartDashboard/Angle");
        }

        if (mRenderCtr % RENDER_FREQUENCY == 0)
        {
            mCoordinateGui.getLayerManager().render();
        }

        ++mRenderCtr;
    }

    @Override
    public Pane getView()
    {
        return mRoot;
    }

}
