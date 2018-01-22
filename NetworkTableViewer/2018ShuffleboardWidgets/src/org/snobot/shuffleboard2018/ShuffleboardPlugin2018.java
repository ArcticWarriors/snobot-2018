package org.snobot.shuffleboard2018;

import java.util.List;

import org.snobot.shuffleboard2018.coordinategui.CoordinateGuiWidget;
import org.snobot.shuffleboard2018.game_data.FmsInfoDataType;
import org.snobot.shuffleboard2018.game_data.ShuffleboardGameDataWidget;

import com.google.common.collect.ImmutableList;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

@Description(group = "org.snobot", name = "SnobotPlugins", version = "1.0.0", summary = "FRC174 Snobot Plugins for the 2018 FIRST game FIRST Power Up")
public class ShuffleboardPlugin2018 extends Plugin
{

    @Override
    public List<ComponentType> getComponents()
    {
        return ImmutableList.of(
                WidgetType.forAnnotatedWidget(ShuffleboardGameDataWidget.class),
                WidgetType.forAnnotatedWidget(CoordinateGuiWidget.class));

    }

    @Override
    public List<DataType> getDataTypes()
    {
        return ImmutableList.of(new FmsInfoDataType());
    }

}
