package org.snobot.sd2018.robot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.snobot.nt.util.Util;

public class RobotDrawer extends JPanel
{
    private static final long serialVersionUID = 1L;
    // Chassis dimensions (CHANGE AT SOME POINT) in inches
    private static final double sROBOT_WIDTH = 32;
    private static final double sROBOT_HEIGHT = 6;
    private static final double sELEVATOR_WIDTH = 4;
    private static final double sELEVATOR_HEIGHT = 80;
    private static final double sCLAW_WIDTH = 16;
    private static final double sCLAW_HEIGHT = 4;
    private static final double sWINCH_RADIUS = 5;
    private static final double sDRAW_PANEL_MARGIN = 10;

    // Drawing locations in pixels
    // Top, Left is 0,0
    // Elements are drawn down so w = 40, H = 40 STARTING AT 0, 0, end ate 40,
    // 40
    private static final double sCHASSIS_X_START = sDRAW_PANEL_MARGIN;
    private static final double sCHASSIS_Y_START = sDRAW_PANEL_MARGIN + sELEVATOR_HEIGHT + sROBOT_HEIGHT;
    // Make all positions relative to robot chassis (so they sit on chassis)
    private static final double sCHASSIS_TOP = sCHASSIS_Y_START;
    private static final double sWINCH_X_START = sCHASSIS_X_START;
    private static final double sWINCH_Y_START = sCHASSIS_TOP - (2 * sWINCH_RADIUS);
    private static final double sELEVATOR_X_START = sCHASSIS_X_START + sROBOT_WIDTH - sELEVATOR_WIDTH - (sCLAW_WIDTH / 2);
    private static final double sELEVATOR_Y_START = sCHASSIS_TOP - sELEVATOR_HEIGHT;
    private static final double sCLAW_X_START = sELEVATOR_X_START + sELEVATOR_WIDTH;
    private static final double sCLAW_Y_START = sCHASSIS_TOP - sCLAW_HEIGHT;

    // Size to draw, in inches
    private static final double sDRAWING_WIDTH = (sDRAW_PANEL_MARGIN * 2) + sROBOT_WIDTH + sCLAW_WIDTH;
    private static final double sDRAWING_HEIGHT = (sDRAW_PANEL_MARGIN * 2) + sCHASSIS_Y_START;

    // Component Colors
    private static final Color sROBOT_BASE_COLOR = Color.black;
    private static final Color sROBOT_WINCH_COLOR = Color.gray;
    private static final Color sROBOT_ELEVATOR_COLOR = Color.magenta;
    private static final Color sROBOT_CLAW_COLOR = Color.green;
    private static final Color sROBOT_CLAW_COLOR_CLOSE = Color.red;

    /**
     * The scaling factor used for drawing. For example, 1 would mean draw every
     * inch as one pixel, 5 would mean draw every inch as 5 pixels
     */
    private double mScaleFactor;

    // Robot State
    private boolean mClawIsOpen;
    private double mWinchSpeed;
    private double mClawHeight;

    /**
     * RobotDrawer Constructor.
     */
    public RobotDrawer()
    {
        setPreferredSize(new Dimension(600, 600));
        setVisible(true);
        setSize(400, 300);

        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent aArg)
            {
                updateSize();
            }
        });
    }

    /**
     * Update size.
     */
    public void updateSize()
    {
        double horizantalScaleFactor = getWidth() / sDRAWING_WIDTH;
        double verticleScaleFactor = getHeight() / sDRAWING_HEIGHT;

        double minScaleFactor = Math.min(horizantalScaleFactor, verticleScaleFactor);

        mScaleFactor = minScaleFactor;

        repaint();
    }

    private void drawRobotBase(Graphics2D aG2d)
    {
        Rectangle2D robotBase = new Rectangle2D.Double(
                sCHASSIS_X_START * mScaleFactor, 
                sCHASSIS_Y_START * mScaleFactor, 
                sROBOT_WIDTH * mScaleFactor,
                sROBOT_HEIGHT * mScaleFactor);

        aG2d.setColor(sROBOT_BASE_COLOR);
        aG2d.fill(robotBase);
    }

    private void drawWinch(Graphics2D aG2d)
    {
        Color color = sROBOT_WINCH_COLOR;
        if (getWinchMotorSpeed() == 0)
        {
            color = sROBOT_WINCH_COLOR;
        }
        else
        {
            color = Util.getMotorColor(getWinchMotorSpeed());
        }
        
        double winchRadius = 2 * sWINCH_RADIUS * mScaleFactor;
        Ellipse2D winch = new Ellipse2D.Double(
                sWINCH_X_START * mScaleFactor, sWINCH_Y_START * mScaleFactor, winchRadius, winchRadius);

        aG2d.setColor(color);
        aG2d.fill(winch);
    }

    /**
     * Draw Elevator.
     * @param aG2d Graphic.
     */
    public void drawElevator(Graphics2D aG2d)
    {
        Color color = sROBOT_ELEVATOR_COLOR;
        Rectangle2D elevator = new Rectangle2D.Double(sELEVATOR_X_START * mScaleFactor, sELEVATOR_Y_START * mScaleFactor,
                sELEVATOR_WIDTH * mScaleFactor, sELEVATOR_HEIGHT * mScaleFactor);
        
        aG2d.setColor(color);
        aG2d.fill(elevator);
    }

    /**
     * Draw the claw.
     * @param aG2d graphic.
     */
    public void drawClaw(Graphics2D aG2d)
    {

        Color color = sROBOT_CLAW_COLOR;
        if (mClawIsOpen)
        {
            color = sROBOT_CLAW_COLOR;
        }
        else
        {
            color = sROBOT_CLAW_COLOR_CLOSE;
        }

        double clawDrawingHeight = (sCLAW_Y_START - mClawHeight) * mScaleFactor;

        Rectangle2D claw = new Rectangle2D.Double(sCLAW_X_START * mScaleFactor, clawDrawingHeight, sCLAW_WIDTH * mScaleFactor,
                sCLAW_HEIGHT * mScaleFactor);

        aG2d.setColor(color);
        aG2d.fill(claw);
    }

    /**
     * Paint.
     */
    @Override
    public void paint(Graphics aGraphics)
    {
        Graphics2D g2d = (Graphics2D) aGraphics;

        aGraphics.clearRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());

        // Draw Robot Parts
        drawRobotBase(g2d);
        drawWinch(g2d);
        drawClaw(g2d);
        drawElevator(g2d);
    }

    /**
     * Get Winch Modotr Speed.
     * @return double winch motor speed.
     */
    public double getWinchMotorSpeed()
    {
        return mWinchSpeed;
    }

    /**
     * Set the Winch Motor Speed.
     * @param aRopeMotorSpeed double rope speed.
     */
    public void setWinchMotorSpeed(double aRopeMotorSpeed)
    {
        mWinchSpeed = aRopeMotorSpeed;
    }

    /**
     * Is the claw open.
     * @return boolean true if open false if closed.
     */
    public boolean isClawOpen()
    {
        return mClawIsOpen;
    }

    /**
     * set claw Is Open.
     * @param aClawIsOpen true if open false if closed.
     */
    public void setClawIsOpen(boolean aClawIsOpen)
    {
        mClawIsOpen = aClawIsOpen;
    }

    /**
     * set claw height.
     * @param aClawHeight double claw height in inches.
     */
    public void setClawHeight(double aClawHeight)
    {
        mClawHeight = aClawHeight;
    }
}
