package org.snobot.sd2018.robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.snobot.SmartDashboardNames;
import org.snobot.sd.util.AutoUpdateWidget;
import org.snobot.sd.util.SmartDashboardUtil;

import edu.wpi.first.smartdashboard.properties.Property;

@SuppressWarnings("serial")
public class RobotWidget extends AutoUpdateWidget
{
    public static final String NAME = "2018 Robot Widget";
    public static final String mClawPositionOpenString = "kForward";
    private final RobotDrawer mDrawer;

    /**
     * Robot widget constructor.
     */
    public RobotWidget()
    {
        super(200);
        setLayout(new BorderLayout());
        mDrawer = new RobotDrawer();
        add(mDrawer, BorderLayout.CENTER);
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    public void propertyChanged(Property aProperty)
    {
        // nothing required
    }

    @Override
    protected void poll() throws Exception
    {
        String robotClawPosition = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sSNOBOT_CLAW_POSITION)
                .getString(mClawPositionOpenString);
        boolean clawIsOpen = mClawPositionOpenString.equals(robotClawPosition);
        double clawHeight = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sELEVATOR_HEIGHT).getDouble(0);
        double winchSpeed = SmartDashboardUtil.getTable().getEntry(SmartDashboardNames.sWINCH_SPEED).getDouble(0);

        if (mDrawer != null)
        {
            mDrawer.setWinchMotorSpeed(winchSpeed);
            mDrawer.setClawHeight(clawHeight);
            mDrawer.setClawIsOpen(clawIsOpen);
        }
        repaint();
    }

    @Override
    public void init()
    {
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent aComponentEvent)
            {
                mDrawer.updateSize();
            }
        });
        mDrawer.updateSize();
    }

}
