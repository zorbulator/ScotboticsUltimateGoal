package org.firstinspires.ftc.teamcode;

import java.util.Math; 

public class Coord {
    private static ScotBot scotBot;
    
    private double x, y;

    private boolean relative;
    
    private Coord(double x, double y, boolean realitve)
    {
        this.x = x;
        this.y = y;
        this.relative = relative;
    }

    public static void SetScotBot(ScotBot scotBot)
    {
        this.scotBot = scotbot;
    }

    public static Coord CreateAbsolute(double x, double y)
    {
        return new Coord(x, y, false);
    }

    public static Coord CreateReletive(double x, double y)
    {
        return new Coord(x + scotBot.x, y + scotBot.y, true);
    }

    public double GetX()
    {
        return x - relative ? scotBot.x : 0;
    }

    public double GetY()
    {
        return y - relative ? scotBot.y : 0;
    }

    public double GetAbsoluteX()
    {
        return x;
    }

    public double GetAbsoluteY()
    {
        return y;
    }

    public double GetReletiveX()
    {
        return x - scotBot.x;
    }

    public double GetReletiveY()
    {
        return y - scotBot.y;
    }

    public void SetX(double x)
    {
        this.x = x + relative ? scotBot.x : 0;
    }

    public void SetY(double y)
    {
        this.y = y + relative ? scotBot.y : 0;
    }

    public void SetAbsoluteX(double x)
    {
        this.x = x;
    }

    public void SetAbsoluteY(double y)
    {
        this.y = y;
    }

    public void SetReletiveX(double x)
    {
        this.x = x + scotBot.x;
    }

    public void SetReletiveY(double y)
    {
        this.y = y + scotBot.y;
    }

    public void Set()
    {
        this.x = x + relative ? scotBot.x : 0;
        this.y = y + relative ? scotBot.y : 0;
    }

    public void SetAbsolute(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void SetRelative(double x, double y)
    {
        this.x = x + scotBot.x;
        this.y = y + scotBot.y;
    }

    public boolean GetRelativity()
    {
        return relative;
    }

    public void SetRelativity(boolean relative)
    {
        this.realtive = relative;
    }

    // The following are other functions that might be usefull but are not essential

    public Coord(Coord parent)
    {
        x = parent.GetAbsoluteX();
        y = parent.GetAbsoluteY();
    }

    public static Coord CreateFromPolar(double theta, double radius)
    {
        return new Coord(radius*Math.cos(theta), radius*Math.sin(theta), true);
    }

    public Coord Plus(Coord other)
    {
        return new Coord(GetX()+other.GetX(), GetY()+other.GetY(), GetRelativity() & other.GetRelativity());
    }
}