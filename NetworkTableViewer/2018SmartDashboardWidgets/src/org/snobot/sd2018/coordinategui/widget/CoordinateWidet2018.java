package org.snobot.sd2018.coordinategui.widget;

import java.awt.BorderLayout;

import org.snobot.coordinate_gui.model.Coordinate;
import org.snobot.coordinate_gui.powerup.CoordinateGui2018;
import org.snobot.sd.util.AutoUpdateWidget;

import edu.wpi.first.smartdashboard.properties.Property;

public class CoordinateWidet2018 extends AutoUpdateWidget
{
    public static final String NAME = "2018 Coordinate Widget";

    private static final long UPDATE_PERIOD = 20; // ms
    private static final long RENDER_FREQUENCY = 15; // number of loops (run at
                                                     // UPDATE_PERIOD) before
                                                     // you re-render
    
    private CoordinateGui2018 mCoordinateGui;
    private int mRenderCtr;

    public CoordinateWidet2018()
    {
        this(false);
    }

    public CoordinateWidet2018(boolean aDebug)
    {
        super(aDebug, UPDATE_PERIOD);

        mCoordinateGui = new CoordinateGui2018();

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
        if (mCoordinateGui != null)
        {
            double x = 0;
            double y = 0;
            double angle = 0;

            Coordinate coord = new Coordinate(x, y, angle);
            mCoordinateGui.addCoordinate(coord);

            if (mRenderCtr % RENDER_FREQUENCY == 0)
            {
                mCoordinateGui.getLayerManager().render();
            }

            ++mRenderCtr;
        }
    }
}
