import java.lang.System;   

public class PIDController {
    private final double projected;
    private final double integral;
    private final double derivative;
    private long previousTime;
    private double previousError = 0;
    private double previousIntegral = 0;

    public PIDController(double projected, double intergral, double derivative)
    {
        this.projected = projected;
        this.integral = intergral;
        this.derivative = derivative;
        previousTime = System.currentTimeMillis();
    }

    public double update(double error)
    {
        long time = System.currentTimeMillis();
        long deltaTime = time - previousTime;
        previousTime = time;
        previousIntegral += error * deltaTime;
        double output = 
            projected * error +
            previousIntegral +
            (error-previousError) / deltaTime;
        previousError = error;
        return output;
    }
}