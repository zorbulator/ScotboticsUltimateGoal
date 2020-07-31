import java.lang.System;   


public class PIDController {

    private final double projected, integral, derivative;
    private long       timePrior = System.currentTimeMillis();
    private double    errorPrior = 0;
    private double integralPrior = 0;

    public PIDController(double projected, double integral, double derivative)
    {
        this.projected  = projected;
        this.integral   = integral;
        this.derivative = derivative;
    }

    // Caculate error correction and update "Prior" values.
    public double update(double error)
    {
        long deltaTime = -timePrior + (timePrior=System.currentTimeMillis());
        return projected  * error +
               integral   * (integralPrior += error * deltaTime) +
               derivative * (-errorPrior + (errorPrior=error)) / deltaTime;
    }
}