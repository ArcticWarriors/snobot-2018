package com.snobot.sd2018.coordinategui.widget;

import java.awt.BorderLayout;
import java.awt.Color;

import com.snobot.coordinate_gui.model.Coordinate;
import com.snobot.coordinate_gui.steamworks.CoordinateGui2017;
import com.snobot.coordinate_gui.ui.renderProps.CoordinateLayerRenderProps;
import com.snobot.coordinate_gui.ui.renderProps.RobotLayerRenderProps;
import com.snobot.sd.util.AutoUpdateWidget;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.tables.ITable;

public class CoordinateWidet2018 extends AutoUpdateWidget
{
    public static final String NAME = "2018 Coordinate Widget";
    
    private CoordinateGui2017 mCoordinateGui;

    public CoordinateWidet2018()
    {
        this(false);
    }

    public CoordinateWidet2018(boolean aDebug)
    {
        super(aDebug, 20);

        CoordinateLayerRenderProps trajectoryLayerRenderProps = new CoordinateLayerRenderProps();
        CoordinateLayerRenderProps coordinateLayerRenderProps = new CoordinateLayerRenderProps();
        RobotLayerRenderProps robotLayerRenderProps = new RobotLayerRenderProps();

        trajectoryLayerRenderProps.setFadeOverTime(false);
        trajectoryLayerRenderProps.setPointSize(5);
        trajectoryLayerRenderProps.setPointMemory(-1);
        trajectoryLayerRenderProps.setPointColor(Color.red);

        mCoordinateGui = new CoordinateGui2017(trajectoryLayerRenderProps, coordinateLayerRenderProps, robotLayerRenderProps);

        setLayout(new BorderLayout());
        add(mCoordinateGui.getComponent(), BorderLayout.CENTER);

        setSize(100, 100);
    }

    @Override
    public void init()
    {
        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property property)
    {

    }


    @Override
    protected void poll() throws Exception
    {
        ITable robotTable = Robot.getTable();

        if (robotTable != null && mCoordinateGui != null)
        {
            double x = 0;
            double y = 0;
            double angle = 0;

            Coordinate coord = new Coordinate(x, y, angle);
            mCoordinateGui.addCoordinate(coord);
        }
    }
}
