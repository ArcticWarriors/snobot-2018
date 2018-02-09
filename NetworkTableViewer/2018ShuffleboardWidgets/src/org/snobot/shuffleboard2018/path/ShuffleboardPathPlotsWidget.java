package org.snobot.shuffleboard2018.path;

import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import org.snobot.nt.coordinategui.IdealPlotSerializer;
import org.snobot.nt.path_plotter.PathPlotterPanel;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Path Plots", dataTypes = PathData.class)
@ParametrizedController("PathPlotsWidget.fxml")
public class ShuffleboardPathPlotsWidget extends SimpleAnnotatedWidget<PathData>
{
    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private PathPlotterPanel mPanel;

    private int mLastIndex;

    @FXML
    private void initialize()
    {
        mPanel = new PathPlotterPanel();

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
            handlePathUpdate(aCur);
        });
    }

    private void handlePathUpdate(PathData aData)
    {
        mPanel.setPath(IdealPlotSerializer.deserializePath(aData.getIdealPath()));

        StringTokenizer tokenizer = new StringTokenizer(aData.getMeasuredPathPoint(), ",");

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
    }

    @Override
    public Pane getView()
    {
        return mRoot;
    }
}
