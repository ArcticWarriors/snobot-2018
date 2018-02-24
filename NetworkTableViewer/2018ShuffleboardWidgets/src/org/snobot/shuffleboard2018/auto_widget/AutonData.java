package org.snobot.shuffleboard2018.auto_widget;

import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class AutonData extends ComplexData<AutonData>
{
    private final String mFileName;
    private final String mCommandText;
    private final boolean mParsedCommand;
    private final boolean mIsSaving;

    public AutonData()
    {
        this("", "# Nothing Received", false, false);
    }

    /**
     * Auton Data Variables.
     * 
     * @param aFileName
     *            File Name
     * @param aParsedCommand
     *            Parsed Command
     * @param aCommandText
     *            Command Text
     */
    public AutonData(String aFileName, String aCommandText, boolean aParsedCommand, boolean aIsSaving)
    {
        mFileName = aFileName;
        mCommandText = aCommandText;
        mParsedCommand = aParsedCommand;
        mIsSaving = aIsSaving;
    }

    /**
     * Creates the Auton Map.
     * 
     * @param aMap
     *            The map gets the info from the smartDashboard
     */
    public AutonData(Map<String, Object> aMap)
    {
        this(
                (String) aMap.getOrDefault(SmartDashboardNames.sAUTON_FILENAME, ""),
                (String) aMap.getOrDefault(SmartDashboardNames.sROBOT_COMMAND_TEXT, "# Nothing Received"),
                (Boolean) aMap.getOrDefault(SmartDashboardNames.sSUCCESFULLY_PARSED_AUTON, false),
                (Boolean) aMap.getOrDefault(SmartDashboardNames.sSAVE_AUTON, false)
        );
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(SmartDashboardNames.sAUTON_FILENAME, mFileName);
        map.put(SmartDashboardNames.sSUCCESFULLY_PARSED_AUTON, mParsedCommand);
        map.put(SmartDashboardNames.sROBOT_COMMAND_TEXT, mCommandText);
        map.put(SmartDashboardNames.sSAVE_AUTON, mIsSaving);

        return map;
    }

    public String getFileName()
    {
        return mFileName;
    }

    public boolean isParsedCommand()
    {
        return mParsedCommand;
    }

    public String getCommandText()
    {
        return mCommandText;
    }

    public boolean isSaving()
    {
        return mIsSaving;
    }

}
