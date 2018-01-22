package org.snobot.nt.game_data;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameDataContainer extends JPanel
{
    private final GameDataPanel mDataDrawerPanel;
    private final JPanel mIndicatorPanel;
    private final JTextField mMatchNumberField;
    private final JTextField mAllianceField;
    private final JTextField mGameDataField;

    public GameDataContainer()
    {
        setLayout(new BorderLayout());

        mDataDrawerPanel = new GameDataPanel();
        add(mDataDrawerPanel, BorderLayout.CENTER);

        mIndicatorPanel = new JPanel();
        add(mIndicatorPanel, BorderLayout.SOUTH);

        mMatchNumberField = new JTextField(4);
        mMatchNumberField.setEditable(false);
        mIndicatorPanel.add(mMatchNumberField);

        mGameDataField = new JTextField(4);
        mGameDataField.setEditable(false);
        mIndicatorPanel.add(mGameDataField);
        
        mAllianceField = new JTextField(5);
        mAllianceField.setEditable(false);
        mIndicatorPanel.add(mAllianceField);
    }

    public void setPositionData(String aMatchNumber, String aAlliance, String aGameData)
    {
        mDataDrawerPanel.setPositionData(aGameData.toLowerCase());

        mGameDataField.setText(aGameData);
        mMatchNumberField.setText(aMatchNumber);
        mAllianceField.setText(aAlliance);
    }

}
