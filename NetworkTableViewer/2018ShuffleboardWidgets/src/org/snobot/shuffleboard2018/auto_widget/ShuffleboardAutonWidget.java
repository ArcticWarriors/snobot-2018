package org.snobot.shuffleboard2018.auto_widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import org.snobot.nt.auton.AutonPanel;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

@Description(name = "Auton Widget", dataTypes = AutonData.class)
@ParametrizedController("AutonWidget.fxml")
public class ShuffleboardAutonWidget extends SimpleAnnotatedWidget<AutonData>
{
    @FXML
    private Pane mRoot;

    @FXML
    private SwingNode mSwingNode;

    private AutonPanel mPanel;


    @FXML
    private void initialize()
    {
        mPanel = new AutonPanel();

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

        mPanel.addSaveListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent aActionEvent)
            {
                AutonData old = getData();
                setData(new AutonData(old.getFileName(), mPanel.getTextArea().getText(), old.isParsedCommand(), true));
            }
        });

    }

    private void handleUpdate(AutonData aData)
    {
        mPanel.getTextArea().setText(aData.getCommandText());
        mPanel.setParseSuccess(aData.isParsedCommand());
    }

    @Override
    public Pane getView()
    {
        return mRoot;
    }

}
