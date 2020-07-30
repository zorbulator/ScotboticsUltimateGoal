public class PIDController {
    private final double projected;
    private final double integral;
    private final double derivative;
    private int previousTime;
    private double previousError = 0;
    private double previousIntegral = 0;

    public PIDController(double projected, double intergral, double derivative)
    {
        this.projected = projected;
        this.intergral = intergral;
        this.derivative = derivative;
        previousTime = getTime();
    }

    public double update(double error)
    {
        int time = getTime();
        int deltaTime = time - previousTime;
        previousTime = time;
        previousIntegral += error * deltatime;
        double output = 
            projected * error +
            previousIntegral +
            (error-error_prior) /deltatime;
        previousError = error;
        return output
    }
}