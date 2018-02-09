package org.snobot.shuffleboard2018.trajectory;

import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import org.snobot.nt.coordinategui.IdealSplineSerializer;
import org.snobot.nt.spline_plotter.SplinePlotterPanel;
import org.snobot.nt.spline_plotter.SplineSegment;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Trajectory Plots", dataTypes = TrajectoryData.class)
@ParametrizedController("TrajectoryPlotsWidget.fxml")
public class ShuffleboardTrajectoryPlotsWidget extends SimpleAnnotatedWidget<TrajectoryData>
{
    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private SplinePlotterPanel mPanel;

    private int mLastIndex;

    @FXML
    private void initialize()
    {
        mPanel = new SplinePlotterPanel();

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mSwingNode.setContent(mPanel);
            }
        });

        dataOrDefault.addListener((aUnused, aPrev, aCur) ->
        {
            handleTrajectoryUpdate(aCur);
        });
    }

    private void handleTrajectoryUpdate(TrajectoryData aData)
    {
        mPanel.setPath(IdealSplineSerializer.deserializePath(aData.getIdealSpline()));

        StringTokenizer tokenizer = new StringTokenizer(aData.getMeasuredSpline(), ",");
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

    @Override
    public Pane getView()
    {
        return mRoot;
    }
}
