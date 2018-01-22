package org.snobot.shuffleboard2018.game_data;

import javax.swing.SwingUtilities;

import org.snobot.nt.game_data.GameDataContainer;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Game Data Widget", dataTypes = FmsInfoData.class)
@ParametrizedController("GameDataWidget.fxml")
public class ShuffleboardGameDataWidget extends SimpleAnnotatedWidget<FmsInfoData>
{
    @FXML
    private Pane root;

    @FXML
    private SwingNode swingNode;

    private GameDataContainer mPanel;

    @FXML
    private void initialize()
    {
        mPanel = new GameDataContainer();

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                swingNode.setContent(mPanel);
            }
        });

        dataOrDefault.addListener((__, prev, cur) ->
        {
            mPanel.setPositionData("" + cur.getMatchNumber(), "" + cur.isIsRedAlliance(), cur.getGameSpecificMessage());
            mPanel.invalidate();
            mPanel.revalidate();
        });
    }

    @Override
    public Pane getView()
    {
        return root;
    }
}
