import java.lang.System;   
 
/* Class for calculating the PID error correction in discrete time. */
public class PIDController {

    private final double projected, integral, derivative;
    private ElapsedTime   period = new ElapsedTime();
    private double    errorPrior = 0;
    private double integralPrior = 0;

    public PIDController(double projected, double integral, double derivative)
    {
        this.projected  = projected;
        this.integral   = integral;
        this.derivative = derivative;
    }

    /* Calculate PID error correction and update "Prior" values. */
    public double getErrorCorrection(double error)
    {
        long deltaTime = period.now();
        period.reset();
        return projected  * error +
               integral   * (integralPrior += error * deltaTime) +
               derivative * (-errorPrior + (errorPrior=error)) / deltaTime;
    }
}