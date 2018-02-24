package org.snobot.sd2018.autonchanger;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.snobot.SmartDashboardNames;
import org.snobot.nt.auton.AutonPanel;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;

public class AutonChangerWidget extends StaticWidget
{
    public static final String NAME = "2018 Auton Widget";

    /**
     * Creates the Auton Changer Widget.
     */
    public AutonChangerWidget()
    {
        AutonPanel panelA = new AutonPanel();
        AutonPanel panelB = new AutonPanel();

        setLayout(new BorderLayout());
        add(panelA, BorderLayout.WEST);
        add(panelB, BorderLayout.EAST);

        NetworkTable autonTableA = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sAUTON_TABLE_A_NAME);
        NetworkTable autonTableB = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sAUTON_TABLE_B_NAME);

        addListener(panelA, autonTableA);
        addListener(panelB, autonTableB);

        this.setVisible(true);
    }

    private void addListener(AutonPanel aAutonPanel, NetworkTable aAutonTable)
    {
        aAutonPanel.addSaveListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent aActionEvent)
            {
                aAutonTable.getEntry(SmartDashboardNames.sROBOT_COMMAND_TEXT).setString(aAutonPanel.getTextArea().getText());
                aAutonTable.getEntry(SmartDashboardNames.sSAVE_AUTON).setBoolean(true);

            }
        });

        aAutonPanel.addTextChangedListener(new DocumentListener()
        {
            private void onChange()
            {
                aAutonTable.getEntry(SmartDashboardNames.sSAVE_AUTON).setBoolean(false);
            }

            @Override
            public void removeUpdate(DocumentEvent aActionEvent)
            {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent aActionEvent)
            {
                onChange();
                
            }

            @Override
            public void insertUpdate(DocumentEvent aActionEvent)
            {
                onChange();
                
            }
        });
        
        TableEntryListener textUpdatedListener = new TableEntryListener()
        {
            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                aAutonPanel.getTextArea().setText(aValue.getString());

            }
        };
        
        TableEntryListener errorListener = new TableEntryListener()
        {
            @Override
            public void valueChanged(NetworkTable aNetworkTable, String aKey, NetworkTableEntry aEntry, NetworkTableValue aValue, int aFlags)
            {
                aAutonPanel.setParseSuccess(aValue.getBoolean());
            }
        };


        aAutonTable.addEntryListener(SmartDashboardNames.sROBOT_COMMAND_TEXT, textUpdatedListener, 0xFF);
        aAutonTable.addEntryListener(SmartDashboardNames.sSUCCESFULLY_PARSED_AUTON, errorListener, 0xFF);
    }

    @Override
    public void propertyChanged(Property aActionEvent)
    {
        // Nothing to do
    }

    @Override
    public void init()
    {
        // Nothing to do
    }

}
