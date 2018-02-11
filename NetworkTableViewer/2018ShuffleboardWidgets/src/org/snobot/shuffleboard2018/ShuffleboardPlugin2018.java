package org.snobot.shuffleboard2018;

import java.util.List;

import org.snobot.shuffleboard2018.auto_widget.AutonDataType;
import org.snobot.shuffleboard2018.auto_widget.ShuffleboardAutonWidget;
import org.snobot.shuffleboard2018.coordinategui.CoordinateGuiWidget;
import org.snobot.shuffleboard2018.coordinategui.GoToPositionDataType;
import org.snobot.shuffleboard2018.dotstar_sim.DotstarDataType;
import org.snobot.shuffleboard2018.dotstar_sim.ShuffleboardDotstarSimWidget;
import org.snobot.shuffleboard2018.game_data.FmsInfoDataType;
import org.snobot.shuffleboard2018.game_data.ShuffleboardGameDataWidget;
import org.snobot.shuffleboard2018.path.PathDataType;
import org.snobot.shuffleboard2018.path.ShuffleboardPathPlotsWidget;
import org.snobot.shuffleboard2018.trajectory.ShuffleboardTrajectoryPlotsWidget;
import org.snobot.shuffleboard2018.trajectory.TrajectoryDataType;

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
                WidgetType.forAnnotatedWidget(ShuffleboardDotstarSimWidget.class), 
                WidgetType.forAnnotatedWidget(ShuffleboardTrajectoryPlotsWidget.class), 
                WidgetType.forAnnotatedWidget(ShuffleboardPathPlotsWidget.class),
                WidgetType.forAnnotatedWidget(CoordinateGuiWidget.class), 
                WidgetType.forAnnotatedWidget(ShuffleboardAutonWidget.class));

    }

    @Override
    public List<DataType> getDataTypes()
    {
        return ImmutableList.of(
                new FmsInfoDataType(),
                new DotstarDataType(),
                new TrajectoryDataType(),
                new PathDataType(),
                new GoToPositionDataType(),
                new AutonDataType());
    }

}
