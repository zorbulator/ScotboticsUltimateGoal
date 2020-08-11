package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

/* Class for calculating the PID error correction in discrete time. */
public class PIDController {

    private final double projected, integral, derivative;
    private double errorPrior, integralPrior;
    private ElapsedTime period = new ElapsedTime();

    public PIDController(double projected, double integral, double derivative)
    {
        this.projected  = projected;
        this.integral   = integral;
        this.derivative = derivative;
    }

    public double getErrorCorrection(double error)
    {
        long deltaTime = period.nanoseconds();
        period.reset();
        return projected  * error +
               integral   * (integralPrior += error * deltaTime) +
               derivative * (-errorPrior + (errorPrior=error)) / deltaTime;
    }
}