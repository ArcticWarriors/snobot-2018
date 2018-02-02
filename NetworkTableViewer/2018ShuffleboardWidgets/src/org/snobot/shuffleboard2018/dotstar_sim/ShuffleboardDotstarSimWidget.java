package org.snobot.shuffleboard2018.dotstar_sim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.snobot.nt.dotstar.DotstarSimulatorPanel;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Doststar Sim Widget", dataTypes = DotstarData.class)
@ParametrizedController("DotstarSimWidget.fxml")
public class ShuffleboardDotstarSimWidget extends SimpleAnnotatedWidget<DotstarData>
{
    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private DotstarSimulatorPanel mPanel;

    @FXML
    private void initialize()
    {
        mPanel = new DotstarSimulatorPanel(60);

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
            handleUpdate(aCur);
        });
    }

    private void handleUpdate(DotstarData aData)
    {
        if (aData.getValues().isEmpty())
        {
            return;
        }
        List<Integer> values = new ArrayList<>();

        for (String s : aData.getValues().split(","))
        {
            values.add(Integer.parseInt(s));
        }

        mPanel.setValues(values);
    }

    @Override
    public Pane getView()
    {
        return mRoot;
    }
}
